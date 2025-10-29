package Validation;

import guiFirstAdmin.ModelFirstAdmin; 
/*******
 * <p> Title: PasswordEvaluationTestingAutomation Class. </p>
 * 
 * <p> Description: A Java demonstration for semi-automated tests </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2022 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00	2022-02-25 A set of semi-automated test cases
 * @version 2.00	2024-09-22 Updated for use at ASU
 * 
 */
public class PasswordEvaluationTestingAutomation {
	
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests

	/*
	 * This mainline displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Password Test Cases **************/
		
		//These are valid passwords that meet all requirements
		performTestCase(1, "TooCool1!", true);
		performTestCase(2, "GreatYes12$", true);
		performTestCase(3, "TH1s3is?", true);
		
		//These are invalid password cases
		performTestCase(4, "Mom1!", false);
		performTestCase(5, "Yes_", false);
		performTestCase(6, "IamMom1", false);
		
		/************** Confirm Password Test Cases **************/
		
		//These are Confirmed passwords that match and are valid 
		confirmPasswordTest(7,"TooCool1!", "TooCool1!", true);
		confirmPasswordTest(8, "GreatYes12$", "GreatYes12$", true);
		confirmPasswordTest(9, "Th1s3is?", "Th1s3is?", true);
		
		//These are Confirmed passwords that mismatch
		confirmPasswordTest(10, "GreatYes12$", "GreatYe12$", false);
		confirmPasswordTest(11, "Th1s3is?", "This3is?", false);
		
		//This is a Confirmed password that match but original is an invalid input
		confirmPasswordTest(12, "Mom1!", "Mom1!", false);
		
		/*************$* End of the test cases **************/
		
		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performTestCase(int testCase, String inputText, boolean expectedPass) {
				
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		String resultText= ModelFirstAdmin.evaluatePassword(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The password <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The password <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The password <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
		displayEvaluation();
	}
	
	/************** Confirm Password Testing **************/
	
	//new method added to Confirm Password
	private static void confirmPasswordTest(int testCase, String pass, String confirm, boolean expectedPass) {
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		//printing out the password and asking to confirm
		System.out.println("Password is: \"" + pass + "\" | Confirm Password: \"" + confirm + "\"");
		System.out.println("--- Testing ----");
		
		//checks if the password and confirm password match
		boolean matches = pass.equals(confirm);
		
		//if passwords match
		if(matches) {
			if(expectedPass) {
				System.out.println("***Success*** Passwords match as expected.");
				numPassed++;
			}
			
			else {
				System.out.println("***Failure*** The Passwords match but were not supposed to be valid");
				numFailed++;
			}
		}
		//if passwords do not match
		else {
			if(expectedPass) {
				System.out.println("***Failure*** Passwords are not a match but were supposed to");
				numFailed++;
			}
			else {
				System.out.println("***Success*** Passwords are not a match as expected");
				numPassed++;
			}
		}
	}
	
	private static void displayEvaluation() {
		
		if (ModelFirstAdmin.foundUpperCase)
			System.out.println("At least one upper case letter - Satisfied");
		else
			System.out.println("At least one upper case letter - Not Satisfied");

		if (ModelFirstAdmin.foundLowerCase)
			System.out.println("At least one lower case letter - Satisfied");
		else
			System.out.println("At least one lower case letter - Not Satisfied");
	

		if (ModelFirstAdmin.foundNumericDigit)
			System.out.println("At least one digit - Satisfied");
		else
			System.out.println("At least one digit - Not Satisfied");

		if (ModelFirstAdmin.foundSpecialChar)
			System.out.println("At least one special character - Satisfied");
		else
			System.out.println("At least one special character - Not Satisfied");

		if (ModelFirstAdmin.foundLongEnough)
			System.out.println("At least 8 characters - Satisfied");
		else
			System.out.println("At least 8 characters - Not Satisfied");
	}
}
