/*package uppgift5;

import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

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
import javafx.stage.Stage;

public class TransactionAccount {

	private static TextField firstText;
	private static TextField secondText;
	private static TextField thirdText;
	private static TextField text;
	private static Object[] transactions;
	private static String balans1;
	private static String spar;
	private static String uttag;
	private static String balans;
	private static LocalDateTime date;
	private static Alert a = new Alert(AlertType.NONE);
	private static Account kund;
	static User login;
	static int c = 0;
	static Stage reg;
	static Object[] record;
	static TableView table1;
	static User customer;
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

		TableView table = new TableView<User>();

		TableColumn<User, String> column1 = new TableColumn<>("Spara");
		column1.setCellValueFactory(new PropertyValueFactory<User, String>("spar"));
		column1.prefWidthProperty().bind(table.widthProperty().multiply(.4));

		TableColumn<User, String> column2 = new TableColumn<>("Uttag");
		column2.setCellValueFactory(new PropertyValueFactory<User, String>("uttag"));
		column2.prefWidthProperty().bind(table.widthProperty().multiply(.3));

		TableColumn<User, String> column3 = new TableColumn<>("OutGoingBalance");
		column3.setCellValueFactory(new PropertyValueFactory<User, String>("balans"));
		column3.prefWidthProperty().bind(table.widthProperty().multiply(.3));

		TableColumn<User, String> column4 = new TableColumn<>("IncomingBalance");
		column4.setCellValueFactory(new PropertyValueFactory<User, String>("balans1"));
		column4.prefWidthProperty().bind(table.widthProperty().multiply(.3));

		TableColumn<User, String> column5 = new TableColumn<>("Date");
		column5.setCellValueFactory(new PropertyValueFactory<User, String>("date"));
		column5.prefWidthProperty().bind(table.widthProperty().multiply(.3));

		table.getColumns().addAll(column4, column1, column2, column3, column5);

		HBox buttons = new HBox(10);

		Button add = new Button("Submit");
		add.setPrefWidth(150);
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

				table.getItems().add(kund = new Account(text.getText(),firstText.getText(),secondText.getText(),thirdText.getText(),LocalDateTime.now()));
				transactions = table.getItems().toArray();
				System.out.println(transactions.toString());

				text.setText("");
				firstText.setText("");
				secondText.setText("");
				thirdText.setText("");

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

		});

		Button finished = new Button("Return");
		finished.setPrefWidth(150);
		finished.setPadding(new Insets(5));
		finished.setOnAction(e -> {
			Main returnscene = new Main();
			returnscene.start(reg);
		});
		Button delete = new Button("Delete");
		delete.setPrefWidth(150);
		delete.setPadding(new Insets(5));
		delete.setOnAction(e -> {
			table.getItems().clear();
		});

		buttons.getChildren().addAll(delete, add, finished);
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
		mainScene.add(thirdText, 3, 14, 6, 1);

		mainScene.add(table, 0, 0, 10, 10);
		mainScene.setPadding(new Insets(5));
		Scene scene = new Scene(mainScene, 650, 400);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();

		return mainScene;
	}

}
*/