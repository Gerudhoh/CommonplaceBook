/**
 * Menu.java
 * 
 * Copyright 2018 Julia <gerudhoh@Bernard>
 * 
 * @author Julia Hohenadel
 * @version October 11 2018
 * 
 */

import java.util.ArrayList;
import java.util.Scanner;

import java.io.IOException;
import java.io.FileNotFoundException;


public class Menu{
	private String action; 
	private String toAddCode;
	private String toAddSemester;
	private String filename;
	private String usrDegree;
	private String fName;
	private String lName;
	private int studentNumber;
	private boolean runAgain = true;
		
	private Degree degree;
	private PlanOfStudy usrPlan; //In the end, we should only be using this, not the other usr vars
	private CourseCatalog catalog; //I should make it so that it adds a course from the catalog
	private University guelph;

/* Constants that determine which menu we go to****/
	public static final int MAIN = 1;
	public static final int START_PLAN = 2;
	public static final int CHOOSE_DEGREE = 3;
	public static final int EDIT_PLAN = 4;
	public static final int LOAD_PLAN = 5;
	public static final int COURSE_EDITOR = 6;
/* ************************************************/	
	
	public Menu(){
			guelph = new University();
			usrPlan = new PlanOfStudy();
			catalog = new CourseCatalog();
	}
	
	/**
	 * This function runs the menues
	 */
	public void runMenu() throws FileNotFoundException, ClassNotFoundException, IOException{
		int stop = 0;
		int menu = 1, prevMenu = 1, back = 1;
		boolean error = false;
		Scanner sc = new Scanner(System.in);
		Integer selection = 0;
		ArrayList<Integer> subMenu = new ArrayList<>();
		subMenu.add(1);
		subMenu.add(2);
		subMenu.add(3);
		
		try{
			guelph.readUniversity();
			catalog = guelph.getCourseCatalog();
			usrPlan.setCatalog(catalog);
		}
		catch(Exception e){
			System.out.println(e);
		}
		
		while(stop == 0){
			while(!subMenu.contains(selection)){
				printMenu(error, menu);
				error = false;
				
				try{
					selection = sc.nextInt();
				}
				catch(Exception e){
					selection = 1;
					stop = 1;
					runAgain = true;
					break;
				}
				
				prevMenu = menu;
				//Determines what "Go back" means
				if(prevMenu < 4){
					back = prevMenu - 1;
				}
				else if(prevMenu == 4){
					back = 1;
				}
				else{
					back = 4;
				}
			
				menu = processInput(selection, menu);
				selection = 0;
				
				if(menu == -1){
					menu = prevMenu;
					error = true;
				}
				else if(menu == 42){
					menu = back;
				}
				else if(menu == 99){
					selection = 1;
					stop = 1;
				}
			}
		}
		
	}
	/**
	 * This function processes the user input, based on which menu they're in
	 * @param selection: Which option they chose
	 * @param menu: Which menu we're printing*/ 
	private int processInput(int selection, int menu) throws FileNotFoundException, ClassNotFoundException, IOException{
		Scanner sc = new Scanner(System.in);
		String toAddOther;
		String confirm;
		int input;
		Course checker;

		if(menu == MAIN){
			if(selection == 1){
				return START_PLAN;
			}
			else if(selection == 2){
				return EDIT_PLAN;
			}
			else if(selection == 99){
				return 99;
			}
			else{
				return -1;
			}
		}
		else if(menu == START_PLAN){
			
			if(selection == 1){ //Read in a file
				try{
					guelph.readUniversity();
					catalog = guelph.getCourseCatalog();
				}
				catch(Exception e){
					System.out.println(e);
				}
				return LOAD_PLAN;
			}
			else if(selection == 2){ //Open choose degree menu
				System.out.println("Choose a degree");
				return CHOOSE_DEGREE;
			}
			else if(selection == 3){ //Choose a major
				
				if(usrDegree != null){
					if(usrDegree.equals("Honours Degree")){
						
						System.out.print("\nChoose a Major:\n 1 = CS\n 2 = SEng\n Select[1-2]: ");
						input = sc.nextInt();
						
						if(input == 1){
							usrPlan.setDegreeProgram(guelph.getADegreePrg("CS"));
							action = "Major set to CS";
						}
						else if(input == 2){
							usrPlan.setDegreeProgram(guelph.getADegreePrg("SEng"));
							action = "Major set to SEng";
						}
						else{
							return -1;
						}
					}
					else if(usrDegree.equals("General Degree")){
						usrPlan.setDegreeProgram(guelph.getADegreePrg("BCG"));
						action = "Major set to BCG";
					}
					else
						return -1;
					degree = usrPlan.getDegreeProgram();
				}
				else{
					action = "No degree selected, unable to continue";
					return -1;
				}
				return START_PLAN;
			}
			else if(selection == 4){ //Input name
				scanNames();
				usrPlan.setStudentInfo(new Student(fName, lName), 0);
				
				action = ("\n Name was set to " + usrPlan.getStudentName());
				return START_PLAN;
			}
			else if(selection == 5){ //Input student number
				System.out.println("Please Input your Student Number");
				studentNumber = (sc.nextInt());
				
				usrPlan.setStudentInfo(new Student(fName, lName), studentNumber);
				
				action = ("\n Student Number was set to " + studentNumber);
				return START_PLAN;
			}
			else if(selection == 42){
				action = "\n Returned to main menu";
				return 42;
			}
			else if(selection == 99){
				return 99;
			}
			else{
				return -1;
			}
		}
		else if(menu == CHOOSE_DEGREE){
			
			if(selection == 1){ //Set degree to honours
				action = "\n Degree was set to honours degree";
				usrDegree = "Honours Degree";
				return CHOOSE_DEGREE;
			}
			else if(selection == 2){ //Set degree to general
				action = "\n Degree was set to general degree";
				usrDegree = "General Degree";
				return CHOOSE_DEGREE;
			}
			else if(selection == 42){
				action = "\n Returned to Start a Plan menu";
				return 42;
			}
			else if(selection == 99){
				return 99;
			}
			else{
				return -1;
			}
		}
		else if(menu == EDIT_PLAN){
			if(selection == 1){ //Load up a plan
			
				return LOAD_PLAN;
			}
			else if(selection == 2){ //Edit a course
				return COURSE_EDITOR;
			}
			else if(selection == 3){ //Save state
				usrPlan.saveState();
				action = "Writing to file POSSave";
				return EDIT_PLAN;
			}
			else if(selection == 4){ //View prereqs of reqs
				System.out.print("Please Input the course code: ");
				toAddCode = (sc.nextLine());
				
				checker = catalog.findCourse(toAddCode);
				if(checker.getPrerequisites().size() > 0 ){
					action = checker.toString();
				}
				else{
					action = checker.getCourseCode() + " has no prerequisites";
				}
				return EDIT_PLAN;
			}
			else if(selection == 5){//view reqs you're missing
				if(degree!=null){
					ArrayList<Course> temp = degree.remainingRequiredCourses(usrPlan);
					action = temp.toString();
				}
				else{
					action = "Your degree must be set before this can be calculated";
				}
				return EDIT_PLAN;
			}
			else if(selection == 6){ //view how many credits you've earned
				if(degree !=null){
					action = "You've earned " + ( degree.getTotalCredits() - degree.numberOfCreditsRemaining(usrPlan))  + " credits";
				}
				else{
					action = "Your degree must be set before this can be calculated";
				}
				return EDIT_PLAN;
			}
			else if(selection == 7){ //view how many credits you have left to earn
				if(degree !=null){
					action = "You're missing " + (degree.numberOfCreditsRemaining(usrPlan)) + " credits";
				}
				else{
					action = "Your degree must be set before this can be calculated";
				}
				return EDIT_PLAN;
			}
			else if(selection == 8){ //See if you can graduate
				if(degree.meetsRequirements(usrPlan) == false){
					action = "You haven't met the reqs champ";
				}
				else{
					action = "You are on the right track!";
				}
				return EDIT_PLAN;
			}
			else if(selection == 9){
				action = usrPlan.toString();
				return EDIT_PLAN;
			}
			else if(selection == 42){
				action = "\n Returned to main menu";
				return 42;
			}
			else if(selection == 99){
				return 99;
			}
			else{
				return -1;
			}
		}
		else if(menu == LOAD_PLAN){
			if(selection == 1){
				try{
					guelph.readUniversity();
					catalog = guelph.getCourseCatalog();
				}
				catch(Exception e){
					System.out.println(e);
				}
				System.out.print("Please Input the file name: ");
				filename = (sc.nextLine());
				usrPlan.importData(filename);
				
				return LOAD_PLAN;
			}
			else if(selection == 2){
				try{
					guelph.readUniversity();
					catalog = guelph.getCourseCatalog();
					usrPlan.setCatalog(catalog);
				}
				catch(Exception e){
					System.out.println(e);
				}
				System.out.print("Please Input the file name: ");
				filename = (sc.nextLine());
				usrPlan.loadOldPOS(filename);
				usrDegree = usrPlan.getDegreeProgram().getDegreeTitle();
				degree = usrPlan.getDegreeProgram();
				System.out.println(usrPlan);
				return LOAD_PLAN;
			}
			else if(selection == 42){
				action = "\n Returned to Edit a Plan menu";
				return 42;
			}
			else if(selection == 99){
				return 99;
			}
			else{
				return -1;
			}
		}
		else if(menu == COURSE_EDITOR){
			if(selection == 1){ //Add a course
				Scanner sc = new Scanner(System.in);
		
                System.out.print("Please Input the course code: ");
                toAddCode = (sc.nextLine());
                        
                System.out.print("Please Input the semester this will be taken: ");
                toAddSemester = (sc.nextLine());
				
				if(catalog.findCourse((toAddCode)) != null){
					usrPlan.addCourse(toAddCode, toAddSemester);
				}
				else{
					return -1;
				}
				
				checker = usrPlan.getCourse(toAddCode, toAddSemester); //This is a tester dummy I should ditch
				
				action = "\n Course \"" + checker.getCourseTitle() + "\" added";
				return COURSE_EDITOR;
			}
			else if(selection == 2){ //Update Status
				Scanner sc = new Scanner(System.in);
		
                System.out.print("Please Input the course code: ");
                toAddCode = (sc.nextLine());
                        
                System.out.print("Please Input the semester this will be taken: ");
                toAddSemester = (sc.nextLine());
				
				System.out.println("Please Input the course Status:");
				System.out.println("1. Planned");
				System.out.println("2. In-Progress");
				System.out.println("3. Completed");
				toAddOther = (sc.nextLine());
				toAddOther = toAddOther.trim();