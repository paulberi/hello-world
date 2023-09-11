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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class TransactionAccount extends Main {

	private static TextField firstText;
	private static TextField secondText;
	private static TextField thirdText;
	private static TextField text;

	private static Alert a = new Alert(AlertType.NONE);
	static SimpleDateFormat date;
	static TableView table;
	static Account customer;
	public static final String SERIALIZED_FILE_NAME1 = "transactions.xml";

	public static GridPane account(Stage stage) {

		Label label = new Label("Ingångs balans");
		label.setFont(new Font("New Times Romans", 13));
		label.setPadding(new Insets(10));

		Label firstLabel = new Label("Spara");
		firstLabel.setFont(new Font("New Times Romans", 13));
		firstLabel.setPadding(new Insets(10));

		Label secondLabel = new Label("Uttag");
		secondLabel.setFont(new Font("New Times Romans", 13));
		secondLabel.setPadding(new Insets(10));

		Label thirdLabel = new Label("Balans");
		thirdLabel.setFont(new Font("New Times Romans", 13));
		thirdLabel.setPadding(new Insets(10));

		text = new TextField("");
		text.setPadding(new Insets(10));
		text.setPromptText("write your outgoing balance frome last transaction, if none write zero(0)");

		firstText = new TextField("");
		firstText.setPadding(new Insets(10));
		firstText.setPromptText("Write down the amount you want to save");

		secondText = new TextField("");
		secondText.setPadding(new Insets(10));
		secondText.setPromptText("Write down the amount you want to withdraw");

		thirdText = new TextField("");
		thirdText.setPadding(new Insets(10));
		thirdText.setPromptText("For admin use only");

		table = new TableView<Account>();

		TableColumn<Account, String> column1 = new TableColumn<>("Spara");
		column1.setCellValueFactory(new PropertyValueFactory<Account, String>("spar"));
		column1.prefWidthProperty().bind(table.widthProperty().multiply(.2));

		TableColumn<Account, String> column2 = new TableColumn<>("Uttag");
		column2.setCellValueFactory(new PropertyValueFactory<Account, String>("uttag"));
		column2.prefWidthProperty().bind(table.widthProperty().multiply(.2));

		TableColumn<Account, String> column3 = new TableColumn<>("OutGoingBalance");
		column3.setCellValueFactory(new PropertyValueFactory<Account, String>("balans"));
		column3.prefWidthProperty().bind(table.widthProperty().multiply(.2));

		TableColumn<Account, String> column4 = new TableColumn<>("IncomingBalance");
		column4.setCellValueFactory(new PropertyValueFactory<Account, String>("balans1"));
		column4.prefWidthProperty().bind(table.widthProperty().multiply(.2));

		TableColumn<Account, String> column5 = new TableColumn<>("Date");
		column5.setCellValueFactory(new PropertyValueFactory<Account, String>("date"));
		column5.prefWidthProperty().bind(table.widthProperty().multiply(.3));
		
		TableColumn<Account, String> column6 = new TableColumn<>("AccountNumber");
		column6.setCellValueFactory(new PropertyValueFactory<Account, String>("kontoNr"));
		column6.prefWidthProperty().bind(table.widthProperty().multiply(.3));

		table.getColumns().addAll(column4, column1, column2, column3, column5,column6);

		for (Account y : transactions) {
			table.getItems().add(y);
		}

		HBox buttons = new HBox(10);
		Button save=new Button("Save");
		save.setOnAction(e->{
			secondText.setVisible(false);
			secondText.setText("0");
		});
		Button withDraw=new Button("Withdraw");
		withDraw.setOnAction(e->{
			firstText.setVisible(false);
			firstText.setText("0");
		});

		Button add = new Button("Submit");
		add.setPrefWidth(90);
		add.setPadding(new Insets(5));

		add.setOnAction(e -> {

			double balans1 = Double.parseDouble(text.getText());
			double spar = Double.parseDouble(firstText.getText());
			double uttag = Double.parseDouble(secondText.getText());

			double balans3 = balans1 + spar - uttag;
			if (balans3 < 0) {
				a.setAlertType(AlertType.ERROR);
				a.show();

			}

			else {

				String balans4 = String.valueOf(balans3);
				thirdText.setText(balans4);

				table.getItems().addAll(customer = new Account(text.getText(), firstText.getText(), secondText.getText(),thirdText.getText(), date, kund.getKontoNr()));
				
				text.setText(balans4);
				firstText.setText("");
				secondText.setText("");
				thirdText.setText("");
				transactions.add(customer);
				save1();
				TransactionAccount.account(stage);

			}
		});

		Button finished = new Button("Return");
		finished.setPrefWidth(90);
		finished.setPadding(new Insets(5));
		finished.setOnAction(e -> {

			Main returnscene = new Main();
			returnscene.start(stage);
		});
		Button delete = new Button("Delete");
		delete.setPrefWidth(90);
		delete.setPadding(new Insets(5));
		delete.setOnAction(e -> {
			table.getItems().clear();
		});

		buttons.getChildren().addAll(delete, save, withDraw,add, finished);
		buttons.setPadding(new Insets(10));

		GridPane mainScene = new GridPane();

		mainScene.add(buttons, 5, 15, 1, 1);
		mainScene.add(label, 0, 11, 1, 1);
		mainScene.add(firstLabel, 0, 12, 1, 1);
		mainScene.add(secondLabel, 0, 13, 1, 1);
		mainScene.add(thirdLabel, 0, 14, 1, 1);

		mainScene.add(text, 3, 11, 6, 1);
		mainScene.add(firstText, 3, 12, 6, 1);
		mainScene.add(secondText, 3, 13, 6, 1);
		mainScene.add(thirdText, 3, 14, 3, 1);

		mainScene.add(table, 0, 0, 10, 10);
		mainScene.setPadding(new Insets(5));
		Scene scene = new Scene(mainScene, 700, 450);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();

		return mainScene;
	}


	public static void save1() {

		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME1)));
			System.out.println("File stream open and encoder created");
			encoder.writeObject(transactions);
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
	public static void decode1() {
		  XMLDecoder decoder=null;
		  try {
		    decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME1)));
		  System.out.println("File stream opened XMDecoder created");
		            
		  transactions=(ArrayList<Account>) decoder.readObject();
		    decoder.close();
		    System.out.println("file read from transaction xml");
		            
		  }catch(FileNotFoundException e) {
		    System.out.println("ERROR: xml file not found");
		  }
		}
}