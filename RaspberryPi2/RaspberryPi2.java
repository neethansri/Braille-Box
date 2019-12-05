

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.IOException;
import java.util.Scanner;

/**
 * RaspberryPi2 is a class responsible for the operations concerning the client
 * RaspberryPi (RPi2)
 * 
 * @author idirz
 * @author solan
 */
public class RaspberryPi2 {

	private BrailleDictionary BRAILLE_CHART = new BrailleDictionary(); // Object for the braille dictionary
	protected char[] currentChars; // Character array of the current message being sent
	protected String[] byteArray; 
	protected int index; // Index of character currently being sent from currentChars
	private int port; // Port number of the pi to receive and send packets
	private InetAddress serverAddress; // Address of the server pi
	private int serverPort; // Port of the server pi
	private String flag;
	private static final int MAX_SIZE = 300;
	static String s = "";
	static int count = 0;
	static int i = 0;

	public RaspberryPi2(int port, int index) {
		this.port = port;
		this.index = index;
	}

	public RaspberryPi2(int port) {
		this.port = port;
		this.index = 0;
	}

	/**
	 * Receive the characters of the text from the server
	 * 
	 * @return True if the received characters are in proper format, False otherwise
	 */
	public String receiveChars() {
		// Receive JSON and store in array

		String received = "";

		try {
			DatagramSocket socket = new DatagramSocket(port);
			DatagramPacket packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);

			socket.receive(packet);
			serverAddress = packet.getAddress();
			serverPort = packet.getPort();
			received = new String(packet.getData(), 0, packet.getLength());
			socket.close();

		} catch (Exception e) {
			return "";
		}

		return received;

	}

	public boolean isProperPacket(String message) {

		// Check if input is null
		if (message == null) {
			return false;
		}
		// Check if the string is empty
		if (message.length() == 0) {
			return false;
		}

		// Check for invalid characters
		for (char c : message.toCharArray()) {
			if (!BRAILLE_CHART.contains(c)) {
				return false;
			}
		}

		// Update current character
		currentChars = message.toCharArray();
		this.byteArray = this.convertAllToBraille();
		
		return true;
	}

	/**
	 * Send feedback to server if characters were successfully received
	 * 
	 * @param validChar The status of the received characters
	 * @throws IOException
	 */
	public void sendResult(boolean validChar) {

		// Convert boolean to bytes
		byte[] data = String.valueOf(validChar).getBytes();

		DatagramSocket socket;
		try {
			// Create Packet with the address and port stored from receival
			socket = new DatagramSocket(port);
			DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Convert the next character in the collection to braille
	 * 
	 * @param c The character to be converted
	 * @return A string containing the braille version of the character
	 */
	public String convertCharToBraille(char c) {
		if (BRAILLE_CHART.contains(c)) {
			return BRAILLE_CHART.getBraille(c);
		} else {
			// If character is invalid, just display empty char
			return BRAILLE_CHART.getBraille(' ');
		}
	}

	public String[] convertAllToBraille() {
		int count1 = 0;
		String[] brailles = new String[this.currentChars.length];
		for(char c : this.currentChars) {
			brailles[count1] = this.convertCharToBraille(c);
			count1++;
		}
		return brailles;
	}
	
	/**
	 * Send the next parsed character to the Arduino
	 * 
	 * @param lastChar if the character the last one in the text
	 */
	public void sendNextChar() {


		SerialPort sPort = SerialPort.getCommPort("/dev/ttyACM0");
		sPort.setComPortParameters(9600, 8, 1, 0);
		sPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		System.out.println("Open port: " + sPort.openPort());
		Scanner in = new Scanner(sPort.getInputStream());

		sPort.addDataListener(new SerialPortDataListener() {
			@Override
			public int getListeningEvents() {
				return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
			}

			@Override
			public void serialEvent(SerialPortEvent serialPortEvent) {

				s = in.nextLine();
				System.out.println(s);

				while (true) {
					if (s.equals("1")) {
						try {
							sPort.getOutputStream().write(byteArray[count].getBytes());
							sPort.getOutputStream().flush();
							Thread.sleep(2000);

						} catch (IOException | InterruptedException e) {

							e.printStackTrace();
						}
						System.out.println("Sent Number " + byteArray[count]);

						count++;
					}

					if (count == 4) {
						count = 0;
					}

					while (sPort.bytesAvailable() > 0) {
						s = in.nextLine();
						System.out.println(s);
					}
				}
			}

		});

		// Increment the index for the next character
		index++;
	}

	public char[] getCharArray() {
		// Return null if array was not initialized or is empty
		if (this.currentChars == null) {
			return null;
		} else if (this.currentChars.length == 0) {
			return null;
		}
		return this.currentChars;
	}

	/**
	 * Find out if the previous character was the last one
	 * 
	 * @return True if it is the last one, false otherwise
	 */
	public boolean isLastChar() {
		// Compare index position to the size of the character array
		return index >= currentChars.length;
	}

	public static void main(String[] args) {

		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter port number:");
		int portNum = scanner.nextInt();
		
		SerialPort sPort = SerialPort.getCommPort("/dev/ttyACM0");
		sPort.setComPortParameters(9600, 8, 1, 0);
		sPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		System.out.println("Open port: " + sPort.openPort());
		
		
		
		while (true) {

			
			// Create pi object with specific port
			RaspberryPi2 pi = new RaspberryPi2(portNum);
			
			// Receive the characters
			System.out.println("Receiving characters");
			String msg = pi.receiveChars();
			System.out.println(msg);
			boolean check = pi.isProperPacket(msg);
			System.out.println(check);

			if (check) {
					
					Scanner in = new Scanner(sPort.getInputStream());
			
					pi.s = in.nextLine();
					System.out.println(pi.s);
					while(pi.count < pi.currentChars.length) {
						in = new Scanner(sPort.getInputStream());
						pi.s = in.nextLine();
						//System.out.println(pi.s);
						if (pi.s.equals("1")) {
							try {
								sPort.getOutputStream().write(pi.byteArray[count].getBytes());
								sPort.getOutputStream().flush();
								
								Thread.sleep(2000);

							} catch (IOException | InterruptedException e) {

								e.printStackTrace();
							}
							System.out.println("Sent Number " + pi.byteArray[count]);

							pi.count++;
						}
						else {
						}

					}
					System.out.println("2");
					pi.currentChars = null;
					pi.count = 0;
					
//					sPort.addDataListener(new SerialPortDataListener() {
//						@Override
//						public int getListeningEvents() {
//							return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
//						}
//
//						@Override
//						public void serialEvent(SerialPortEvent serialPortEvent) {
//							
//							
//
//							
//							
//						}
//						
//					});
					

			}
		}
		
	}

}
