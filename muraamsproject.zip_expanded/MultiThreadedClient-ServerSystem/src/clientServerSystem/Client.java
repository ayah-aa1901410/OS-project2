
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.util.Formatter;
import java.util.Scanner;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;

public class Client {
	String currentUsersHomeDir = System.getProperty("user.home");
	private Socket server;
	private Scanner user;
	private PrintWriter toServer;
	private BufferedReader fromServer;
	private String username;

	ObjectOutputStream fileTransfer = null;

	private ObjectInputStream fileTransferInput = null;
	private File file = null;
	private FileEvent fileEvent;

	private FileOutputStream fileOutputStream = null;

	boolean invalid = false;
	String coursePath;
	boolean isInstructor = false;

	public Client() {
		System.out.println(currentUsersHomeDir);
		try {
			server = new Socket("localhost", 4000);
			user = new Scanner(System.in);
			fromServer = new BufferedReader(new InputStreamReader(server.getInputStream()));
			toServer = new PrintWriter(server.getOutputStream(), true);
			username = "";

			// read from user

			do {

				System.out.print("Enter the user name: ");
				username = user.nextLine();
				toServer.println(username);

				String response = fromServer.readLine();

				System.out.println(response);

				if (response.contains("Username not valid")) {
					invalid = true;

				} else {
					if (response.contains("Instructor")) {
						isInstructor = true;
					}
					invalid = false;
				}
			} while (invalid);

			invalid = false;

			do {
				if (invalid) {
					System.out.println("Enter Password:");
				}
				String password = user.nextLine();
				toServer.println(password);

				String responseOnPassword = fromServer.readLine();
				System.out.println(responseOnPassword);

				if (responseOnPassword.contains("Password not valid")) {
					invalid = true;

				} else {
					invalid = false;
				}
			} while (invalid);

			if (!isInstructor) {
				do {
					
					String serverInput;

					while (true) {
						serverInput = fromServer.readLine();
						// System.out.println(serverInput);
						if (serverInput.equals("Done") || serverInput == null) {
							// System.out.println(serverInput);
							break;
						}
						System.out.println(serverInput);
					}

					Scanner selection = new Scanner(System.in);

					switch (selection.nextInt()) {
					case 1:

						invalid = false;
						toServer.println("1");
						navigateToProject(username);
						

						if (invalid) {
							break;
						} else {
							submitFile();
							invalid = true;
						}
						break;
					case 2:
						invalid = false;
						toServer.println("2");
						navigateToProject(username);

						if(invalid){
							System.out.println(invalid);
							break;
						
						}else{
						boolean back = false;
						do {
							String serverIn;

							while (true) {
								try {

									serverIn = fromServer.readLine();

									if (serverIn.equals("Done") || serverIn == null) {

										break;
									}
									System.out.println(serverIn);

								} catch (IOException e) {
								}

							}
							Scanner in = new Scanner(System.in);
							int selected;

							do {
								boolean invalid = false;
								try {
									selected = in.nextInt();

									toServer.println(selected);

									serverIn = fromServer.readLine();

									if (serverIn.equalsIgnoreCase("Invalid")) {
										System.out.println("Please Enter a Valid Option");
										invalid = true;
									}
								} catch (Exception e) {
									// TODO: handle exception
								}
							} while (invalid);

							while (true) {
								try {

									serverIn = fromServer.readLine();

									if (serverIn.equals("Done") || serverIn == null) {
										break;
									}
									System.out.println(serverIn);

								} catch (IOException e) {
								}

							}

							String download = in.next();
							toServer.println(download);
							if (download.equals("y") || download.equals("Y")) {
								serverIn = fromServer.readLine();
								System.out.println(serverIn);

								String destPath = user.nextLine();
								toServer.println(destPath);

								downloadFeedback(destPath);
							}

							serverIn = fromServer.readLine();
							System.out.println(serverIn);

							String answer = in.next();
							toServer.println(answer);

							if (answer.equals("y") || answer.equals("Y")) {
								back = true;
							} else {
								back = false;
							}
						} while (back);

						invalid = true;
						}
						
						break;
					case 3:
						invalid = false;
						
						System.exit(0);
					default:
						System.out.println("Wrong selection please choose from 1-3");
						invalid = true;
					}

				} while (invalid);
			} else {
				System.out.println("instructor");
				do {
					String instuctorMenu;

					while (true) {
						instuctorMenu = fromServer.readLine();
						// System.out.println(serverInput);
						if (instuctorMenu.equals("Done") || instuctorMenu == null) {
							// System.out.println(serverInput);
							break;
						}
						System.out.println(instuctorMenu);
					}
					Scanner selection = new Scanner(System.in);

					switch (selection.nextInt()) {
					case 1:
						toServer.println("1");

						String courses;

						while (true) {
							courses = fromServer.readLine();
							// System.out.println(serverInput);
							if (courses.equals("Done") || courses == null) {
								// System.out.println(serverInput);
								break;
							}
							System.out.println(courses);
						}

						Scanner in = new Scanner(System.in);
						int course = in.nextInt();
						toServer.println(course);

						System.out.println(fromServer.readLine());

						String insFilePath = user.nextLine();

						FileEvent fileEvent = new FileEvent();
						String destinationPath = fromServer.readLine();
						String fileName = fromServer.readLine();

						fileEvent.setDestinationDirectory(destinationPath);
						fileEvent.setFilename(fileName);
						fileEvent.setSourceDirectory(insFilePath);
						System.out.println(insFilePath);

						File fileToTransfer = new File(insFilePath);
						try {
							fileTransfer = new ObjectOutputStream(server.getOutputStream());

							DataInputStream fileByteReader = new DataInputStream(new FileInputStream(fileToTransfer));
							long len = (int) fileToTransfer.length();
							byte[] fileBytes = new byte[(int) len];
							int read = 0;
							int numRead = 0;
							while (read < fileBytes.length
									&& (numRead = fileByteReader.read(fileBytes, read, fileBytes.length - read)) >= 0) {
								read = read + numRead;
							}

							fileEvent.setFileSize(len);
							fileEvent.setFileData(fileBytes);
							fileEvent.setStatus("Success");

							fileByteReader.close();
						} catch (Exception e) {
							fileEvent.setStatus("Error");
						}

						try {
							fileTransfer.writeObject(fileEvent);
							fileTransfer.flush();
							System.out.println("Uploaded");
						} catch (IOException e) {
							e.printStackTrace();
						}

						break;

					case 2:
						toServer.println("2");

						String Lcourses;

						while (true) {
							courses = fromServer.readLine();
							// System.out.println(serverInput);
							if (courses.equals("Done") || courses == null) {
								// System.out.println(serverInput);
								break;
							}
							System.out.println(courses);
						}

						Scanner Lin = new Scanner(System.in);
						int Lcourse = Lin.nextInt();
						toServer.println(Lcourse);

						String teams;

						System.out.println(fromServer.readLine());

						while (true) {
							teams = fromServer.readLine();
							// System.out.println(serverInput);
							if (teams.equals("Done") || teams == null) {
								// System.out.println(serverInput);
								break;
							}
							System.out.println(teams);
						}

						Scanner teamIn = new Scanner(System.in);
						int team = teamIn.nextInt();
						toServer.println(team);

						String logging;

						while (true) {
							logging = fromServer.readLine();
							// System.out.println(serverInput);
							if (logging.equals("Done") || logging == null) {
								// System.out.println(serverInput);
								break;
							}
							System.out.println(logging);
						}

						// System.out.println(fromServer.readLine());

						Scanner sc = new Scanner(System.in);
						String download = sc.next();
						if (download.equals("y") || download.equals("Y")) {
							toServer.println(1);
							System.out.println(fromServer.readLine());
							String destPath = user.nextLine();
							toServer.println(destPath);

							downloadFeedback(destPath);

						}

						break;

					case 3:
						toServer.println("3");

						String Tcourses;

						while (true) {
							Tcourses = fromServer.readLine();
							// System.out.println(serverInput);
							if (Tcourses.equals("Done") || Tcourses == null) {
								// System.out.println(serverInput);
								break;
							}
							System.out.println(Tcourses);
						}

						Scanner Tin = new Scanner(System.in);
						int Tcourse = Tin.nextInt();
						toServer.println(Tcourse);

						System.out.println(fromServer.readLine());

						Scanner testCase = new Scanner(System.in);

						String testCasePath = testCase.nextLine();
						String tcFilename = fromServer.readLine();
						String tcPath = fromServer.readLine();

						fileEvent = new FileEvent();

						fileEvent.setDestinationDirectory(tcPath);
						System.out.println("tcPath" + tcPath);
						fileEvent.setFilename(tcFilename);
						fileEvent.setSourceDirectory(testCasePath);
						System.out.println(testCasePath);

						fileToTransfer = new File(testCasePath);
						try {
							fileTransfer = new ObjectOutputStream(server.getOutputStream());

							DataInputStream fileByteReader = new DataInputStream(new FileInputStream(fileToTransfer));
							long len = (int) fileToTransfer.length();
							byte[] fileBytes = new byte[(int) len];
							int read = 0;
							int numRead = 0;
							while (read < fileBytes.length
									&& (numRead = fileByteReader.read(fileBytes, read, fileBytes.length - read)) >= 0) {
								read = read + numRead;
							}

							fileEvent.setFileSize(len);
							fileEvent.setFileData(fileBytes);
							fileEvent.setStatus("Success");

							fileByteReader.close();
						} catch (Exception e) {
							fileEvent.setStatus("Error");
						}

						try {
							fileTransfer.writeObject(fileEvent);
							fileTransfer.flush();
							System.out.println("Uploaded");
						} catch (IOException e) {
							e.printStackTrace();
						}

						break;

					default:
						System.out.println("Wrong selection please choose from 1-4");
						invalid = true;

					}
				} while (invalid);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void navigateToProject(String username) {
		String[] details = new String[2];
		int course;
		int team;

		String serverInput;

		while (true) {
			try {

				serverInput = fromServer.readLine();
				// System.out.println(serverInput);
				if (serverInput.equals("Done") || serverInput == null) {
					// System.out.println(serverInput);
					break;
				}
				System.out.println(serverInput);

			} catch (IOException e) {
			}

		}

		Scanner in = new Scanner(System.in);
		do {
			boolean invalid = false;
			try {
				course = in.nextInt();

				toServer.println(course);
				
				serverInput = fromServer.readLine();

				if (serverInput.equalsIgnoreCase("Invalid")) {
					System.out.println("Please Enter a Valid Option");
					invalid = true;
				}
			} catch (Exception e) {
				// TODO: handle exception
			}
		} while (invalid);

		String state;
		try {
			state = fromServer.readLine();
			if (state.equalsIgnoreCase("Invalid")) {
				System.out.println(
						"You are not enrolled in this course, or you were not added to a team. Please contact your Instructor.");
				invalid = true;
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean submitFile() {

		try {

			String received = fromServer.readLine();
			System.out.println(received);
			if (received.equalsIgnoreCase("Didn't pass")) {

				System.out.println("Enter the path of the project file to submit:");

				String path = user.nextLine();
				toServer.println(path);

				FileEvent fileEvent = new FileEvent();
				String destPath = fromServer.readLine();
				String fileName = fromServer.readLine();
				fileEvent.setDestinationDirectory(destPath);
				fileEvent.setFilename(fileName);
				fileEvent.setSourceDirectory(path);

				File fileToTransfer = new File(path);
				try {
					fileTransfer = new ObjectOutputStream(server.getOutputStream());

					DataInputStream fileByteReader = new DataInputStream(new FileInputStream(fileToTransfer));
					long len = (int) fileToTransfer.length();
					byte[] fileBytes = new byte[(int) len];
					int read = 0;
					int numRead = 0;
					while (read < fileBytes.length
							&& (numRead = fileByteReader.read(fileBytes, read, fileBytes.length - read)) >= 0) {
						read = read + numRead;
					}

					fileEvent.setFileSize(len);
					fileEvent.setFileData(fileBytes);
					fileEvent.setStatus("Success");

					fileByteReader.close();
				} catch (Exception e) {
					fileEvent.setStatus("Error");
				}

				try {
					fileTransfer.writeObject(fileEvent);
					fileTransfer.flush();
					System.out.println("Submission Successful");

				} catch (IOException e) {
					e.printStackTrace();
				}

			} else {
				System.out.println(
						"Unfortunately due date has passed,try your luck by sending your project by email to the instructor :’)");

			}
		} catch (Exception e) {
		}
		return true;

	}

	public void downloadFeedback(String destPath) {
		System.out.println("Downloading...");
		try {

			System.out.println("Contacting Server...");

			fileTransferInput = new ObjectInputStream(server.getInputStream());

			fileEvent = (FileEvent) fileTransferInput.readObject();

			if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
				System.out.println("Error occurred ..So exiting");
				// System.exit(0);
			}
			System.out.println("Receiving file...");
			String outputFile = destPath + "/" + fileEvent.getFilename();

			file = new File(outputFile);
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(fileEvent.getFileData());
			fileOutputStream.flush();
			fileOutputStream.close();

			Logger.getInstance().writeToLog(LocalDateTime.now() + ": " + username + " downloaded the feedback file \n");

		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	public static void main(String[] args) {
		new Client();
	}
}
