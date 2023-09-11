/*package uppgift5;

import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;

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

public class CustomerDetails {

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
	public static final String SERIALIZED_FILE_NAME = "customer.xml";

	public static SplitPane registration(Stage stage) {

		GridPane grid = new GridPane();

		SplitPane sp = new SplitPane();

		Label name = new Label("Name");
		name.setPadding(new Insets(5));
		Label birth = new Label("Date of birth");
		birth.setPadding(new Insets(5));
		Label account = new Label("Desired Account number");
		account.setPadding(new Insets(5));
		Label user = new Label("Username");
		user.setPadding(new Insets(5));
		Label password = new Label("Password");
		password.setPadding(new Insets(5));

		TextField name1 = new TextField();
		name1.setPadding(new Insets(5));

		TextField birth1 = new TextField();
		birth1.setPadding(new Insets(5));
		TextField account1 = new TextField();
		account1.setPadding(new Insets(5));
		TextField user1 = new TextField();
		user1.setPadding(new Insets(5));
		PasswordField password1 = new PasswordField();
		password1.setPadding(new Insets(5));
		Button button = new Button("Submit");
		Button button1 = new Button("Delete");

		VBox labels = new VBox(35, name, birth, account, user, password, button1);
		labels.setPadding(new Insets(20));
		labels.setMaxWidth(300);

		VBox text = new VBox(35, name1, birth1, account1, user1, password1, button);
		text.setPadding(new Insets(20));
		text.setMinWidth(200);

		button.setOnAction(e -> {
			if (!table1.isEditable()) {
				table1.setEditable(true);

				table1.getItems().add(login = new User(name1.getText(), birth1.getText(), account1.getText(),
						user1.getText(), password1.getText(), LocalDateTime.now()));
				name1.setText("");
				birth1.setText("");
				account1.setText("");
				user1.setText("");
				password1.setText("");
				button.setText("Return");
				record = table1.getItems().toArray();

				XMLEncoder encoder = null;
				try {
					encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
					System.out.println("File stream open and encoder created");
					encoder.writeObject(record);
					System.out.println("object written");
					encoder.flush();
					encoder.close();
					System.out.println("file stream closed");

				} catch (FileNotFoundException fileNotFound) {
					System.out.println("ERROR: While creating or opening the File Staff.xml");

				}
			} else {
				Main returnscene = new Main();
				returnscene.start(reg);

			}

		});
		button1.setOnAction(e -> {
			table1.getItems().clear();
		});

		table1 = new TableView<User>();

		TableColumn<User, String> column1 = new TableColumn<>("Name");
		column1.setCellValueFactory(new PropertyValueFactory<>("name"));
		column1.prefWidthProperty().bind(table1.widthProperty().multiply(.5));
		TableColumn<User, String> column2 = new TableColumn<>("Date Of Birth");
		column2.setCellValueFactory(new PropertyValueFactory<>("dob"));
		column2.prefWidthProperty().bind(table1.widthProperty().multiply(.5));
		TableColumn<User, String> column3 = new TableColumn<>("Account");
		column3.setCellValueFactory(new PropertyValueFactory<>("KontoNr"));
		column3.prefWidthProperty().bind(table1.widthProperty().multiply(.5));
		TableColumn<User, String> column4 = new TableColumn<>("Username");
		column4.setCellValueFactory(new PropertyValueFactory<>("userName"));
		column4.prefWidthProperty().bind(table1.widthProperty().multiply(.5));
		TableColumn<User, String> column5 = new TableColumn<>("Password");
		column5.setCellValueFactory(new PropertyValueFactory<>("passWord"));
		column5.prefWidthProperty().bind(table1.widthProperty().multiply(.5));
		TableColumn<User, String> column6 = new TableColumn<>("Date of Creation");
		column6.setCellValueFactory(new PropertyValueFactory<>("dateOfAccount"));
		column6.prefWidthProperty().bind(table1.widthProperty().multiply(.5));

		table1.getColumns().addAll(column1, column2, column3, column4, column5, column6);

		XMLDecoder decoder = null;
		try {
			decoder = new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
			System.out.println("File stream open and decoder created");
			record = (Object[]) decoder.readObject();
			System.out.println("object read");
			decoder.close();
			System.out.println("file stream closed");

			for (Object x : record) {
				table1.getItems().add(x);
			}

		} catch (FileNotFoundException fileNotFound) {
			System.out.println("ERROR: While creating or opening the File Staff.xml");

		} finally {
		}

		sp.getItems().addAll(labels, text, table1);

		Scene scene = new Scene(sp, 700, 400);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();
		return sp;

	}

}
*/