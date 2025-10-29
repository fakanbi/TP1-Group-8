package guiRole1;

import database.PostManager;
import guiRole1.ViewRole1Home;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import java.util.Optional;
import entityClasses.Post;
import entityClasses.User;
import javafx.scene.control.TextInputDialog;
import entityClasses.Reply;
import database.ReplyManager;

public class ControllerRole1Home {

	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	
 	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewRole1Home.theStage);
	}
	
	/**********
	 * <p> Method: performCreatePost() </p>
	 *
	 * <p> Description: This method handles the request from the Role1 Home page
	 * to create a new discussion post. It navigates the user to the post creation view. </p>
	 */
	protected static void performCreatePost(javafx.stage.Stage ps, entityClasses.User user) {
	    guiRole1.ViewRole1CreatePost.displayCreatePost(ps, user);
	    
	    ViewRole1Home.refreshPostList(database.PostManager.getInstance().getAllPosts());

	}
	
	protected static void performSearch(String keyword) {
	    PostManager postManager = PostManager.getInstance();
	    var filteredPosts = postManager.searchPosts(keyword);
	    ViewRole1Home.refreshPostList(filteredPosts);
	}
	
	protected static void performDeletePost(User currentUser) {
	    PostManager postManager = PostManager.getInstance();
	    int selectedIndex = ViewRole1Home.list_Posts.getSelectionModel().getSelectedIndex();

	    if (selectedIndex < 0) {
	        showInfo("No Post Selected", "Please select a post to delete.");
	        return;
	    }

	    // Get the selected post text and find corresponding Post object
	    String selectedText = ViewRole1Home.list_Posts.getSelectionModel().getSelectedItem();
	    Post selectedPost = postManager.getAllPosts().stream()
	            .filter(p -> selectedText.contains("Post #" + p.getPostId()))
	            .findFirst()
	            .orElse(null);

	    if (selectedPost == null) {
	        showInfo("Error", "Could not find the selected post.");
	        return;
	    }

	    // Restrict delete to user's own posts
	    if (!selectedPost.getAuthor().equals(currentUser.getUserName())) {
	        showInfo("Access Denied", "You can only delete your own posts.");
	        return;
	    }

	    // Confirm deletion
	    Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
	    confirm.setTitle("Confirm Deletion");
	    confirm.setHeaderText("Are you sure you want to delete this post?");
	    confirm.setContentText("This action cannot be undone.");
	    Optional<ButtonType> result = confirm.showAndWait();

	    if (result.isPresent() && result.get() == ButtonType.OK) {
	        // Mark post as deleted instead of removing it
	        selectedPost.setContent(null); // Keeps replies visible

	        showInfo("Post Deleted", "Your post has been deleted.");
	        ViewRole1Home.refreshPostList();
	    }
	}
	
	// Helper method for showing simple alerts
	private static void showInfo(String title, String message) {
	    Alert alert = new Alert(AlertType.INFORMATION);
	    alert.setTitle(title);
	    alert.setHeaderText(null);
	    alert.setContentText(message);
	    alert.showAndWait();
	}
	
	protected static void performEditPost(User currentUser) {
	    PostManager postManager = PostManager.getInstance();
	    int selectedIndex = ViewRole1Home.list_Posts.getSelectionModel().getSelectedIndex();

	    if (selectedIndex < 0) {
	        showInfo("No Post Selected", "Please select a post to edit.");
	        return;
	    }

	    // Get selected post
	    String selectedText = ViewRole1Home.list_Posts.getSelectionModel().getSelectedItem();
	    Post selectedPost = postManager.getAllPosts().stream()
	            .filter(p -> selectedText.contains("Post #" + p.getPostId()))
	            .findFirst()
	            .orElse(null);

	    if (selectedPost == null) {
	        showInfo("Error", "Could not find the selected post.");
	        return;
	    }

	    // Restrict editing to author
	    if (!selectedPost.getAuthor().equals(currentUser.getUserName())) {
	        showInfo("Access Denied", "You can only edit your own posts.");
	        return;
	    }

	    // Open input dialog to edit content
	    TextInputDialog dialog = new TextInputDialog(selectedPost.getContent());
	    dialog.setTitle("Edit Post");
	    dialog.setHeaderText("Editing Post #" + selectedPost.getPostId());
	    dialog.setContentText("Enter your updated post (max 100 words):");

	    Optional<String> result = dialog.showAndWait();
	    result.ifPresent(newContent -> {
	        int wordCount = newContent.trim().split("\\s+").length;
	        if (wordCount > 100) {
	            showInfo("Too Long", "Posts cannot exceed 100 words.");
	        } else {
	            selectedPost.setContent(newContent.trim());
	            showInfo("Success", "Your post has been updated.");
	            ViewRole1Home.refreshPostList();
	        }
	    });
	}
	
	protected static void performReply(User currentUser) {
	    PostManager postManager = PostManager.getInstance();
	    ReplyManager replyManager = ReplyManager.getInstance();

	    int selectedIndex = ViewRole1Home.list_Posts.getSelectionModel().getSelectedIndex();
	    if (selectedIndex < 0) {
	        showInfo("No Post Selected", "Please select a post to reply to.");
	        return;
	    }

	    // Find the selected post
	    String selectedText = ViewRole1Home.list_Posts.getSelectionModel().getSelectedItem();
	    Post selectedPost = postManager.getAllPosts().stream()
	            .filter(p -> selectedText.contains("Post #" + p.getPostId()))
	            .findFirst()
	            .orElse(null);

	    if (selectedPost == null) {
	        showInfo("Error", "Could not find the selected post.");
	        return;
	    }

	    // Prompt user for reply
	    TextInputDialog dialog = new TextInputDialog();
	    dialog.setTitle("Reply to Post");
	    dialog.setHeaderText("Replying to Post #" + selectedPost.getPostId());
	    dialog.setContentText("Enter your reply (max 100 words):");

	    dialog.showAndWait().ifPresent(replyContent -> {
	        if (replyContent.trim().isEmpty()) {
	            showInfo("Empty Reply", "Your reply cannot be empty.");
	            return;
	        }

	        int wordCount = replyContent.trim().split("\\s+").length;
	        if (wordCount > 100) {
	            showInfo("Too Long", "Replies cannot exceed 100 words.");
	            return;
	        }

	        int newReplyId = replyManager.getAllReplies().size() + 1;
	        Reply newReply = new Reply(newReplyId, selectedPost.getPostId(),
	                currentUser.getUserName(), replyContent.trim());
	        replyManager.addReply(newReply);

	        showInfo("Reply Posted", "Your reply was successfully added.");
	        ViewRole1Home.refreshPostList(); // refreshes list to include new replies
	    });
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
		System.exit(0);
	}
}
