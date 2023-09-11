package uppgift5;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CustomerDetails extends Main {

	private static Alert a = new Alert(AlertType.NONE);

	static int c = 0;
	static Stage reg;
	static SimpleDateFormat date;

	static TableView table1;
	static Kunder login;
	public static final String SERIALIZED_FILE_NAME = "customer.xml";

	public static SplitPane registration(Stage stage) {

		SplitPane sp = new SplitPane();

		Label nameLabel = new Label("Name");
		nameLabel.setPadding(new Insets(5));
		Label dateOfCreationLabel = new Label("Date of Creation");
		dateOfCreationLabel.setPadding(new Insets(5));
		Label accountNrLabel = new Label("Desired Account number");
		accountNrLabel.setPadding(new Insets(5));
		Label passwordLabel = new Label("Password");
		passwordLabel.setPadding(new Insets(5));

		TextField nameField = new TextField();
		nameField.setPadding(new Insets(5));

		TextField dateOfAccountText = new TextField();
		dateOfAccountText.setPadding(new Insets(5));

		TextField accountText = new TextField();
		accountText.setPadding(new Insets(5));
		PasswordField passwordText = new PasswordField();
		passwordText.setPadding(new Insets(5));

		Button returnButton = new Button("Return");
		returnButton.setPrefWidth(90);
		returnButton.setPadding(new Insets(5));
		returnButton.setOnAction(e -> {

			Main returnscene = new Main();
			returnscene.start(stage);
		});
		Button addAccountbutton = new Button("Submit");
		addAccountbutton.setOnAction(e -> {
			kund = new Kunder(nameField.getText(), accountText.getText(), passwordText.getText(), dateOfAccountText.getText(), null, null);
			Main.customers.add(kund);
			
		
			TransactionAccount.account(stage);
		});

		VBox labels = new VBox(35, nameLabel, dateOfCreationLabel, accountNrLabel, passwordLabel, returnButton);
		labels.setPadding(new Insets(20));
		labels.setMaxWidth(300);

		VBox text = new VBox(35, nameField, dateOfAccountText, accountText, passwordText, addAccountbutton);
		text.setPadding(new Insets(20));
		text.setMinWidth(200);

		sp.getItems().addAll(labels, text);

		Scene scene = new Scene(sp, 350, 300);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();
		return sp;


	}
	public static void save(){

		
		XMLEncoder encoder = null;
	try {
		encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
		System.out.println("File stream open and encoder created");
		encoder.writeObject(account);
		System.out.println("object written");
		encoder.flush();
		encoder.close();
		System.out.println("file stream closed");
		

	} catch (FileNotFoundException fileNotFound) {
		System.out.println("ERROR: While creating or opening the File Staff.xml");

	}
	}
	public static void decode() {
		  XMLDecoder decoder=null;
		  try {
		    decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
		  System.out.println("File stream opened XMDecoder created");
		            
		    account=(ArrayList<Kunder>) decoder.readObject();
		    decoder.close();
		    System.out.println("file read from transaction xml");
		            
		  }catch(FileNotFoundException e) {
		    System.out.println("ERROR: xml file not found");
		  }

	}
}
