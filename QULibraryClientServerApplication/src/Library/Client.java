package Library;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Client {
	public static void main(String args[]) {
		try {
			boolean success = false;
			Socket client = new Socket("localhost", 1500);
			Scanner fromUser = new Scanner(System.in);
			Scanner fromServer = new Scanner(new InputStreamReader(client.getInputStream()));
			PrintWriter toServer = new PrintWriter(client.getOutputStream(), true);
			
			System.out.println("Username: ");
			String username = fromUser.nextLine();
			toServer.println(username);
			String message = fromServer.nextLine();
			System.out.println(message);
			if(message.equals("Enter Password: ")) {
				String password = fromUser.nextLine();
				toServer.println(password);
				message = fromServer.nextLine();
				System.out.println(message);
				if(message.equals("Valid Login")) {
					success=true;
				}
			}

			if(success) {
				
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
