package uppgift5;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

	private static Alert a = new Alert(AlertType.NONE);
	static int c = 0;
	static Stage reg;
	protected static ArrayList<Kunder> customers = new ArrayList<Kunder>(); // implement logic to get all customers
	static List<Kunder> account = new ArrayList<Kunder>();
	protected static List<Kunder> transactions = new ArrayList<Kunder>();
	static Kunder customer;
	public static final String SERIALIZED_FILE_NAME = "customer.xml";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {

		decode();

		GridPane welcomepage = new GridPane();
		Label user = new Label("Användar Namn");

		Label passwordLabel = new Label("Lösenord");

		TextField userNameTextField = new TextField();
		userNameTextField.setPrefWidth(150);

		PasswordField passwordTextField = new PasswordField();
		passwordTextField.setPromptText("password");
		passwordTextField.setPrefWidth(150);

		Button button2 = new Button("Skaffa konto");

		Button loginButton = new Button("Login");

		Button saveToXML = new Button("Save");
		saveToXML.setOnAction(e -> {
			save();
		});

		button2.setOnAction(e -> {
			CustomerDetails.registration(stage);

		});

		/*
		 * Given a username and password If there is any customer with these values,
		 * then it is the right person otherwise not authentic
		 */

		loginButton.setOnAction(e -> {
			String usernameToLookFor = userNameTextField.getText();
			String passwordToLookFor = passwordTextField.getText();

			for (Kunder kund : customers) {
				if (kund.getUserName().equals(usernameToLookFor) && kund.getPassWord().equals(passwordToLookFor)) {
					System.out.println("Success");
					// TransactionAccount.account(stage);
					return;
				}
			}
		});

		HBox users = new HBox();

		users.getChildren().addAll(user, userNameTextField);
		users.setPadding(new Insets(30));
		users.setSpacing(20);
		HBox pass = new HBox();
		pass.getChildren().addAll(passwordLabel, passwordTextField);
		pass.setPadding(new Insets(30));
		pass.setSpacing(55);
		HBox button = new HBox();
		button.getChildren().addAll(loginButton, button2, saveToXML);
		button.setPadding(new Insets(30));
		button.setSpacing(40);
		button.setAlignment(Pos.CENTER);

		welcomepage.addRow(1, users);
		welcomepage.addRow(3, pass);
		welcomepage.addRow(5, button);
		Scene scene = new Scene(welcomepage, 350, 300);
		stage.setTitle("Välkomen till Bankomaten");
		stage.setScene(scene);
		stage.show();
	}

	public static void save() {
		customers.addAll(account);
		customers.addAll(transactions);

		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
			System.out.println("File stream open and encoder created");
			encoder.writeObject(customers);
			System.out.println("object written");
			encoder.flush();
			encoder.close();
			System.out.println("file stream closed");

		} catch (FileNotFoundException fileNotFound) {
			System.out.println("ERROR: While creating or opening the File Staff.xml");

		}
	}

	/**
	 * 
	 */
	public static void decode() {
		XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
			System.out.println("File stream opened XMDecoder created");

			customers = (ArrayList<Kunder>) decoder.readObject();
			decoder.close();
			System.out.println("file read from transaction xml");

		} catch (FileNotFoundException e) {
			System.out.println("ERROR: xml file not found");
		}
	}

	public static void main(String[] args) {
		launch(args);
	}
}

/*
 * 
 * private static final List<Kunder> getCustomers() {
 * 
 * return customers; }
 * 
 * 
 * private static final void setCustomers(List<Kunder> customers) { // todo
 * implement how to get customers Main.customers = customers; }
 * 
 * 
 * 
 * 
 * public static boolean isValidCustomer(final String username, final String
 * password) { if(username.isEmpty() || password.isEmpty()) return false;
 * 
 * if(customers.isEmpty()) return false;
 * 
 * for(Kunder customer: customers) {
 * if(customer.getUserName().equalsIgnoreCase(username.trim()) &&
 * customer.getPassWord().equalsIgnoreCase(password.trim())) { return true; }
 * 
 * }
 * 
 * return false;
 * 
 */
