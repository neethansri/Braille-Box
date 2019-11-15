import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

public class RaspberryPi2 {
	private BrailleDictionary BRAILLE_CHART = new BrailleDictionary();
	private char[] currentChars;
	private String binaryMessage;
	private int index;
	
	
	public RaspberryPi2() {
		
	}
	
	
	/**
	 * Receive the characters of the text from the server
	 * @return True if the received characters are in proper format,
	 * False otherwise
	 */
	public boolean receiveChars() {
		//Receive JSON and store in array
		
		byte[] buf = "hi".getBytes();
		String received = "";
		
		try {
			DatagramSocket socket = new DatagramSocket();
			InetAddress address = InetAddress.getByName("localhost");
			DatagramPacket packet = new DatagramPacket(buf, buf.length, address, 4445);
			
			socket.send(packet);
	        packet = new DatagramPacket(buf, buf.length);
	        socket.receive(packet);
	        received = new String(packet.getData(), 0, packet.getLength());
	        socket.close();
	        
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		
		currentChars = received.toCharArray(); 
	    
		
		
		
		//Check for invalid characters
		for(char c : currentChars) {
			if(!BRAILLE_CHART.contains(c)) {
				return false;
			}
		}
		
		//
		
		
		return false;
	}
	
	/**
	 * Send feedback to server if characters were successfully received
	 * @param validChar The status of the received characters
	 */
	public void sendResult(boolean validChar) {
		//Send JSON back to Server
	}
	
	/**
	 * Convert the next character in the collection to braille
	 * @param c The character to be converted
	 * @return A string containing the braille version of the character
	 */
	public String convertCharToBraille(char c) {

		return BRAILLE_CHART.getBraille(c);
	}
	
	/**
	 * Send the next parsed character to the Arduino
	 * @param lastChar if the character the last one in the text
	 */
	public void sendNextChar(boolean lastChar) {
		
		//Solan code
		
		index++;
	}
	
	public char[] getCharArray() {
		return this.currentChars;
	}
	
	/**
	 * Find out if the next character is the last one
	 * @return True if it is the last one, false otherwise
	 */
	public boolean isLastChar() {
		return index == currentChars.length;
	}
	
	
	public static void main(String[] args) {
		System.out.println("hello");

		RaspberryPi2 pi = new RaspberryPi2();
		
		String s = "ab cd!";
		char[] array = s.toCharArray();
		System.out.println(array);
		for(char c : array) {
			System.out.println(pi.convertCharToBraille(c));
		}
		
		
	}

	
	
}
