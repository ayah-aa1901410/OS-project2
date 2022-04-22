package Library;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	public static void main(String args[]) {
		try {
			
			ServerSocket server = new ServerSocket(1500);
			for(;;) {
				
				Socket client = server.accept();
				Services clientThread = new Services(client);
				
				
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
