package guiFirstAdmin;

import java.sql.SQLException;
import database.Database;
import entityClasses.User;
import guiNewAccount.ControllerNewAccount;
import guiFirstAdmin.ModelFirstAdmin;
import Validation.UserNameRecognizer;
import javafx.stage.Stage;
import java.time.LocalDate;

public class ControllerFirstAdmin {
	/*-********************************************************************************************

	The controller attributes for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/
	
	private static String adminUsername = "";
	private static String adminPassword1 = "";
	private static String adminPassword2 = "";	
	private static String dateOfBirth = "";
	private static String phoneNumber = "";
	protected static Database theDatabase = applicationMain.FoundationsMain.database;		

	/*-********************************************************************************************

	The User Interface Actions for this page
	
	*/
	
	
	/**********
	 * <p> Method: setAdminUsername() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the username field in the
	 * View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminUsername() {
		adminUsername = ViewFirstAdmin.text_AdminUsername.getText();
	}
	
	
	/**********
	 * <p> Method: setAdminPassword1() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 1 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminPassword1() {
		adminPassword1 = ViewFirstAdmin.text_AdminPassword1.getText();
		ViewFirstAdmin.label_PasswordsDoNotMatch.setText("");
	}
	
	
	/**********
	 * <p> Method: setAdminPassword2() </p>
	 * 
	 * <p> Description: This method is called when the user adds text to the password 2 field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminPassword2() {
		adminPassword2 = ViewFirstAdmin.text_AdminPassword2.getText();		
		ViewFirstAdmin.label_PasswordsDoNotMatch.setText("");
	}
	/**********
	 * <p> Method: setAdminDOB() </p>
	 * 
	 * <p> Description: This method is called when the user adds a date to the date of birth field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminDOB() {
	    LocalDate date = ViewFirstAdmin.datePicker_DOB.getValue();
	    if (date != null) {
	    	dateOfBirth = date.toString();
	    } else {
	    	dateOfBirth = null;
	    }
	}
	/**********
	 * <p> Method: setAdminPhoneNumber() </p>
	 * 
	 * <p> Description: This method is called when the user adds a phone number to the phone number field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	protected static void setAdminPhoneNumber() {
	    phoneNumber = ViewFirstAdmin.text_PhoneNumber.getText();
	}
	
	/**********
	 * <p> Method: doSetupAdmin() </p>
	 * 
	 * <p> Description: This method is called when the user presses the button to set up the Admin
	 * account.  It start by trying to establish a new user and placing that user into the
	 * database.  If that is successful, we proceed to the UserUpdate page.</p>
	 * 
	 */
	protected static void doSetupAdmin(Stage ps, int r) {
		if(adminPassword1.isEmpty()) {
			ControllerNewAccount.alertUsernamePasswordError.setTitle("Invalid Password!");
        	ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
        	ControllerNewAccount.alertUsernamePasswordError.setContentText("Please Enter a valid password.");
        	ControllerNewAccount.alertUsernamePasswordError.showAndWait();
        	ViewFirstAdmin.text_AdminPassword1.setText("");
            return;
		}
		
		// Validating the username. To do this it reads the error message passed by the UserNameRecognizer
		// function checkForValidUserName and passes the error message as an alert to the user.
		String usernameValidationResult = UserNameRecognizer.checkForValidUserName(adminUsername);
		if(!(usernameValidationResult.isEmpty())) {
			String error[] = usernameValidationResult.split("\\*\\*\\* ERROR \\*\\*\\* ");
			ControllerNewAccount.alertUsernamePasswordError.setTitle("Invalid Username!");
			ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
			ControllerNewAccount.alertUsernamePasswordError.setContentText(error[1]);
			ControllerNewAccount.alertUsernamePasswordError.showAndWait();
            System.out.println(usernameValidationResult);
            ViewFirstAdmin.label_PasswordsDoNotMatch.setText("");
            return;
		}  
        
		// Validating the password and sending helpful errors. To do this it reads the error message passed 
		// by the Model function evaluatePassword and passes the error message as an alert to the user.
        String passwordValidationResult = ModelFirstAdmin.evaluatePassword(adminPassword1);
        if (!passwordValidationResult.isEmpty()) {
        	 String error[] = passwordValidationResult.split("; ");
             String errMessage = "";
             for(int i = 0; i < error.length; i++) {
         		if (error[i].equals("Upper case"))
         			errMessage += "• At Least one Uppercase Character \n";
        		
        		if (error[i].equals("Lower case"))
        			errMessage += "• At Least one Lowercase Character \n";
        		
        		if (error[i].equals("Numeric digits"))
        			errMessage += "• At Least one Numeric digits \n";
        			
        		if (error[i].equals("Special character"))
        			errMessage += "• At Least one Special character \n";
        			
        		if (error[i].equals("Long Enough"))
        			errMessage += "• At Least 8 charaters long \n";
        		
        		if(error[i].equals("Too Long"))
        			errMessage += "• No More than 32 Characters \n";	
             }
            errMessage += "The listed conditions were not satisfied.";
        	ControllerNewAccount.alertUsernamePasswordError.setTitle("Invalid Password!");
        	ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
        	ControllerNewAccount.alertUsernamePasswordError.setContentText(errMessage);
        	ControllerNewAccount.alertUsernamePasswordError.showAndWait();
        	ViewFirstAdmin.text_AdminPassword1.setText("");
        	ViewFirstAdmin.text_AdminPassword2.setText("");
            return;
        }
        
     // Validate date of birth
        if (dateOfBirth == null || dateOfBirth.trim().isEmpty()) {
            ControllerNewAccount.alertUsernamePasswordError.setTitle("Date of Birth Required!");
            ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
            ControllerNewAccount.alertUsernamePasswordError.setContentText("Please select your date of birth.");
            ControllerNewAccount.alertUsernamePasswordError.showAndWait();
            return;
        }

        // Uses the current date to validate the user is at least 13
        try {
            LocalDate dob = LocalDate.parse(dateOfBirth);
            LocalDate today = LocalDate.now();
            LocalDate minAgeDate = today.minusYears(13);
            
            if (dob.isAfter(minAgeDate)) {
                ControllerNewAccount.alertUsernamePasswordError.setTitle("Age Restriction!");
                ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
                ControllerNewAccount.alertUsernamePasswordError.setContentText("You must be at least 13 years old to create an account.");
                ControllerNewAccount.alertUsernamePasswordError.showAndWait();
                return;
            }
        } catch (Exception e) {
            ControllerNewAccount.alertUsernamePasswordError.setTitle("Invalid Date!");
            ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
            ControllerNewAccount.alertUsernamePasswordError.setContentText("Please select a valid date of birth.");
            ControllerNewAccount.alertUsernamePasswordError.showAndWait();
            return;
        }
        
     // Validate phone number format. It is optional field, but if it is provided it must be correct format
        if (phoneNumber != null && !phoneNumber.trim().isEmpty() && !validatePhoneNumber(phoneNumber)) {
            ControllerNewAccount.alertUsernamePasswordError.setTitle("Invalid Phone Number Format!");
            ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
            ControllerNewAccount.alertUsernamePasswordError.setContentText("Phone number must be in the format: xxx-xxx-xxxx\nExample: 123-456-7890\nOr leave the field empty.");
            ControllerNewAccount.alertUsernamePasswordError.showAndWait();
            return;
        }
        
		// Make sure the two passwords are the same
        if (adminPassword1.compareTo(adminPassword2) == 0) {
            // Create the passwords and proceed to the user home page
        	User user = new User(adminUsername, adminPassword1, "", "", "", "", "", dateOfBirth, phoneNumber, true, false, false); 
            try {
                // Create a new User object with admin role and register in the database
                theDatabase.register(user);
            }
            catch (SQLException e) {
                System.err.println("*** ERROR *** Database error trying to register a user: " + 
                        e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
            
            // User was established in the database, so navigate to the User Update Page
        	guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewFirstAdmin.theStage, user);
		}
		else {
			// The two passwords are NOT the same, so clear the passwords, explain the passwords
			// must be the same, and clear the message as soon as the first character is typed.
			ViewFirstAdmin.text_AdminPassword1.setText("");
			ViewFirstAdmin.text_AdminPassword2.setText("");
			ViewFirstAdmin.label_PasswordsDoNotMatch.setText(
					"The two passwords must match. Please try again!");
		}
	}
	
	/**********
	 * <p> Method: validatePhoneNumber() </p>
	 * 
	 * <p> Description: This method validates that the phone number is in the format xxx-xxx-xxxx
	 * where x represents a digit from 0-9. If no phone number is provided, it returns true.</p>
	 * 
	 * @param phoneNumber The phone number string to validate
	 * @return boolean true if valid format or empty, false otherwise
	 */
	protected static boolean validatePhoneNumber(String phoneNumber) {
	    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
	        return true; // Phone number is optional, so empty is valid
	    }
	    
	    // Checks the for the format for xxx-xxx-xxxx 
	    String phoneRegex = "^\\d{3}-\\d{3}-\\d{4}$";
	    return phoneNumber.matches(phoneRegex);
	}
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}	
}