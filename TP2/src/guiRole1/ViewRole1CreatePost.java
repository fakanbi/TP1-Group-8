package guiRole1;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;

public class ViewRole1CreatePost {

    protected static Stage theStage;
    protected static User theUser;
    private static Scene theCreatePostScene;

    public static void displayCreatePost(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        Pane root = new Pane();
        theCreatePostScene = new Scene(root, applicationMain.FoundationsMain.WINDOW_WIDTH, applicationMain.FoundationsMain.WINDOW_HEIGHT);

        Label label_Title = new Label("Create a New Discussion Post");
        label_Title.setFont(Font.font("Arial", 24));
        label_Title.setMinWidth(applicationMain.FoundationsMain.WINDOW_WIDTH);
        label_Title.setAlignment(Pos.CENTER);
        label_Title.setLayoutY(20);

        Label label_Instructions = new Label("Enter your post (max 100 words):");
        label_Instructions.setFont(Font.font("Arial", 16));
        label_Instructions.setLayoutX(20);
        label_Instructions.setLayoutY(80);

        TextArea textArea_Post = new TextArea();
        textArea_Post.setWrapText(true);
        textArea_Post.setLayoutX(20);
        textArea_Post.setLayoutY(110);
        textArea_Post.setPrefSize(720, 300);

        Label label_WordCount = new Label("0 / 100 words");
        label_WordCount.setFont(Font.font("Arial", 14));
        label_WordCount.setLayoutX(20);
        label_WordCount.setLayoutY(420);

        textArea_Post.textProperty().addListener((obs, oldVal, newVal) -> {
            String[] words = newVal.trim().split("\\s+");
            int count = newVal.trim().isEmpty() ? 0 : words.length;
            label_WordCount.setText(count + " / 100 words");
            if (count > 100) {
                textArea_Post.setText(oldVal);
                textArea_Post.positionCaret(oldVal.length());
                label_WordCount.setText("100 / 100 words (limit reached)");
            }
        });

        Button button_Submit = new Button("Submit Post");
        button_Submit.setLayoutX(20);
        button_Submit.setLayoutY(460);
        button_Submit.setOnAction(e -> {
            ControllerRole1CreatePost.performSubmitPost(theStage, theUser, textArea_Post.getText());
        });

        Button button_Back = new Button("Back");
        button_Back.setLayoutX(150);
        button_Back.setLayoutY(460);
        button_Back.setOnAction(e -> ViewRole1Home.displayRole1Home(theStage, theUser));

        root.getChildren().addAll(label_Title, label_Instructions, textArea_Post, label_WordCount, button_Submit, button_Back);
        theStage.setScene(theCreatePostScene);
        theStage.show();
    }
}