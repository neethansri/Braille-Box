import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import com.fazecast.jSerialComm.*;

public class RaspberryPi2 {
	private BrailleDictionary BRAILLE_CHART = new BrailleDictionary();
	private char[] currentChars;
	private String binaryMessage;
	private int index;
	private int port;
	private InetAddress serverAddress;
	private int serverPort;
	private String flag;


	public RaspberryPi2(int port) {
		this.port = port;
	}

	/**
	 * Receive the characters of the text from the server
	 * 
	 * @return True if the received characters are in proper format, False otherwise
	 */
	public boolean receiveChars() {
		// Receive JSON and store in array

		String received = "";

		try {
			DatagramSocket socket = new DatagramSocket(port);
			DatagramPacket packet = new DatagramPacket(new byte[300], 300);

			socket.receive(packet);
			serverAddress = packet.getAddress();
			serverPort = packet.getPort();
			received = new String(packet.getData(), 0, packet.getLength());
			socket.close();

		} catch (Exception e) {
			return false;
		}

		// Check if something was actually sent
		if (received.toCharArray().length == 0) {
			return false;
		}

		// Transfer received data to character array
		currentChars = received.toCharArray();

		// Check for invalid characters
		for (char c : currentChars) {
			if (!BRAILLE_CHART.contains(c)) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Send feedback to server if characters were successfully received
	 * 
	 * @param validChar The status of the received characters
	 * @throws IOException
	 */
	public void sendResult(boolean validChar) {

		byte[] data = String.valueOf(validChar).getBytes();

		DatagramSocket socket;
		try {
			socket = new DatagramSocket(port);
			DatagramPacket packet = new DatagramPacket(data, data.length, serverAddress, serverPort);

			socket.send(packet);
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
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

		return BRAILLE_CHART.getBraille(c);
	}

	/**
	 * Send the next parsed character to the Arduino
	 * 
	 * @param lastChar if the character the last one in the text
	 */
	public void sendNextChar(boolean lastChar) {
		this.flag = "1";
		String portDescriptor = "COM5";
		// Access the flag
		SerialPort.getCommPort(portDescriptor);

		if (Integer.parseInt(this.flag) == 1) {
			// send this.currentChars[index]
			// index++;
		}

		index++;
	}

	public char[] getCharArray() {
		return this.currentChars;
	}

	/**
	 * Find out if the previous character was the last one
	 * 
	 * @return True if it is the last one, false otherwise
	 */
	public boolean isLastChar() {
		return index >= currentChars.length;
	}

	public static void main(String[] args) {

		/*
		 * TEST 1: BRAILLE DICTIONARY TEST
		 * 
		 * RaspberryPi2 pi = new RaspberryPi2();
		 * 
		 * String s = "ab cd!"; char[] array = s.toCharArray();
		 * System.out.println(array); for(char c : array) {
		 * System.out.println(pi.convertCharToBraille(c)); }
		 */

		/*
		 * TEST 2: RECEVING CHAR TEST
		 * 
		 * RaspberryPi2 pi = new RaspberryPi2();
		 * System.out.println("Receiving characters"); pi.receiveChars();
		 * System.out.println(pi.getCharArray()); }
		 */

		while (true) {

			// Create pi object with specific port
			RaspberryPi2 pi = new RaspberryPi2(1002);

			// Receive the characters
			System.out.println("Receiving characters");
			boolean check = pi.receiveChars();
			System.out.println(pi.getCharArray());

			// Send the result to the server Pi
			pi.sendResult(check);

			if (check) {
				// int count = 0;

				while (!pi.isLastChar()) {
					// Check for Arduino input (1 or 0)

					pi.sendNextChar(false);
					// System.out.println(count);
					// count++;
				}

				pi.sendNextChar(true);
			}

		}

	}

}
