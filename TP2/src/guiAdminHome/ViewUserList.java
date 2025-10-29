package guiAdminHome;

import database.Database;
import entityClasses.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.sql.SQLException;
import java.util.List;

public class ViewUserList {
    private static Stage theStage;
    private static User theUser;
    private static Database theDatabase = applicationMain.FoundationsMain.database;

    // Table and columns
    private static TableView<User> userTable = new TableView<>();
    private static TableColumn<User, String> usernameColumn = new TableColumn<>("Username");
    private static TableColumn<User, String> emailColumn = new TableColumn<>("Email");
    private static TableColumn<User, String> rolesColumn = new TableColumn<>("Roles");

    // Buttons
    private static Button button_Back = new Button("Back");
    private static Button button_Refresh = new Button("Refresh");

    public static void displayUserList(Stage ps, User user) {
        theStage = ps;
        theUser = user;

        // Create the main layout
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(20));

        // Setup the table
        setupTable();

        // Setup buttons
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER_RIGHT);
        buttonBox.setPadding(new Insets(10, 0, 0, 0));
        buttonBox.getChildren().addAll(button_Refresh, button_Back);

        // Set button actions
        button_Back.setOnAction(e -> ViewAdminHome.displayAdminHome(theStage, theUser));
        button_Refresh.setOnAction(e -> refreshUserList());

        // Setup title
        Label titleLabel = new Label("System Users");
        titleLabel.setFont(Font.font("Arial", 24));
        BorderPane.setAlignment(titleLabel, Pos.CENTER);
        BorderPane.setMargin(titleLabel, new Insets(0, 0, 20, 0));

        // Add components to layout
        root.setTop(titleLabel);
        root.setCenter(userTable);
        root.setBottom(buttonBox);

        // Create scene and show
        Scene scene = new Scene(root, 600, 500);
        theStage.setTitle("User List");
        theStage.setScene(scene);
        theStage.show();

        // Load initial data
        refreshUserList();
    }

    private static void setupTable() {
        // Configure columns with custom cell value factories
        usernameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                return new javafx.beans.property.SimpleStringProperty(param.getValue().getUserName());
            }
        });
        usernameColumn.setMinWidth(150);

        emailColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                String email = param.getValue().getEmailAddress();
                return new javafx.beans.property.SimpleStringProperty(email != null ? email : "<none>");
            }
        });
        emailColumn.setMinWidth(200);

        rolesColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, javafx.beans.value.ObservableValue<String>>() {
            @Override
            public javafx.beans.value.ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> param) {
                User user = param.getValue();
                StringBuilder roles = new StringBuilder();
                if (user.getAdminRole()) roles.append("Admin ");
                if (user.getNewRole1()) roles.append("Role1 ");
                if (user.getNewRole2()) roles.append("Role2 ");
                
                String rolesText = roles.toString().trim();
                if (rolesText.isEmpty()) {
                    rolesText = "<none>";
                }
                return new javafx.beans.property.SimpleStringProperty(rolesText);
            }
        });
        rolesColumn.setMinWidth(150);

        // Add columns to table
        userTable.getColumns().addAll(usernameColumn, emailColumn, rolesColumn);
        
        // Style the table
        userTable.setPlaceholder(new Label("No users found"));
        
        // Set column resize policy
        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    private static void refreshUserList() {
        try {
            List<User> users = theDatabase.getAllUsers();
            ObservableList<User> userData = FXCollections.observableArrayList(users);
            userTable.setItems(userData);
            
            // Update table placeholder based on results
            if (users.isEmpty()) {
                userTable.setPlaceholder(new Label("No users found in the system"));
            }
        } catch (SQLException e) {
            System.err.println("*** ERROR *** Database error in refreshUserList: " + e.getMessage());
            e.printStackTrace();
            
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Database Error");
            errorAlert.setHeaderText(null);
            errorAlert.setContentText("An error occurred while accessing the database.");
            errorAlert.showAndWait();
        }
    }
}