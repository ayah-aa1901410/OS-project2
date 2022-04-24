

import java.util.Scanner;

public class Upload {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		do{
			System.out.println("Menu");
			System.out.println("----------------------------------");
			System.out.println("1-Upload project");
			System.out.println("2-Recieve data about submitted project");
			System.out.println("3-Exit");
			System.out.println("Enter Your Choice: ");
			Scanner selection = new Scanner(System.in);
			
			switch(selection.nextInt()){
			case 1:
			
				
				int course;
				int team;
				String path="~/Documents/Filesystem";
				
				System.out.println("Please select the course:"
			             + "\n (1) CMPS151_SalehAlhazbi"
		             + "\t (2) CMPS251_KhaledKhan"
			             + "\t (3) CMPS350_AbdelkarimErradi"
			             + "\t (0) Exit Program");
				
				Scanner in = new Scanner(System.in);
				 course = in.nextInt();
				   
		      while(course < 0 || course > 3) {
			        	System.out.println("Please Enter a Valid Option");
			            course = in.nextInt();
		        }
				
				switch (course) {
				  case 1:
				    path+="/CMPS151_SalehAlhazbi";
				    System.out.println(path);
				    break;
			    
				  case 2:
					    path+="/CMPS251_KhaledKhan";
				    break;
				    
				  case 3:
					  path+="/CMPS350_AbdelkarimErradi";
				    break;
				    
				  case 0:
				    System.out.println("Please enter a valid option.");
				    break;
			
			}
			case 2:
			//navigate within the own folder structure
			case 3:
				System.exit(0);
			default:
				System.out.println("Wrong selection please choose from 1-3");
			}}while(true);
		
	}
}

//		
//	
//
//
////public static void chooseCourse(int course) {
////	 String path="~/Documents/Filesystem";
////	 Scanner in = new Scanner(System.in);
////	 
//////	 System.out.println("Please select the course:"
//////             + "\n (1) CMPS151_SalehAlhazbi"
//////             + "\t (2) CMPS251_KhaledKhan"
//////             + "\t (3) CMPS350_AbdelkarimErradi"
//////             + "\t (0) Exit Program");
////	 
//////	 course = in.nextInt();
////	   
////      while(course < 0 || course > 3) {
////        	System.out.println("Please Enter a Valid Option");
////            course = in.nextInt();
////        }
////	
////	switch (course) {
////	  case 1:
////	    path+="/CMPS151_SalehAlhazbi";
////	    break;
////	    
////	  case 2:
////		    path+="/CMPS251_KhaledKhan";
////	    break;
////	    
////	  case 3:
////		  path+="/CMPS350_AbdelkarimErradi";
////	    break;
////	    
////	  case 0:
////	    System.out.println("Please enter a valid option.");
////	    break;
////
////	}
////	
////	in.close();
////}
////
////
////
////public void chooseTeam(int team) {
////	
////}
//
//
//
//
//
//
//}
//	}
//}


