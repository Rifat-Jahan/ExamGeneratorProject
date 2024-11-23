import java.io.File;  
import java.io.FileNotFoundException;  
import java.util.Scanner; 

import java.io.FileWriter;  
import java.io.FileReader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet; 
import java.sql.Statement;

class ExamGeneratorProjectMain {   

	 public static void main(String[] args) {

		GeneratorData.GenerateExam(); // Generate the question paper randomaly using default values stored in static variables  (option 4) 
		while (true)
		{
			
			new InputsScreen(); //Show main menu from text file and input option
			if (InputsScreen.MainOption==0) 	// Exit Program
			{ 
				break;
			}
			else if (InputsScreen.MainOption==1) // Make a question paper and store in database (from stored questions pool)
			{
				while (true)
				{
					GeneratorData.TakeInputs(); //take user input for quest paper(6 inputs)
					if (GeneratorData.isTakenInputOk)
					{ break;
					}
				}
			}
			else if ((InputsScreen.MainOption==2) || (InputsScreen.MainOption==3)) // generated and saved question paper or answer sheet
			{
				// Show Exam Paper
				ExamTemplate exmTempObj = new ExamTemplate();  
				exmTempObj.DisplaySaveExamPaper();//this method we are using to display question paper nd save ques paper
			}	
			else if (InputsScreen.MainOption==4) 
			{
				GeneratorData.GenerateExam(); //  Generate the question paper randomaly using default values stored in static variables  (option 4) 
			}				
		}
    }
}

class GeneratorData  // all static user inputs and generated question+answer storing in static
{
	public static int Mcq_MaxMarks =60;     //this is our default values  with the help of these value regene do  their work randomaly 
	public static int Mcq_NumOfQuestions=3;
	
	public static int Cloze_MaxMarks =30; 
	public static int Cloze_NumOfQuestions=3; //global variable static we can use in any class 
	
	public static int Open_MaxMarks =20;
	public static int Open_NumOfQuestions=2;
	
	public static String[][] MCQ_Questions_StringAry = new String[50][2]; 
	public static String[][] Cloze_Questions_StringAry = new String[50][2];
	public static String[][] Open_Questions_StringAry = new String[50][2];

	public static boolean isTakenInputOk=true;
	
	public static String PaperOutput = "";//before saving the qu paper we are keeping in this variable
	
	public static void TakeInputs()
	{	
		 isTakenInputOk=false;		 
		 
		 try
		 {
			 Scanner myObj = new Scanner(System.in);  // Create a Scanner object
			 
			 System.out.println("---------------------------------------------------------------");
			 System.out.println("Please provides following inputs to generate the Question paper");
			 System.out.println("---------------------------------------------------------------");
			 
			 System.out.println("");
			 System.out.print("Maximum marks for MCQ ?");
			 Mcq_MaxMarks = myObj.nextInt();
			 System.out.print("Number of Questions for MCQ ?");
			 Mcq_NumOfQuestions = myObj.nextInt();
			 
			 System.out.println("");
			 System.out.print("Maximum marks for Cloze ?");
			 Cloze_MaxMarks = myObj.nextInt();
			 System.out.print("Number of Questions for Cloze ?");
			 Cloze_NumOfQuestions = myObj.nextInt();

			 System.out.println("");
			 System.out.print("Maximum marks for OpenQuestion ?");
			 Open_MaxMarks = myObj.nextInt();
			 System.out.print("Number of Questions for OpenQuestion ?");
			 Open_NumOfQuestions = myObj.nextInt();
			 System.out.println("");
			 isTakenInputOk=true;
			 
			 GeneratorData.GenerateExam(); // with input values 
		 }
		 catch (Exception e) 
		 {
			isTakenInputOk=false;
			e.printStackTrace();//with the help of this java Api we can see the error in our screen 
		 }
	}
	
	public static void GenerateExam()
	{
	
		// Collect questions from db
		ExamDataBase eObj = new ExamDataBase();
		eObj.pickRandomly_MCQ();
		eObj.pickRandomly_Cloze();
		eObj.pickRandomly_Open();
		eObj.closeExamDb();				
	
	
	}
	
}


class InputsScreen { // main menu
	public static int MainOption = -1;	

	public InputsScreen()
	{   
		while (true) 
		{
			ShowMenu();
			TakeInput();  //which option you want 
			if (validateInput()==true)  // 0..4   if it is true come out from loop 
			{ break;
			}
			
		}
	}
	
	private void ShowMenu()
	{
		try {
			File myObj = new File("templates//MainScreen.txt"); //myobj this is my file object 
			Scanner myReader = new Scanner(myObj);  //scanner obj for that file myobj
			while (myReader.hasNextLine())    //check next line data is avaiable  or not to read (while loop work until data )
			{
				String data = myReader.nextLine();  // read next line and store in data variable 
				System.out.println(data); // print the data veriable which have one line everytime 
			}
			myReader.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
		
	}
	
	private void TakeInput()
	{
		try {
			 Scanner myObj = new Scanner(System.in);  // Create a Scanner object
			 System.out.println("");
			 System.out.print("Please enter your choice? ");
			 MainOption = myObj.nextInt();
			 System.out.println("");
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}	 	 
	
	private boolean validateInput()
	{
		boolean isOk = false;
		
		if ((MainOption >=0) && (MainOption <=4))
		{
			isOk = true;
		}
		else
		{
			isOk = false;
		}
		return isOk;
	}
	
}


class ExamTemplate
{
	public void DisplaySaveExamPaper()   //method to display the question paper and to save in the file 
	{
		try {
			
			
			FileReader fileReader = new FileReader("templates//ExamTemplate.txt"); //filereader obj

			String questionPaper = ""; 

			int data = fileReader.read();  //read file next 1 character
			while(data != -1) {          //repeat loop reading char by char 
			  data = fileReader.read();// read next 1 character
			  questionPaper += (char)data;    //200 we staored in a questionpaper
			}
			
			fileReader.close();
		
			questionPaper = PutValuesInTemplate(questionPaper);// putting values in the templeate and storing to question paper
			GeneratorData.PaperOutput = questionPaper; //here we are storing question papr in static variable 

			SavePaperOutput(GeneratorData.PaperOutput); // static variable content we are saving to file 
			
			System.out.println(GeneratorData.PaperOutput);//display question paper into screen
			PressForAKey();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}

	}
	
	private String PutValuesInTemplate(String str) //method to put values in templete 
	{

			str = str.replace("{V:MCQ_MMARKS}", String.valueOf(GeneratorData.Mcq_MaxMarks));	//	"{V:MCQ_MMARKS}" replacing this text with value in the static variable	
  		    str = str.replace("{V:MCQ_QNUM}", String.valueOf(GeneratorData.Mcq_NumOfQuestions));

			str = str.replace("{V:CLOZE_MMARKS}", String.valueOf(GeneratorData.Cloze_MaxMarks));			
  		    str = str.replace("{V:CLOZE_QNUM}", String.valueOf(GeneratorData.Cloze_NumOfQuestions));

			str = str.replace("{V:OPEN_MMARKS}", String.valueOf(GeneratorData.Open_MaxMarks));			
  		    str = str.replace("{V:OPEN_QNUM}", String.valueOf(GeneratorData.Open_NumOfQuestions));			
			
			
			String McqStr = "";		
			String ClozeStr = "";		
			String OpenStr = "";		
			
			for (int i = 1; i <= GeneratorData.Mcq_NumOfQuestions; i++)//
			{
				McqStr += GeneratorData.MCQ_Questions_StringAry[i-1][0]; // with the array adding questions  in the McqStr 
				if (InputsScreen.MainOption==3)	// answer sheet	
				{
					McqStr += GeneratorData.MCQ_Questions_StringAry[i-1][1]; // answers
				}
			}
			
			for (int i = 1; i <= GeneratorData.Cloze_NumOfQuestions; i++)
			{
				ClozeStr += GeneratorData.Cloze_Questions_StringAry[i-1][0];
				if (InputsScreen.MainOption==3)	// answer sheet	
				{
					ClozeStr += GeneratorData.Cloze_Questions_StringAry[i-1][1];
				}
			}
			
			for (int i = 1; i <= GeneratorData.Open_NumOfQuestions; i++)
			{
				OpenStr += GeneratorData.Open_Questions_StringAry[i-1][0];
				if (InputsScreen.MainOption==3)	// answer sheet	
				{
					OpenStr += GeneratorData.Open_Questions_StringAry[i-1][1];
				}
			}
			

			str = str.replace("{V:MCQ_DATA}", McqStr);
			str = str.replace("{V:CLOZE_DATA}", ClozeStr);
			str = str.replace("{V:OPEN_DATA}", OpenStr);

			return str;
	}
	
	private void SavePaperOutput( String str )
	{
		try 
		{
			FileWriter fw = new FileWriter("Output//PaperOutput.txt", false); //  false for overwrite file 
			fw.write( str );//file writing happening here
			fw.close();
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		}
	}
	
	private void PressForAKey()
	{
		
		System.out.println("\nPress enter to continue...");
		try
		{
			System.in.read();
		}
		catch(Exception e)
		{	
			e.printStackTrace();
		}	
	
	}
}

class ExamDataBase {
	Connection con;
	Statement stmt;
	
	public ExamDataBase() // database open 
	{
		try
		{
			con = DriverManager.getConnection("jdbc:ucanaccess://db\\ExamDatabase.accdb");  // db\ExamDatabase.accdb  con for connection 
			stmt = con.createStatement();    
		}
		catch (Exception e)
		{
			e.printStackTrace();
			PressForAKey();
		}
	}
	
	public void pickRandomly_MCQ() // getting mcq random questios
	{
		try 
		{ 
			String howMany = String.valueOf(GeneratorData.Mcq_NumOfQuestions); //data laready stored in the mcq and we have to make string //5
			
			ResultSet rs = stmt.executeQuery("select Top " + howMany + " * FROM TBL_Stored_MCQ ORDER BY RND(ID) "); 
			// select      Top 5     *    FROM  TBL_Stored_MCQ       ORDER BY    RND(ID)
			
			
			
			int sn = 0;
			while (rs.next()) //we arw now in one recordset 
			{
				sn++;
				String question = "";	
				String answer = "";
				
				question += "\n" + sn + "). " + rs.getString("FLD_QUE_PARAGRAPH") + "\n\n" ; //after evealuter alltheses things store in question 
				question += "    " + "A) " + rs.getString("FLD_POSSIBLE_ANS_A") + "\n\n" ;
				question += "    " + "B) " + rs.getString("FLD_POSSIBLE_ANS_B") + "\n\n" ;
				question += "    " + "C) " + rs.getString("FLD_POSSIBLE_ANS_C") + "\n\n" ;
				question += "    " + "D) " + rs.getString("FLD_POSSIBLE_ANS_D") + "\n\n" ;
				
				answer += "Answer : " + rs.getString("FLD_ANSWER") + "\n\n\n" ;
				answer += "Explanation : " + rs.getString("FLD_EXPLANATION") + "\n\n\n" ;
				
				GeneratorData.MCQ_Questions_StringAry[sn-1][0] = question; // storing in static  array static  we can store everywhere 
				GeneratorData.MCQ_Questions_StringAry[sn-1][1] = answer;   // 
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			PressForAKey();
		}
	}
	
	public void pickRandomly_Cloze()
	{
		try 
		{
			String howMany = String.valueOf(GeneratorData.Cloze_NumOfQuestions);
			
			ResultSet rs = stmt.executeQuery("select Top " + howMany + " * FROM TBL_Stored_Cloze ORDER BY RND(ID) "); 
			
			int sn = 0;
			while (rs.next()) //resultset
			{
				sn++;
				String question = "";	
				String answer = "";
				
				question += "\n" + sn + "). " + rs.getString("FLD_QUE_PARAGRAPH") + "\n\n" ;
				question += "    " + "A) " + rs.getString("FLD_ANS_A") + "\n\n" ;
				question += "    " + "B) " + rs.getString("FLD_ANS_B") + "\n\n" ;
				question += "    " + "C) " + rs.getString("FLD_ANS_C") + "\n\n" ;
				question += "    " + "D) " + rs.getString("FLD_ANS_D") + "\n\n" ;   // getstring to get data  from string 
				
				answer += "Answer : " + rs.getString("FLD_ANSWER") + "\n\n\n" ;
				answer += "Explanation : " + rs.getString("FLD_EXPLANATION") + "\n\n\n" ;
				
				GeneratorData.Cloze_Questions_StringAry[sn-1][0] = question;
				GeneratorData.Cloze_Questions_StringAry[sn-1][1] = answer;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			PressForAKey();
		}
	}
	
	public void pickRandomly_Open()
	{
		try 
		{
			String howMany = String.valueOf(GeneratorData.Open_NumOfQuestions);
			ResultSet rs = stmt.executeQuery("select Top " + howMany + " * FROM TBL_Stored_OpenQue ORDER BY RND(ID) "); 
			

			int sn = 0;			
			while (rs.next())
			{
				sn++;
				String question = "";	
				String answer = "";

				question += "\n" + sn + "). " + rs.getString("FLD_QUE_PARAGRAPH") + "\n\n\n" ;
				answer += "Answer : " + rs.getString("FLD_ANS_PARAGRAPH") + "\n\n\n" ;
				
				GeneratorData.Open_Questions_StringAry[sn-1][0] = question;
				GeneratorData.Open_Questions_StringAry[sn-1][1] = answer;

			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	
	public void closeExamDb() // close database
	{
		try {
			con.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private void PressForAKey()
	{
		
		System.out.println("\nPress enter to continue...");
		try
		{
			System.in.read();  
		}
		catch(Exception e)
		{	
			e.printStackTrace();
		}	
	
	}
	
	
}
