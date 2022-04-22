package Library;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class Services extends Thread {
	private Socket client;
	private Scanner fromClient;
	private PrintWriter toClient;
	private ObjectInputStream fileTransferInput = null;
	private ObjectOutputStream fileTransfer = null;
	private FileOutputStream fileOutputStream = null;
	private File file = null;
//	private FileEvent fileEvent;
	String currentUserHomeDir = System.getProperty("user.home");
	private String username=null;
	
	private User users[] = {new User("Ayah Abdel-Ghani", "ayah", "password1"), new User("Fatimaelzahraa Ahmed", "fatima", "password2"), new User("Asma Qayummudin", "asma", "password3"), new User("Heba Dawoud", "heba", "password4")};
	private User currentUser;

	public Services(Socket client) {
		super();
		this.client = client;
		this.start();
	}

	
	public void run() {
		boolean isUser = false;
		boolean cont = false;
		try {
			
			fromClient = new Scanner(client.getInputStream());
			toClient = new PrintWriter(client.getOutputStream(), true);
			username = fromClient.nextLine();
			
			String message = "";
			
			while(!cont) {
				for(int i = 0; i<users.length; i++) {
					if(username.equalsIgnoreCase(users[i].getUsername())) {
						isUser = true;
						currentUser = users[i];
						message += "Enter Password: ";
					}
				}
				
				if(!isUser) {
					message += "Not a valid user.";
				}
				
				toClient.println(message);
				
				if(message.equals("Enter Password: ")) {
					String password = fromClient.nextLine();
					if(password.equals(currentUser.getPassword())) {
						message = "Valid Login\r\n";
						toClient.println(message);
						cont = true;
					}else {
						message = "Invalid Login\r\n";
						toClient.println(message);
					}
				}
			}
			
			
			
			
			
		}catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
