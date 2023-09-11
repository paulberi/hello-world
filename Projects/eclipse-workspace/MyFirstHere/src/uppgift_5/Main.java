package uppgift_5;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64.Decoder;
import java.util.List;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

	private static Alert a = new Alert(AlertType.NONE);
	protected static ArrayList<Kunder> customers = new ArrayList<Kunder>(); // implement logic to get all customers
	
	public static final String SERIALIZED_FILE_NAME = "customer.xml";
	
	//static String date;
	
	static Kunder kund;
	
	public static void main(String[] args) {
		launch(args);
	}	
	
	@Override
	public void start(Stage stage) {
		
		
		//decode();

		GridPane welcomepage = new GridPane();
		Label kontoLabel = new Label("Konto Nummer");

		Label passwordLabel = new Label("Lösenord");

		TextField kontoTextField = new TextField();
		kontoTextField.setPrefWidth(150);

		PasswordField passwordTextField = new PasswordField();
		passwordTextField.setPromptText("password");
		passwordTextField.setPrefWidth(150);

		Button createKontobutton = new Button("Skaffa konto");

		Button loginButton = new Button("Login");

		Button saveToXML = new Button("Save");
		saveToXML.setOnAction(e -> {
		save();
		});

		createKontobutton.setOnAction(e -> {
			Scenary.registration(stage);

		});

		/*
		 * Given a username and password If there is any customer with these values,
		 * then it is the right person otherwise not authentic
		 */
		
		

		loginButton.setOnAction(e -> {
		/*	String usernameToLookFor = kontoTextField.getText();
			String passwordToLookFor = passwordTextField.getText();
			
			for (Kunder kund : customers) {
				if (kund.getKontoNr().equals(usernameToLookFor) && kund.getPassWord().equals(passwordToLookFor)) { 
					System.out.println("We have found the account!");
				
					Scenary.account(stage,kund);
					Scenary.mouseEvent();
					return;
					
				} else {
					
					a.setAlertType(AlertType.ERROR);
					a.show();
				}
			}*/
			
			if(kontoTextField.getText().equals("king")&&passwordTextField.getText().equals("king")) {
				Scenary.account(stage,kund);
				
			}
		});

		HBox users = new HBox();

		users.getChildren().addAll(kontoLabel, kontoTextField);
		users.setPadding(new Insets(30));
		users.setSpacing(20);
		HBox pass = new HBox();
		pass.getChildren().addAll(passwordLabel, passwordTextField);
		pass.setPadding(new Insets(30));
		pass.setSpacing(55);
		HBox button = new HBox();
		button.getChildren().addAll(loginButton, createKontobutton, saveToXML);
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

	
	public static ArrayList<Kunder> decode() {
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
		return customers;

		//account=(List<Kunder>) customers.get(2);
		// transactions=(List<Kunder>) customers.get(1);
		
		

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
