package guiAdminHome;

import database.InvitationDatabase;
import entityClasses.Invitation;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.sql.Connection;
import java.util.List;

public class ManageInvitationDialog extends Stage {
	private final TableView<Invitation> table = new TableView<>();
	

	

    public ManageInvitationDialog(Window owner, Connection conn) {
        setTitle("Manage Invitations");
        initOwner(owner);
        initModality(Modality.WINDOW_MODAL); // block only the parent window

        // Table: one column "Email"
        TableColumn<Invitation, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().email));
        emailCol.setPrefWidth(420);

        table.getColumns().add(emailCol);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        // Buttons, only the Close button does anything for now
        Button resend  = new Button("Resend");
        Button revoke  = new Button("Revoke");
        Button extend7 = new Button("Extend 7d");
        Button close   = new Button("Close");

        close.setOnAction(e -> close());
        // Placeholders there is intentionally no functionality yet
        resend.setOnAction(e -> {});
        revoke.setOnAction(e -> {});
        extend7.setOnAction(e -> {});

        HBox buttons = new HBox(8, resend, revoke, extend7, close);
        buttons.setAlignment(Pos.CENTER_RIGHT);
        buttons.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(table);
        root.setBottom(buttons);
        BorderPane.setMargin(table, new Insets(10, 10, 0, 10));

        Scene scene = new Scene(root, 520, 380);
        setScene(scene);

        // Load emails from Database, is read only
        InvitationDatabase dao = new InvitationDatabase(conn);
        List<Invitation> list = dao.listAll(); // Expects the id and email
        ObservableList<Invitation> items = FXCollections.observableArrayList(list);
        table.setItems(items);
    }
    
    
	

}
