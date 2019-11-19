package model;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;


public class JSONReceiver {

    public static void main(String[] args) throws IOException {
    	DatagramSocket socket1 = new DatagramSocket(1000);
    	while(true) {
    		System.out.println( "Receiving on port " + 1000 ) ;
    		DatagramPacket packet = new DatagramPacket(new byte[256], 256);
    		socket1.receive( packet ) ;
    		System.out.println( packet.getAddress() + " " +
    		packet.getPort() + ": " +
    		new String(packet.getData()).trim());
    		}
    }
    
    
}