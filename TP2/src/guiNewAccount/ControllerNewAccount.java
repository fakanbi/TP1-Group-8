package guiNewAccount;

import java.sql.SQLException;

import database.Database;
import entityClasses.User;
import guiFirstAdmin.ViewFirstAdmin;
import guiNewAccount.ModelNewAccount;
import guiFirstAdmin.ControllerFirstAdmin;
import Validation.UserNameRecognizer;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import java.time.LocalDate;

public class ControllerNewAccount {
	
	/*-********************************************************************************************

	The User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/


	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	public static Alert alertUsernamePasswordError = new Alert(AlertType.INFORMATION);
	/**********
	 * <p> Method: public doCreateUser() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the User Setup
	 * button.  This method checks the input fields to see that they are valid.  If so, it then
	 * creates the account by adding information to the database.
	 * 
	 * The method reaches batch to the view page and to fetch the information needed rather than
	 * passing that information as parameters.
	 * 
	 */	
	
	// Fetch the username and password. (We use the first of the two here, but we will validate
	// that the two password fields are the same before we do anything with it.)
	protected static String username = ViewNewAccount.text_Username.getText();
	protected static String password = ViewNewAccount.text_Password1.getText();
	protected static String phoneNumber = ViewNewAccount.text_PhoneNumber.getText();
	protected static String dateOfBirth = ""; 
    
    
	protected static void doCreateUser() {

		// Checking if password is empty and passing an error if it is.
		if(password.isEmpty()) {
			ControllerNewAccount.alertUsernamePasswordError.setTitle("Invalid Password!");
        	ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
        	ControllerNewAccount.alertUsernamePasswordError.setContentText("Please Enter a valid password.");
        	ControllerNewAccount.alertUsernamePasswordError.showAndWait();
            ViewNewAccount.text_Password1.setText("");
            return;
		}
		
		// Validating the username. To do this it reads the error message passed by the UserNameRecognizer
		// function checkForValidUserName and passes the error message as an alert to the user.
		String usernameValidationResult = UserNameRecognizer.checkForValidUserName(username);
		if(!(usernameValidationResult.isEmpty())) {
			String error[] = usernameValidationResult.split("\\*\\*\\* ERROR \\*\\*\\* ");
			ControllerNewAccount.alertUsernamePasswordError.setTitle("Invalid Username!");
			ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
			ControllerNewAccount.alertUsernamePasswordError.setContentText(error[1]);
			ControllerNewAccount.alertUsernamePasswordError.showAndWait();
            System.out.println(usernameValidationResult);
            ViewNewAccount.text_Username.setText("");
            return;
		}  
		
        if (phoneNumber != null && !phoneNumber.trim().isEmpty() && !validatePhoneNumber(phoneNumber)) {
            ControllerNewAccount.alertUsernamePasswordError.setTitle("Invalid Phone Number Format!");
            ControllerNewAccount.alertUsernamePasswordError.setHeaderText(null);
            ControllerNewAccount.alertUsernamePasswordError.setContentText("Phone number must be in the format: xxx-xxx-xxxx\nExample: 123-456-7890\nOr leave the field empty.");
            ControllerNewAccount.alertUsernamePasswordError.showAndWait();
            return;
        }
        
        // Validating the password and sending helpful errors. To do this it reads the error message passed 
		// by the Model function evaluatePassword and passes the error message as an alert to the user.
        String passwordValidationResult = ModelNewAccount.evaluatePassword(password);
        if (!passwordValidationResult.isEmpty()) {
        	 String error[] = passwordValidationResult.split("; ");
             String errMessage = "";
        // Making the error into a readable alert message for the user.
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
            ViewNewAccount.text_Password1.setText("");
            ViewNewAccount.text_Password2.setText("");
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

        // Convert to LocalDate and validate age
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
		
		// Display key information to the log
		System.out.println("** Account for Username: " + username + "; theInvitationCode: "+
				ViewNewAccount.theInvitationCode + "; email address: " + 
				ViewNewAccount.emailAddress + "; Role: " + ViewNewAccount.theRole);
		
		// Initialize local variables that will be created during this process
		int roleCode = 0;
		User user = null;

		// Make sure the two passwords are the same.	
		if (ViewNewAccount.text_Password1.getText().
				compareTo(ViewNewAccount.text_Password2.getText()) == 0) {
			
			if (theDatabase.doesUserExist(username)) {
		        Alert usernameExistsAlert = new Alert(AlertType.ERROR);
		        usernameExistsAlert.setTitle("Username Already Exists");
		        usernameExistsAlert.setHeaderText(null);
		        usernameExistsAlert.setContentText("The username '" + username + "' is already taken. Please choose a different username.");
		        usernameExistsAlert.showAndWait();
		        ViewNewAccount.text_Username.setText(""); // Clear the username field
		        return; // Stop the registration process
		    }
			
			// The passwords match so we will set up the role and the User object base on the 
			// information provided in the invitation
			 if (ViewNewAccount.theRole.compareTo("Admin") == 0) {
		            roleCode = 1;
		            user = new User(username, password, "", "", "", "", "", dateOfBirth, phoneNumber, true, false, false);
		        } else if (ViewNewAccount.theRole.compareTo("Role1") == 0) {
		            roleCode = 2;
		            user = new User(username, password, "", "", "", "", "", dateOfBirth, phoneNumber, false, true, false);
		        } else if (ViewNewAccount.theRole.compareTo("Role2") == 0) {
		            roleCode = 3;
		            user = new User(username, password, "", "", "", "", "", dateOfBirth, phoneNumber, false, false, true);
		        } else {
		            System.out.println(
		                    "**** Trying to create a New Account for a role that does not exist!");
		            System.exit(0);
		        }
			
			// Unlike the FirstAdmin, we know the email address, so set that into the user as well.
        	user.setEmailAddress(ViewNewAccount.emailAddress);

        	// Inform the system about which role will be played
			applicationMain.FoundationsMain.activeHomePage = roleCode;
			
        	// Create the account based on user and proceed to the user account update page
            try {
            	// Create a new User object with the pre-set role and register in the database
            	theDatabase.register(user);
            } catch (SQLException e) {
                System.err.println("*** ERROR *** Database error: " + e.getMessage());
                e.printStackTrace();
                System.exit(0);
            }
            
            // The account has been set, so remove the invitation from the system
            theDatabase.removeInvitationAfterUse(
            		ViewNewAccount.text_Invitation.getText());
            
            // Set the database so it has this user and the current user
            theDatabase.getUserAccountDetails(username);

            // Navigate to the Welcome Login Page
            guiUserUpdate.ViewUserUpdate.displayUserUpdate(ViewNewAccount.theStage, user);
		}
		else {
			// The two passwords are NOT the same, so clear the passwords, explain the passwords
			// must be the same, and clear the message as soon as the first character is typed.
			ViewNewAccount.text_Password1.setText("");
			ViewNewAccount.text_Password2.setText("");
			ViewNewAccount.alertUsernamePasswordError.showAndWait();
		}
	}

	/**********
	 * <p> Method: setAdminDOB() </p>
	 * 
	 * <p> Description: This method is called when the user adds a date to the date of birth field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	// Sets the admins date of birth
	protected static void setAdminDOB() {
	    LocalDate date = ViewNewAccount.text_DateOfBirth.getValue();
	    if (date != null) {
	    	dateOfBirth = date.toString();
	    }
	    else {
	    	dateOfBirth = "";
	    }
	}
	/**********
	 * <p> Method: setAdminPhoneNumber() </p>
	 * 
	 * <p> Description: This method is called when the user adds a phone number to the phone number field in
	 * the View.  A private local copy of what was last entered is kept here.</p>
	 * 
	 */
	// sets the admin phone number
	protected static void setAdminPhoneNumber() {
	    phoneNumber = ViewNewAccount.text_PhoneNumber.getText();
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
	// Method to validate the phone number
	protected static boolean validatePhoneNumber(String phoneNumber) {
	    if (phoneNumber == null || phoneNumber.trim().isEmpty()) {
	        return true; // Phone number is optional, so empty is valid
	    }
	    
	    // Regular expression for xxx-xxx-xxxx format
	    String phoneRegex = "^\\d{3}-\\d{3}-\\d{4}$";
	    return phoneNumber.matches(phoneRegex);
	}
	
	/**********
	 * <p> Method: public performQuit() </p>
	 * 
	 * <p> Description: This method is called when the user has clicked on the Quit button.  Doing
	 * this terminates the execution of the application.  All important data must be stored in the
	 * database, so there is no cleanup required.  (This is important so we can minimize the impact
	 * of crashed.)
	 * 
	 */	
	protected static void performQuit() {
		System.out.println("Perform Quit");
		System.exit(0);
	}	
}
