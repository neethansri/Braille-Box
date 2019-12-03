
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
 *
 */
public class RaspberryPi2 {

	private BrailleDictionary BRAILLE_CHART = new BrailleDictionary(); // Object for the braille dictionary
	protected char[] currentChars; // Character array of the current message being sent
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

	/**
	 * Send the next parsed character to the Arduino
	 * 
	 * @param lastChar if the character the last one in the text
	 */
	public void sendNextChar(int buttonFlag) {

		String s = "";
		int count = 0;
		int i = 0;
		String[] byteArray = new String[] { "101010", "111111", "101100", "011001" };

		SerialPort port = SerialPort.getCommPort("COM5");
		port.setComPortParameters(9600, 8, 1, 0);
		port.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		System.out.println("Open port: " + port.openPort());
		Scanner in = new Scanner(port.getInputStream());

		port.addDataListener(new SerialPortDataListener() {
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
							port.getOutputStream().write(byteArray[count].getBytes());
							port.getOutputStream().flush();
							Thread.sleep(5000);

						} catch (IOException | InterruptedException e) {

							e.printStackTrace();
						}
						System.out.println("Sent Number " + byteArray[count]);

						count++;
					}

					if (count == 4) {
						count = 0;
					}

					while (port.bytesAvailable() > 0) {
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

		int[] input = new int[] { 1, 1, 1, 0, 0, 1, 1 };

		while (true) {

			// Create pi object with specific port
			RaspberryPi2 pi = new RaspberryPi2(1002);

			// Receive the characters
			System.out.println("Receiving characters");
			String msg = pi.receiveChars();
			boolean check = pi.isProperPacket(msg);

			if (check) {

				while (!pi.isLastChar()) {
					// Check for Arduino input (1 or 0)

					pi.sendNextChar();

				}

				pi.sendNextChar();
			}
			pi.index = 0;

		}

	}

}
