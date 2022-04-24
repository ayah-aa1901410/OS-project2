
import java.io.BufferedReader;
import java.io.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.Scanner;
import java.util.Date;
import java.text.SimpleDateFormat;

public class ServerService extends Thread {
	private Socket client;
	private Scanner fromClient;
	private PrintWriter toClient;
	private ObjectInputStream fileTransferInput = null;
	private File file = null;
	private FileEvent fileEvent;
	private FileOutputStream fileOutputStream = null;
	String currentUsersHomeDir = System.getProperty("user.home");
	boolean isInstructor = false;
	private String delimiter = "Done";
	private boolean invalid = false;

	ObjectOutputStream fileTransfer = null;
	// private String currentUser;
	private String username;
	private User users[] = { new Student("se1701977", "password1", "Somaya"),
			new Student("ma1800416", "password2", "Muraam"), new Instructor("sa3456", "password3", "Saleh") };

	String coursePath;

	public ServerService(Socket client) {
		this.client = client;
		this.start();
	}

	public void run() {// any code here is threaded
		// It can be lengthy in time
		// int loginTrials = 0;
		boolean found = false;
		boolean isInstructor = false;
		try {
			do {
				fromClient = new Scanner(client.getInputStream());
				toClient = new PrintWriter(client.getOutputStream(), true);
				username = fromClient.nextLine();

				String response = "";

				for (int i = 0; i < users.length; i++) {
					System.out.println(users[i].getUsername());
					if (username.equalsIgnoreCase(users[i].getUsername())) {
						found = true;
						if (users[i] instanceof Instructor) {
							response += "Instructor, Enter Password:";
							isInstructor = true;
						} else {
							response += "Enter Password:";
						}
					}
				}
				if (!found) {
					response = "Username not valid";

					// loginTrials++;

					// if(loginTrials>3){
					// response+="\nConnection Timed out due to invalid login attempts";
					// }
				}

				toClient.println(response);

				// if(loginTrials>3){
				// client.close();
				// }
			} while (!found);

			found = false;

			do {
				// fromClient = new Scanner(client.getInputStream());
				// toClient = new Formatter(client.getOutputStream());
				String password = fromClient.nextLine();

				String response = "";

				for (int i = 0; i < users.length; i++) {
					if (username.equalsIgnoreCase(users[i].getUsername())) {
						if (password.equalsIgnoreCase(users[i].getPassword())) {
							found = true;
							response += "";
						}
					}
				}
				if (!found) {
					response += "Password not valid";
				}

				toClient.println(response);

			

			} while (!found);
			
			Logger.getInstance().writeToLog(LocalDateTime.now() + ": " + username + " logged in\n");
			
			while (true) {
				if (!isInstructor) {
					toClient.println("Menu");
					toClient.println("----------------------------------");
					toClient.println("1-Upload project");
					toClient.println("2-Recieve data about submitted project");
					toClient.println("3-Exit");
					toClient.println("Enter Your Choice: ");
					toClient.println("Done");

					String choice = fromClient.nextLine();
					System.out.println(choice);

					switch (choice) {
					case "1":
						String path = currentUsersHomeDir
								+ "/Documents/MultiThreadedClient-ServerSystem/src/FileSystem";

						File file = new File(path);
						String[] directories = file.list(new FilenameFilter() {
							@Override
							public boolean accept(File current, String name) {
								return new File(current, name).isDirectory();
							}
						});

						toClient.println("Please select the course:");
						for (int i = 0; i < directories.length; i++) {
							toClient.println((i + 1) + ": " + directories[i].toString());
						}
						toClient.println(delimiter);

						int course;

						while (true) {
							String option = fromClient.nextLine();
							course = Integer.parseInt(option);
							if (course < 1 || course > directories.length) {
								toClient.println("Invalid");
							} else {
								toClient.println("Valid");
								break;
							}
						}

						try {
							String[] details = navigateToProject(course, directories, path);

							System.out.println(details[0]);
							System.out.println(details[1]);

							String FolderPath = details[0];
							String teamdir = details[1];

							String dueDate = "";
							String[] solution;
							String instructorPath;
							String instructorFile;
							String currentDate;
							String[] autoGrading = new String[] { currentUsersHomeDir
									+ "/Documents/MultiThreadedClient-ServerSystem/src/Bash Scripts/autograding.sh" };
							instructorPath = coursePath + "/InstructorFiles/";
							File instFile = new File(instructorPath);
							solution = instFile.list();
							instructorFile = solution[0];

							Date date = new Date();
							SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
							currentDate = formatter.format(date);

							try {
								BufferedReader brTest = new BufferedReader(
										new FileReader(instructorPath + instructorFile));
								dueDate = brTest.readLine();
								// System.out.println("Firstline is : " + dueDate);
							} catch (Exception e) {
							}

							try {
								Date dueDate1 = formatter.parse(dueDate);
								Date currentDate1 = formatter.parse(currentDate);
								if (dueDate1.compareTo(currentDate1) >= 0) {
									System.out.println("FROM SERVER");
									toClient.println("Didn't pass");

									String sourcePath = fromClient.nextLine();
									String[] pieces = teamdir.split("_", 2);
									String fileName = pieces[1];
									fileName += "_v";
									// I AM HERE
									FileFilter filter = new FileFilter() {

										public boolean accept(File f) {
											return f.isFile() == true;
										}
									};

									int version = new File(FolderPath).listFiles(filter).length;

									version++;

									fileName += version;

									String pathToFile = sourcePath.substring(0, sourcePath.lastIndexOf("/") + 1);
									String fileExtension = sourcePath.substring(sourcePath.lastIndexOf(".") + 1);
									

									fileName += ".";
									fileName += fileExtension;

									toClient.println(FolderPath);
									toClient.println(fileName);
									receiveFile();
									ProcessBuilder pb = new ProcessBuilder(autoGrading);

									try {

										Process p = pb.start();

										if (p.isAlive()) {

											BufferedReader reader = new BufferedReader(
													new InputStreamReader(p.getInputStream()));

											String s = null;

											while ((s = reader.readLine()) != null) {
												System.out.println(s);
											}
										}

										p.waitFor();

									} catch (Exception e) {
										e.printStackTrace();
										System.out.println("could not autograde");

									}
									Logger.getInstance().writeToLog(LocalDateTime.now() + ": " + username
											+ " submitted a project to " + teamdir + "\n");
								} else {
									toClient.println("Passed");
								}

							} catch (Exception e) {
								// TODO: handle exception
							}
							// Autograding
							// Versioning (FileName)
							// Logging

						} catch (NullPointerException e) {

						}

						break;
					case "2":
						String filepath = currentUsersHomeDir
								+ "/Documents/MultiThreadedClient-ServerSystem/src/FileSystem/";

						File filesystem = new File(filepath);
						String[] courses = filesystem.list(new FilenameFilter() {
							@Override
							public boolean accept(File current, String name) {
								return new File(current, name).isDirectory();
							}
						});

						toClient.println("Please select the course:");
						for (int i = 0; i < courses.length; i++) {
							toClient.println((i + 1) + ": " + courses[i].toString());
						}
						toClient.println(delimiter);

						int coursec;

						while (true) {
							String option = fromClient.nextLine();
							coursec = Integer.parseInt(option);
							if (coursec < 1 || coursec > courses.length) {
								toClient.println("Invalid");
							} else {
								toClient.println("Valid");
								break;
							}
						}

						String[] dirdetails = navigateToProject(coursec, courses, filepath);
						
						if(invalid){
							continue;
						}

						boolean back = false;

						String feedbackFolderPath = dirdetails[0];
						// String feedbackFolderPath = navigateToProject(username);

						feedbackFolderPath += "/";

						File feedbackFile = new File(feedbackFolderPath);
						String projDirects[] = feedbackFile.list(new FilenameFilter() {
							@Override
							public boolean accept(File current, String name) {
								return new File(current, name).isDirectory();
							}
						});

						for (int i = 0; i < projDirects.length; i++) {
							if (projDirects[i].contains("Feedback")) {
								feedbackFolderPath += projDirects[i].toString();
								break;
							}
						}

						file = new File(feedbackFolderPath);
						String feedbackFiles[] = file.list();

						do {
							toClient.println("Please select the file to view:");
							for (int i = 0; i < feedbackFiles.length; i++) {
								toClient.println((i + 1) + ": " + feedbackFiles[i].toString());
							}
							toClient.println(delimiter);

							int feedchoice;

							while (true) {
								String option = fromClient.nextLine();
								feedchoice = Integer.parseInt(option);
								if (feedchoice < 1 || feedchoice > feedbackFiles.length) {
									toClient.println("Invalid");
								} else {
									toClient.println("Valid");
									break;
								}
							}

							String filename = feedbackFiles[feedchoice - 1].toString();
							String feedbackFilePath = feedbackFolderPath;
							feedbackFilePath += "/";
							feedbackFilePath += filename;

							toClient.println("Content of " + filename);
							toClient.println("----------------------");

							BufferedReader br = new BufferedReader(new FileReader(feedbackFilePath));
							String line;
							while ((line = br.readLine()) != null)
								toClient.println(line);

							Logger.getInstance().writeToLog(LocalDateTime.now() + ": " + username
									+ " viewed the feedback file " + filename + "\n");
							toClient.println("\nDownload Feedback File? (Y/N)");
							toClient.println(delimiter);

							String download = fromClient.nextLine();
							if (download.equals("y") || download.equals("Y")) {
								toClient.println("Enter the destination Folder Path:");
								String destPath = fromClient.nextLine();

								String sourcePath = feedbackFilePath.substring(0,
										feedbackFilePath.lastIndexOf("/") + 1);

								sendFeedback(destPath, filename, sourcePath);
							}

							toClient.println("Navigate back to Feedback Folder? (Y/N)");

							String answer = fromClient.nextLine();

							if (answer.equals("y") || answer.equals("Y")) {
								back = true;
							} else {
								back = false;
							}
						} while (back);

						break;
					case "3":
						break;
					default:
						toClient.println("Wrong selection please choose from 1-3");
					}
				} else {
					toClient.println("Menu");
					toClient.println("----------------------------------");
					toClient.println("1-Add intructor file");
					toClient.println("2-view logging");
					toClient.println("3-Add test cases");
					toClient.println("4-Exit");
					toClient.println("Enter Your Choice: ");
					toClient.println(delimiter);

					String choice = fromClient.nextLine();
					System.out.println(choice);
					switch (choice) {
					case "1":
						String[] courseDetails1 = navigateToInstructorCourse();
						String coursePath1 = courseDetails1[0];
						coursePath1 += "/InstructorFiles/";
						System.out.println(coursePath1);
						toClient.println("Enter the path of the instructor file to upload:");
						String[] pieces = courseDetails1[1].split("_");
						String fileName = pieces[0] + "_project_solution";
						toClient.println(coursePath1);
						toClient.println(fileName);

						receiveFile();
						Logger.getInstance().writeToLog(LocalDateTime.now() + ": " + username + " uploaded "+fileName+"\n");
						break;
					case "2":
						String[] courseDetails2 = navigateToInstructorCourse();
						String Lpath = courseDetails2[0];
						file = new File(Lpath);
						String[] teamDirectories = file.list(new FilenameFilter() {
							@Override
							public boolean accept(File current, String name) {
								return new File(current, name).isDirectory();
							}
						});
						toClient.println("Please select the team:");
						String[] tempTeamDir;

						for (int index = 0; index < teamDirectories.length; index++) {
							if (!teamDirectories[index].contains("InstructorFiles")) {
								toClient.println((index + 1) + ": " + teamDirectories[index].toString());
							}
						}
						toClient.println(delimiter);
						int teamChoice = fromClient.nextInt();

						String teamName = teamDirectories[teamChoice - 1].toString();
						System.out.println(teamName);
						Lpath += "/";
						Lpath += teamName;
						String teamPath = Lpath;
						System.out.println("team path" + teamPath);
						file = new File(teamPath);
						String[] directories = file.list();

						String projectPath = teamPath + "/" + directories[0] + "/";
						System.out.println(projectPath);
						String loggingPath = projectPath + "FeedbackFolder/logging.txt";
						System.out.println(loggingPath);
						BufferedReader br = new BufferedReader(new FileReader(loggingPath));
						String line;
						
						toClient.println("Content of " + teamName + " logging File");
						System.out.println("----------------------");
						while ((line = br.readLine()) != null)
							toClient.println(line);

						Logger.getInstance().writeToLog(LocalDateTime.now() + ": " + username + " viewed logfile for team "+teamName+"\n");
						toClient.println("\nDownload Feedback File? (Y/N)");
						toClient.println(delimiter);

						int download = fromClient.nextInt();
						if (download == 1) {
							toClient.println("Enter the destination Folder Path:");
						}

						String desPath = fromClient.nextLine();

						System.out.println("destpath" + desPath);
						String filename = "logging.txt";
						
						String sourcePath = loggingPath.substring(0, loggingPath.lastIndexOf("/") + 1);
						

						sendFeedback(desPath, filename, sourcePath);

						Logger.getInstance()
								.writeToLog(LocalDateTime.now() + ": Instructor " + username + " downloaded logging file for team "+teamName+"\n");
						break;
					case "3":
						String[] courseDetails3 = navigateToInstructorCourse();
						String tcPath = courseDetails3[0];
						File teamsfile = new File(tcPath);
						String[] tcTeamDirectories = teamsfile.list(new FilenameFilter() {
							@Override
							public boolean accept(File current, String name) {
								return new File(current, name).isDirectory();
							}
						});
						for (int teamsIndex = 0; teamsIndex < tcTeamDirectories.length; teamsIndex++) {
							if (!tcTeamDirectories[teamsIndex].contains("InstructorFiles")) {
								System.out.println("Team path" + tcPath);
								tcPath += "/";
								tcPath += tcTeamDirectories[teamsIndex];

								System.out.println(tcPath);
								File projFile = new File(tcPath);
								String[] projDirectories = projFile.list();
								System.out.println("proj file:" + projDirectories[0]);

								tcPath += "/";
								tcPath += projDirectories[0];
								tcPath += "/TestCases/";
								toClient.println("Enter the path of the test case to upload:");
								String testCasePath = fromClient.nextLine();
								int counter = new File(tcPath).list().length;

								counter++;

								String tcFilename = "TestCase" + counter;
								toClient.println(tcFilename);
								toClient.println(tcPath);
								receiveFile();

								Logger.getInstance().writeToLog(LocalDateTime.now() + ": Instructor" + username + " uploaded testcases for course "+courseDetails3[1]+"\n");
							}
						}

						break;

					case "4":
						Logger.getInstance()
								.writeToLog(LocalDateTime.now() + ": Instructor " + username + " logged out\n");
						// System.exit(0);
						break;
					default:
						toClient.println("Wrong selection please choose from 1-4");

					}

				}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private String[] navigateToProject(int course, String[] directories, String path) {
		String[] details = new String[2];
		String coursename = directories[course - 1].toString();
		path += "/";
		path += coursename;
		coursePath = path;
		file = new File(path);
		directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});

		String teamdir = null;
		for (int i = 0; i < directories.length; i++) {
			if (directories[i].contains(username)) {
				teamdir = directories[i].toString();
				break;
			}
		}

		try {
			if (teamdir != null) {
				path += "/";
				path += teamdir;
				
				file = new File(path);
				directories = file.list();

				path += "/";
				path += directories[0];
				path += "/";

				details[0] = path;
				details[1] = teamdir;

				toClient.println("valid");
				invalid = false;
				return details;
			} else {
				toClient.println("Invalid");
				invalid = true;
			}
		} catch (NullPointerException e) {

		}
		return null;

	}

	private String[] navigateToInstructorCourse() {
		String[] courseDetails = new String[2];
		String path = currentUsersHomeDir + "/Documents/MultiThreadedClient-ServerSystem/src/FileSystem/";
		File file = new File(path);
		String[] directories = file.list(new FilenameFilter() {
			@Override
			public boolean accept(File current, String name) {
				return new File(current, name).isDirectory();
			}
		});
		ArrayList<String> cInstCourses = new ArrayList<String>();
		for (int i = 0; i < directories.length; i++) {
			if (directories[i].contains(username)) {
				cInstCourses.add(directories[i]);

			}
		}
		toClient.println("Please select the course:");
		for (int i = 0; i < cInstCourses.size(); i++) {
			toClient.println((i + 1) + ": " + cInstCourses.get(i).toString());
		}
		toClient.println(delimiter);

		int course = fromClient.nextInt();

		String coursename = cInstCourses.get(course - 1).toString();
		path += coursename;

		courseDetails[0] = path;
		courseDetails[1] = coursename;
		return courseDetails;

	}

	private void receiveFile() {

		try {
			fileTransferInput = new ObjectInputStream(client.getInputStream());

			fileEvent = (FileEvent) fileTransferInput.readObject();

			if (fileEvent.getStatus().equalsIgnoreCase("Error")) {
				System.out.println("Error occurred ..So exiting");
				
			}

			String outputFile = fileEvent.getDestinationDirectory() + fileEvent.getFilename();
			System.out.println(outputFile);


			file = new File(outputFile);
			fileOutputStream = new FileOutputStream(file);
			fileOutputStream.write(fileEvent.getFileData());
			fileOutputStream.flush();
			fileOutputStream.close();

		} catch (Exception e) {
			// TODO: handle exception
		}

	}

	private void sendFeedback(String destPath, String filename, String sourcePath) {
	

		FileEvent fileEvent = new FileEvent();

		fileEvent.setDestinationDirectory(destPath);
		fileEvent.setFilename(filename);
		fileEvent.setSourceDirectory(sourcePath);

		File fileToTransfer = new File(sourcePath + filename);
		System.out.println(sourcePath + filename);
		try {
			fileTransfer = new ObjectOutputStream(client.getOutputStream());

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

			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
