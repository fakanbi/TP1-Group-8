package guiRole2;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import guiRole1.ControllerRole1Home;
import guiUserUpdate.ViewUserUpdate;
import javafx.scene.control.ListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import entityClasses.Post;
import entityClasses.Reply;
import database.PostManager;
import database.ReplyManager;
import javafx.scene.control.TextField;
import java.util.List;

/*******
 * <p> Title: ViewRole2Home Class. </p>
 * 
 * <p> Description: The Java/FX-based Role2 Home Page.  The page is a stub for some role needed for
 * the application.  The widgets on this page are likely the minimum number and kind for other role
 * pages that may be needed.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-04-20 Initial version
 *  
 */

public class ViewRole2Home {
	
	/*-*******************************************************************************************

	Attributes
	
	 */
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;


	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	// GUI Area 1: It informs the user about the purpose of this page, whose account is being used,
	// and a button to allow this user to update the account settings
	protected static Label label_PageTitle = new Label();
	protected static Label label_UserDetails = new Label();
	protected static Button button_UpdateThisUser = new Button("Account Update");
		
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator1 = new Line(20, 95, width-20, 95);

	// GUI ARea 2: This is a stub, so there are no widgets here.  For an actual role page, this are
	// would contain the widgets needed for the user to play the assigned role.
	protected static ListView<String> list_Posts = new ListView<>();
	protected static ObservableList<String> observablePosts = FXCollections.observableArrayList();
	private static javafx.scene.control.TextField text_Search = new javafx.scene.control.TextField();
	private static javafx.scene.control.Button button_Search = new javafx.scene.control.Button("Search");
	
	// This is a separator and it is used to partition the GUI for various tasks
	protected static Line line_Separator4 = new Line(20, 525, width-20,525);
	
	// GUI Area 3: This is last of the GUI areas.  It is used for quitting the application and for
	// logging out.
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");

	// This is the end of the GUI objects for the page.
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewRole2Home theView;		// Used to determine if instantiation of the class
												// is needed

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static Stage theStage;			// The Stage that JavaFX has established for us	
	protected static Pane theRootPane;			// The Pane that holds all the GUI widgets
	protected static User theUser;				// The current logged in User
	
	private static Scene theRole2HomeScene;		// The shared Scene each invocation populates
	protected static final int theRole = 3;		// Admin: 1; Role1: 2; Role2: 3

	/*-*******************************************************************************************

	Constructors
	
	 */

	/**********
	 * <p> Method: displayRole2Home(Stage ps, User user) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the Role2 Home page to be displayed.
	 * 
	 * It first sets up every shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User for this GUI and it's methods
	 * 
	 */
	public static void displayRole2Home(Stage ps, User user) {
		
		// Establish the references to the GUI and the current user
		theStage = ps;
		theUser = user;
		
		// If not yet established, populate the static aspects of the GUI
		if (theView == null) theView = new ViewRole2Home();		// Instantiate singleton if needed
		
		// Populate the dynamic aspects of the GUI with the data from the user and the current
		// state of the system.
		theDatabase.getUserAccountDetails(user.getUserName());
		applicationMain.FoundationsMain.activeHomePage = theRole;
		
		label_UserDetails.setText("User: " + theUser.getUserName());// Set the username
		
		refreshPostList();

		// Set the title for the window, display the page, and wait for the Admin to do something
		theStage.setTitle("CSE 360 Foundations: Role2 Home Page");
		theStage.setScene(theRole2HomeScene);						// Set this page onto the stage
		theStage.show();											// Display it to the user
	}
	
	/**********
	 * <p> Method: ViewRole2Home() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object. </p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayRole2Home method.</p>
	 * 
	 */
	private ViewRole2Home() {
		
		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theRole2HomeScene = new Scene(theRootPane, width, height);	// Create the scene
		
		// Set the title for the window
		
		// Populate the window with the title and other common widgets and set their static state
		
		// GUI Area 1
		label_PageTitle.setText("Role2 Home Page");
		setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

		label_UserDetails.setText("User: " + theUser.getUserName());
		setupLabelUI(label_UserDetails, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
		
		setupButtonUI(button_UpdateThisUser, "Dialog", 18, 170, Pos.CENTER, 610, 45);
		button_UpdateThisUser.setOnAction((event) -> {ControllerRole2Home.performUpdate(); });
		
		// GUI Area 2
		Label label_Discussion = new Label("Create a Discussion Post:");
		setupLabelUI(label_Discussion, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 120);

		Button button_CreatePost = new Button("New Post");
		setupButtonUI(button_CreatePost, "Dialog", 18, 200, Pos.CENTER, 20, 160);
		
		list_Posts.setItems(observablePosts);
		list_Posts.setLayoutX(300);
		list_Posts.setLayoutY(260);
		list_Posts.setPrefSize(640, 240);
		list_Posts.setPrefWidth(500);
		list_Posts.setPrefHeight(300);
		
		button_CreatePost.setOnAction((event) -> {
		    ControllerRole2Home.performCreatePost(theStage, theUser);
		});
		
		text_Search.setPromptText("Enter keyword...");
		text_Search.setLayoutX(250);
		text_Search.setLayoutY(160);
		text_Search.setPrefWidth(200);

		setupButtonUI(button_Search, "Dialog", 16, 100, Pos.CENTER, 470, 160);
		button_Search.setOnAction(e -> {
		    String keyword = text_Search.getText();
		    refreshPostList(PostManager.getInstance().searchPosts(keyword));
		});
		
		// Search area
		Label label_Search = new Label("Search Posts:");
		setupLabelUI(label_Search, "Arial", 18, width, Pos.BASELINE_LEFT, 400, 120);

		TextField text_SearchField = new TextField();
		text_SearchField.setPromptText("Enter keyword...");
		
		label_Search.setLayoutX(20);
		label_Search.setLayoutY(270);
		list_Posts.setPrefSize(740, 220);

		text_SearchField.setLayoutX(160);
		text_SearchField.setLayoutY(215);
		text_SearchField.setPrefWidth(200);
		
		Button button_Search = new Button("Search");
		setupButtonUI(button_Search, "Dialog", 16, 100, Pos.CENTER, 380, 215);
		
		Button button_ClearSearch = new Button("Clear");
		setupButtonUI(button_ClearSearch, "Dialog", 16, 100, Pos.CENTER, 490, 215);
		
		button_Search.setOnAction(e -> {
		    String keyword = text_SearchField.getText().trim();
		    ControllerRole2Home.performSearch(keyword);
		});

		button_ClearSearch.setOnAction(e -> {
		    text_SearchField.clear();
		    ControllerRole2Home.performSearch(""); // show all posts again
		});
		
		Button button_DeletePost = new Button("Delete Post");
		setupButtonUI(button_DeletePost, "Dialog", 16, 150, Pos.CENTER, 20, 500);

		button_DeletePost.setOnAction(e -> {
		    ControllerRole2Home.performDeletePost(theUser);
		});
		
		Button button_EditPost = new Button("Edit Post");
		setupButtonUI(button_EditPost, "Dialog", 16, 150, Pos.CENTER, 190, 500);

		button_EditPost.setOnAction(e -> {
		    ControllerRole2Home.performEditPost(theUser);
		});
		
		Button button_Reply = new Button("Reply");
		setupButtonUI(button_Reply, "Dialog", 16, 150, Pos.CENTER, 370, 500);
		button_Reply.setOnAction(e -> {
		    ControllerRole2Home.performReply(theUser);
		});

		// GUI Area 3
        setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 20, 540);
        button_Logout.setOnAction((event) -> {ControllerRole2Home.performLogout(); });
        
        setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 300, 540);
        button_Quit.setOnAction((event) -> {ControllerRole2Home.performQuit(); });

		// This is the end of the GUI initialization code
		
		// Place all of the widget items into the Root Pane's list of children
        theRootPane.getChildren().addAll(
    			label_PageTitle, label_UserDetails, button_UpdateThisUser, line_Separator1, label_Discussion, button_CreatePost, label_Search, text_SearchField, button_Search, button_ClearSearch, list_Posts,
    	        line_Separator4, button_Reply, button_EditPost, button_DeletePost, button_Logout, button_Quit);
	}
	
	
	/*-********************************************************************************************

	Helper methods to reduce code length

	 */
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, 
			double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, 
			double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
	
	// Refresh with all posts
		public static void refreshPostList() {
		    PostManager postManager = PostManager.getInstance();
		    refreshPostList(postManager.getAllPosts());
		}

		// Refresh with filtered posts (search results)
		public static void refreshPostList(List<Post> posts) {
		    observablePosts.clear();
		    ReplyManager replyManager = ReplyManager.getInstance();

		    if (posts == null || posts.isEmpty()) {
		        observablePosts.add("No matching posts found.");
		    } else {
		        for (Post p : posts) {
		            String postContent;

		            // If the post is deleted, show placeholder text
		            if (p.getContent() == null || p.getContent().trim().isEmpty()) {
		                postContent = "[This post has been deleted]";
		            } else {
		                postContent = p.getContent();
		            }

		            // Add the post to the list
		            observablePosts.add("Post #" + p.getPostId() + " by " + p.getAuthor() + ":\n" + postContent);

		            // Show replies even if the post was deleted
		            List<Reply> replies = replyManager.getRepliesForPost(p.getPostId());
		            for (Reply r : replies) {
		                observablePosts.add("   ↳ " + r.getAuthor() + ": " + r.getContent());
		            }
		        }
		    }

		    list_Posts.setItems(observablePosts);
		}
}
