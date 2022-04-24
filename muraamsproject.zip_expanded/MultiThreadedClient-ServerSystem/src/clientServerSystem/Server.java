

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
	private ServerSocket service;
	private Socket client;
	public Server(int port) {
		try {
			service = new ServerSocket(port);
			while(true) {//24/7
				client = service.accept();
				new ServerService(client);
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}



	public static void main(String[] args) {
		new Server(4000);

	}

}
