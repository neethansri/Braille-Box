import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Receiver {
	
	static Socket socket;
	static ServerSocket serverSocket;
	static InputStreamReader inputStreamReader;
	static BufferedReader bufferReader;
	static String message;
//	static ServerSocket serverSocket2;
//	static Socket socket2;
//	static PrintWriter printWriter;
	

	public static void main(String[] args) {
		
		try {
			serverSocket = new ServerSocket(1001);
			
			int count = 0;
			PrintStream history = new PrintStream(new File("C:\\Users\\mohd-\\Desktop\\history.txt"));
			while(true) {
				
				socket = serverSocket.accept();
				inputStreamReader = new InputStreamReader(socket.getInputStream());
				bufferReader = new BufferedReader(inputStreamReader);
				message = bufferReader.readLine();
				System.out.println("Receiving characters");
				count++;
				PrintStream pi = new PrintStream(new File("C:\\Users\\mohd-\\Desktop\\pi.txt"));
				// Creating a File object that represents the disk file.  
		        PrintStream console = System.out; 
		        
		        // Assign pi to output stream 
		        System.setOut(pi);
		        System.out.println(message);
		        
		        // Assign history to output stream 
		        System.setOut(history); 
		        System.out.println("["+count+"]"+message); 
		  
		        // Use stored value for output stream 
		        System.setOut(console); 
		        System.out.println("["+count+"]"+message);
		        
//		        printWriter = new PrintWriter(socket.getOutputStream());
//				printWriter.write("Message Received");
//				printWriter.flush();
//				printWriter.close();

			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}

	}


}
