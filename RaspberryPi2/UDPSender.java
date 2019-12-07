
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Scanner;

/**
 * Basic UDP sender to test communication
 * @author idirz
 *
 */
public class UDPSender extends Thread {
	
	public static void main(String[] args) throws IOException {
		
		Scanner scanner = new Scanner(System.in);
		DatagramSocket socket1 = new DatagramSocket();
		while (true) {
			
			System.out.println("Enter text to be sent, ENTER to quit ");
			String message = scanner.nextLine();
			if (message.length()==0) break;
			byte [] data = message.getBytes() ;
			
			DatagramPacket packet = new DatagramPacket(data, data.length, InetAddress.getByName("10.0.0.1"), Integer.parseInt(args[0]) ) ;
			socket1.send( packet );
			

		}
		scanner.close();
		socket1.close();
	}
	
}
