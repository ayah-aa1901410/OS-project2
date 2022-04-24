package Library;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Client {
	public static void main(String args[]) {
		Scanner fromUser = null;
		Scanner fromServer = null;
		PrintWriter toServer = null;
		try {
			Socket client = new Socket("localhost", 1500);
			fromUser = new Scanner(System.in);
			fromServer = new Scanner(new InputStreamReader(client.getInputStream()));
			toServer = new PrintWriter(client.getOutputStream(), true);
			int loginCount =0;
			boolean cont = false;
			String message= null;
			
			while(loginCount < 5) {
				System.out.println("Username: ");
				String username = fromUser.nextLine();
				toServer.println(username);
				message = fromServer.nextLine();
				System.out.println(message);
				if(message.equals("Enter Password: ")) {
					String password = fromUser.nextLine();
					toServer.println(password);
					message = fromServer.nextLine();
					System.out.println(message);
					if(message.equals("Valid Login")) {
						cont = true;
						break;
					}else {
						loginCount++;
					}
				}else {
					loginCount++;
				}
			}

				while(cont == true) {
					for(int i = 0; i<6; i++) {
						String menuItem = fromServer.nextLine();
						System.out.println(menuItem);
					}
					
					int response = fromUser.nextInt();
					toServer.println(response);
					
					switch(response) {
					case 1:
						String askForISBN = fromServer.nextLine();
						System.out.println(askForISBN);
						Long isbn = fromUser.nextLong();
						toServer.println(isbn);
						break;
					case 2:
						String response2 = fromServer.nextLine();
						System.out.println(response2);
						break;
					case 3:
						String response3 = fromServer.nextLine();
						System.out.println(response3);
						break;
					case 4:
						String response4 = fromServer.nextLine();
						System.out.println(response4);
						break;
					case 5:
						String response5 = fromServer.nextLine();
						System.out.println(response5);
						cont = false;
						break;
					default:
						String response6 = fromServer.nextLine();
						System.out.println(response6);
						break;
					}
				}

			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			if(fromServer != null) {
				fromServer.close();
			}
			if(toServer != null) {
				toServer.close();
			}
			if(fromUser != null) {
				fromUser.close();
			}
		}
	}
}
