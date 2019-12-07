
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
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
	protected String[] byteArray; 
	protected int index; // Index of character currently being sent from currentChars
	private int port; // Port number of the pi to receive and send packets
	private InetAddress serverAddress; // Address of the server pi
	private int serverPort; // Port of the server pi
	private static final int MAX_SIZE = 300; //Maximum size of a packet sent
	static final int SLEEP_TIME = 2000; //Time in between each character print
	static String flag = ""; //Input string for button flag
	static int count = 0; //Count to keep track of the index of the characters being printed
	static int i = 0;

	/**
	 * Create a RaspberryPi2 object with selected index and port
	 * @param port Port to receive packets from server 
	 * @param index Starting index for the first message sent
	 */
	public RaspberryPi2(int port, int index) {
		this.port = port;
		this.index = index;
	}

	/**
	 * Create a RaspberryPi object with default index (0)
	 * @param port Port to receive packets from server 
	 */
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

		//Create String to store received message
		String received = "";

		try {
			//Open socket and packet to receive information
			DatagramSocket socket = new DatagramSocket(port);
			DatagramPacket packet = new DatagramPacket(new byte[MAX_SIZE], MAX_SIZE);

			//Receive and parse the message
			socket.receive(packet);
			serverAddress = packet.getAddress();
			serverPort = packet.getPort();
			received = new String(packet.getData(), 0, packet.getLength());
			//Close the socket
			socket.close();

		} catch (Exception e) {
			//Return empty message if an error occurs
			return "";
		}

		//Return a string of the message received
		return received;

	}

	/**
	 * Check if the message received was in proper format
	 * @param message String message to be checked for formatting
	 * @return True if in proper format, false otherwise
	 */
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

		// Update character array and byte array
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

	/**
	 * Convert all of the characters received to braille binary
	 * @return an array of String containing all the character with 6 bits
	 */
	public String[] convertAllToBraille() {
		//Create empty array
		int count1 = 0;
		String[] brailles = new String[this.currentChars.length];
		
		//Convert characters from currentChars
		for(char c : this.currentChars) {
			
			brailles[count1] = this.convertCharToBraille(c);
			count1++;
		}
		//Return the array of braille binary
		return brailles;
	}
	
	/**
	 * Send the next parsed character to the Arduino
	 * 
	 * @param lastChar if the character the last one in the text
	 */
	public void sendNextChar() {

		//Open Serial port between Pi and Arduino
		SerialPort sPort = SerialPort.getCommPort("COM5");
		sPort.setComPortParameters(9600, 8, 1, 0);
		sPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		
		//Check if port is open
		System.out.println("Open port: " + sPort.openPort());
		Scanner in = new Scanner(sPort.getInputStream());
		
		//Check flag status
	    flag = in.nextLine();
		System.out.println(flag);
		while(count < currentChars.length) {
			in = new Scanner(sPort.getInputStream());
			flag = in.nextLine();
			if (flag.equals("1")) {
				try {
					//Send character
					sPort.getOutputStream().write(byteArray[count].getBytes());
					sPort.getOutputStream().flush();
					
					Thread.sleep(SLEEP_TIME);

				} catch (IOException | InterruptedException e) {

					e.printStackTrace();
				}
				System.out.println("Sent Number " + byteArray[count]);

				//Increment index
				count++;
			}
			else {
			}

		}
		//Reset index and character array
		currentChars = null;
		count = 0;

	}

	/**
	 * Get the current character array
	 * @return an array of characters representing the current message to be printed
	 */
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

		//Create a scanner to receive port number from the user
		Scanner scanner = new Scanner(System.in);
		System.out.println("Please enter port number:");
		int portNum = scanner.nextInt();
		
		//Open the serial port connection between the Pi and the Arduino
		//Use COM for Windows, /dev/ttyACM0 for Pi
		SerialPort sPort = SerialPort.getCommPort("/dev/ttyACM0");
		sPort.setComPortParameters(9600, 8, 1, 0);
		sPort.setComPortTimeouts(SerialPort.TIMEOUT_SCANNER, 0, 0);
		
		//Confirm if port is open
		System.out.println("Open port: " + sPort.openPort());
		
		while (true) {

			
			// Create pi object with specific port
			RaspberryPi2 pi = new RaspberryPi2(portNum);
			
			// Receive the characters
			System.out.println("Receiving characters");
			String msg = pi.receiveChars();
			boolean check = pi.isProperPacket(msg);

			//If the message is in proper format, continue to sending 
			if (check) {
					
					//Get the input stream from the Pi	
					Scanner in = new Scanner(sPort.getInputStream());
					
					//Check the value of the flag
					pi.flag = in.nextLine();
					
					//Loop for each character in the character array
					while(pi.count < pi.currentChars.length) {
						
						//Check the value of the flag
						in = new Scanner(sPort.getInputStream());
						pi.flag = in.nextLine();

						//If Arduino is in Play mode, send characters
						if (pi.flag.equals("1")) {
							try {
								//Send the 6 bits for character
								sPort.getOutputStream().write(pi.byteArray[count].getBytes());
								sPort.getOutputStream().flush();
								
								Thread.sleep(pi.SLEEP_TIME);

							} catch (IOException | InterruptedException e) {

								e.printStackTrace();
							}
							System.out.println("Sent Number " + pi.byteArray[count]);

							//Increment index
							pi.count++;
						}
						else {
						}

					}

					//Message was sent, reset index and array of characters
					pi.currentChars = null;
					pi.count = 0;					

			}
		}
		
	}

}
