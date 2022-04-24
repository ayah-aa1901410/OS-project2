## Getting Started
In order to run and try our application please follow the following steps:

Run the server java file.
Run the Client java file to start interacting with our application.
You will be asked to enter a username. Right now there are 3 legitimate users stored in the system, and they are as follows:
    Student: Somaya, username: se1701977, password: password1
    Student: Muraam, username: ma1800416, password: password2
    Instructor: SalehAlhazbi, username: sa3456, password: password3

Start by logging in using one of the Student user credentials.
On entering an incorrect username or password, you should be prompted that the respective credential is invalid.

On successful login, you should be presented with the Student Menu.
The options are as follows:
    Option 1:
        The student can upload their project to a certain course
        Enter the number 1 to select this option.
        You will be prompted with the list of courses.
        Enter the course that you are registered in 
        At the moment, both students are registered in CMPS151_SalehAlhazbi(sa3456)
        Thus choose the option 1
        On entering 2 or 3, you will be informed that you are not registered in this course and will be redirected back to the menu
        On entering 1, you will be prompted to enter the project file path on your machine
        Eg: /home/ra/Documents/project1.sh
        The system will perform the uploading of the file to the file system and will autograde the project submitted.
        You will then be redirected to the main menu again
        To view the feedback file for the autograded project, please select the second option in the menu

        NOTE: If the autograding was not triggered, you may need to change the permissions of the autograding script located in the Bash Scripts directory under the src folder of the project from the command line to make it executible. Once this issue was faced, and the reason was that the autograding script did not have an execution permission.
    
    Option 2:
        This will allow you to view the feedback files for submitted projects.
        Enter the course number that you are registered in. Just like in the previous option, the course that the students are currently in is the CMPS151
        Entering 2 or 3 will prompt the same as in Option 1
        Select 1 to view the feedback files for submitted projects.
        To view the feedback file of the latest project, be sure to select the number that corresponds to the feedback file with the highest value, as that corresponds to the submission number.

        Enter the number to view the file content

        You will then be prompted with a question to know if you want to download this feedback file.
        Enter y or Y to download.
        On downloading, enter the path to download the project in 
        Eg:  home/ra/Documents/
        The process of downloading will take place. You will then have the feedback file downloaded to your specified location
        You will then be asked if you would like to navigate to the feedback file list. If you select y or Y, you will be navigated back to it. 
        If you choose not to navigate, you will be returned to the main menu.
    Option 3:
        Choose this option if you would like to exit the system


To run the instructor menu:
Enter the credentials of the instructor. The authentication process is the same as for the students.
On successfully logging in, you will see the instructor menu. The options are as follows:

    Option 1:
        This option allows the instructor to upload the instructor file that contains the duedate for the project and the solution or solutions expected depending on whether testcases are used in the project or the project contains embedded input.

        On entering 1, the instructor will view the courses that they teach, and can select one of them to upload the instructor files.
        Currently, you will see only 1 course for this Instructor.
        Enter the number for the course , 1
        You will be prompted to enter the path to upload the instructor file.
        Eg: /home/ra/Desktop/projectsolution
        You will be informed that the instructor file was successfully uploaded
        The instructor file is renamed to follow a naming convention:
            coursecode_project_solution

    Option 2:
        This allows the instructor to view the logging for a team in their course. 
        After selecting the course, they will see the list of teams.
        The process of viwing and downloading the logging file is just like that for the feedback file in the student menu.
    Option 3:
        Allows the instructor to add the test cases to be used by students when the code is written to be read the input from files.
        The instructor will be asked to select the course, then they will be prompted to 
        provide the path for the testcase that they want to upload.
        This will upload the testcase for all the teams in this course.
    Option 4:
        This allows the Instructor to log out of the system
