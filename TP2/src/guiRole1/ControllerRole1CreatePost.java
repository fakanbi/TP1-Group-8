package guiRole1;

import javafx.scene.control.Alert;
import javafx.stage.Stage;
import entityClasses.User;
import entityClasses.Post;
import database.PostManager;

public class ControllerRole1CreatePost {

    private static PostManager postManager = applicationMain.FoundationsMain.database.getPostManager();

    protected static void performSubmitPost(Stage ps, User user, String content) {
        if (content.trim().isEmpty()) {
            showAlert("Empty Post", "Please enter content for your post.");
            return;
        }

        int wordCount = content.trim().split("\\s+").length;
        if (wordCount > 100) {
            showAlert("Word Limit Exceeded", "Your post exceeds 100 words. Please shorten it.");
            return;
        }

        Post newPost = new Post(postManager.getAllPosts().size() + 1, user.getUserName(), content);
        postManager.addPost(newPost);

        showAlert("Success", "Your post has been successfully created.");
        ViewRole1Home.displayRole1Home(ps, user);
    }

    private static void showAlert(String title, String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}