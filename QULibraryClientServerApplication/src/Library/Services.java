package Library;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Services extends Thread {
	private Socket client;
	private Scanner fromClient;
	private PrintWriter toClient;
	private ObjectInputStream fileTransferInput = null;
	private ObjectOutputStream fileTransfer = null;
	private FileOutputStream fileOutputStream = null;
	private File file = null;
	String currentUserHomeDir = System.getProperty("user.home");
	private String username=null;
	
	FileSystem fileSystem = new FileSystem();
	BookStock bookStock = new BookStock();
	
	private User users[] = {new User("Ayah Abdel-Ghani", "ayah", "password1"), new User("Fatimaelzahraa Ahmed", "fatima", "password2"), new User("Asma Qayummudin", "asma", "password3"), new User("Heba Dawoud", "heba", "password4")};
	private User currentUser;
	
	public Services(Socket client) {
		super();
		this.client = client;
		this.start();
	}

	public void run() {
		boolean isUser = false;
		try {
			fromClient = new Scanner(client.getInputStream());
			toClient = new PrintWriter(client.getOutputStream(), true);
			String message = "";
			boolean cont = false;
			int loginCount = 0;
			
			while(loginCount<5) {
				username = fromClient.nextLine();
				for(int i = 0; i<users.length; i++) {
					if(username.equalsIgnoreCase(users[i].getUsername())) {
						isUser = true;
						currentUser = users[i];
						message = "Enter Password: ";
						toClient.println(message);
						String password = fromClient.nextLine();
						if(password.equals(currentUser.getPassword())) {
							message = "Valid Login";
							toClient.println(message);
							cont = true;
							break;
						}else {
							message = "Invalid Login";
							toClient.println(message);
							loginCount++;
						}
					}
				}
				if(cont == true) {
					break;
				}
				if(!isUser) {
					message = "Not a valid user.";
					toClient.println(message);
					loginCount++;
				}
			}

				while(cont == true) {
					toClient.println("Choose One of the following Services: ");
					toClient.println("1: Borrow a Book");
					toClient.println("2: Rate a Book");
					toClient.println("3: Upload a Book");
					toClient.println("4: Download a Book");
					toClient.println("5: Exit");
					int response = fromClient.nextInt();
					
					switch(response) {
					case 1:
						Book requiredBook = null;
						boolean bookAvailable = false; 
						String checkingBookMessage = "";
						
						toClient.println("Please enter the ISBN of the Book: ");
						Long isbn = fromClient.nextLong();
						
						try {
							requiredBook = bookStock.getBook(isbn);
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						toClient.println(requiredBook);
						if(requiredBook != null) {
							if(requiredBook.isAvailable() == true) {
								message = "Book is Available. You have now Borrowed the Book";
								try {
									bookStock.setAvailability(requiredBook, false);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}else {
								message = "Book is not Available. You cannot borrow this book.";
							}
						}else {
							message = "Book Not in Stock";
						}
						
						toClient.println(message);
						
						break;
					case 2:
						toClient.println("you are rating a book");
						break;
					case 3:
						toClient.println("you are uploading a book");
						break;
					case 4:
						toClient.println("you are downloading book");
						break;
					case 5:
						toClient.println("Thank you for visiting!");
						cont = false;
						break;
					default:
						toClient.println("Invalid");
						break;
					}					
				}

				
		}catch (IOException e) {
			e.printStackTrace();
		}finally {
			if(toClient != null) {
				toClient.close();
			}
			if(fromClient != null) {
				fromClient.close();
			}
			if(client != null) {
				try {
					client.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
}
