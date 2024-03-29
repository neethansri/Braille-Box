import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class JavaSerialCom {
	static String s = "";
	static int count = 0;
	static ArrayList<String> byteArray = new ArrayList<String(); 
			//{ "101010", "111111", "101100", "111001" };

	public static void main(String[] args) {
		for (String bytes : args) {
			byteArray.add(bytes);
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
							port.getOutputStream().write(byteArray.get(count).getBytes());
							port.getOutputStream().flush();
							Thread.sleep(5000);

						} catch (IOException | InterruptedException e) {

							e.printStackTrace();
						}
						System.out.println("Sent Number " + byteArray.get(count));

						count++;
					}

					if (count == 4) {
						count = 0;
					}

					while(port.bytesAvailable()>0) {
						s = in.nextLine();
						System.out.println(s);
					}
				}
			}
			}


		});
	}

}}
