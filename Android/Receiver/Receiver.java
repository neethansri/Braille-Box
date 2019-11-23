import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import org.json.simple.JSONObject;


public class Receiver {
	
	static Socket socket;
	static ServerSocket serverSocket;
	static InputStreamReader inputStreamReader;
	static BufferedReader bufferReader;
	static String newMessage;
	static String oldMessage;
	

	public static void main(String[] args) {
		
		try {
			serverSocket = new ServerSocket(1001);
			
			int count = 0;
			PrintStream history = new PrintStream(new File("E:\\Users\\Moe\\Desktop\\history.txt"));
			FileWriter fileWriter = new FileWriter("E:\\Users\\Moe\\Desktop\\JSONBox.JSON");
			oldMessage = "";
			while(true) {
				
				socket = serverSocket.accept();
				inputStreamReader = new InputStreamReader(socket.getInputStream());
				bufferReader = new BufferedReader(inputStreamReader);
				newMessage = bufferReader.readLine();
				if (!(oldMessage.equals(newMessage))) {
					System.out.println("Receiving characters");
					count++;
					PrintStream pi = new PrintStream(new File("E:\\Users\\Moe\\Desktop\\pi.txt"));
					// Creating a File object that represents the disk file.  
			        PrintStream console = System.out; 
			        
			        // check if the same person sends the same message
			        String list[] = newMessage.split(",");
			        JSONObject obj = new JSONObject();
			        for (String i : list){
			        	String temp[] = i.split(":");
			        	obj.put(temp[0], temp[1]);
			        }
			        fileWriter = new FileWriter("E:\\Users\\Moe\\Desktop\\JSONBox.JSON", true); //Set true for append mode
			        PrintWriter pw = new PrintWriter(fileWriter);
			        pw.write(obj.toJSONString()+"\n"); 
			        pw.flush();
			        pw.close();
			        
			        // Assign pi to output stream 
			        System.setOut(pi);
			        System.out.println(newMessage);
			        
			        // Assign history to output stream 
			        System.setOut(history); 
			        System.out.println("["+count+"]"+newMessage); 
			  
			        // Use stored value for output stream 
			        System.setOut(console); 
			        System.out.println("["+count+"]"+newMessage);
			        
			        oldMessage = newMessage;
				}else {
					System.out.println("Message was already sent!");
				}

		        

			}
			
		}catch(IOException e) {
			e.printStackTrace();
		}

	}


}
