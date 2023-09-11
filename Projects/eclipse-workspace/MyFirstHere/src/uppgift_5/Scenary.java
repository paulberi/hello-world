package uppgift_5;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableList;
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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Scenary extends Main {
	static Stage stage;
	static LocalDateTime dateTime = LocalDateTime.now();
	private static Alert a = new Alert(AlertType.NONE);
	static TableView <Kunder>table;
	static TextField incomingBalanceFieldtext ;
	static TextField lastTransactionText ;
	static TextField accountNrText ;

	public Scenary(Stage stage, ArrayList<Kunder> customers) {
		this.stage = stage;
		this.customers = customers;
	}

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
			//Main.customers.add(kund);
			
		
			account(stage, kund);
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

	public static GridPane account(Stage stage, Kunder konto) {

		Label inBalansLabel = new Label("Icoming Balance");
		inBalansLabel.setFont(new Font("New Times Romans", 13));
		inBalansLabel.setPadding(new Insets(10));

		Label saveLabel = new Label("Save");
		saveLabel.setFont(new Font("New Times Romans", 13));
		saveLabel.setPadding(new Insets(10));

		Label withdrawLabel = new Label("Witdraw");
		withdrawLabel.setFont(new Font("New Times Romans", 13));
		withdrawLabel.setPadding(new Insets(10));

		Label lastTransactionLabel = new Label("Latest transaction");
		lastTransactionLabel.setFont(new Font("New Times Romans", 13));
		lastTransactionLabel.setPadding(new Insets(10));

		Label outBalansLabel = new Label("Balance");
		outBalansLabel.setFont(new Font("New Times Romans", 13));
		outBalansLabel.setPadding(new Insets(10));

		VBox labels = new VBox(inBalansLabel, saveLabel, withdrawLabel, outBalansLabel, lastTransactionLabel);

		incomingBalanceFieldtext = new TextField();
		incomingBalanceFieldtext.setPadding(new Insets(10));
		incomingBalanceFieldtext
				.setPromptText("write your outgoing balance frome last transaction, if none write zero(0)");

		TextField saveText = new TextField("");
		saveText.setPadding(new Insets(10));
		saveText.setPromptText("Write down the amount you want to save");
		saveText.setVisible(false);

		TextField withdrawText = new TextField("");
		withdrawText.setPadding(new Insets(10));
		withdrawText.setPromptText("Write down the amount you want to withdraw");
		withdrawText.setVisible(false);

		TextField outGoingBalanceText = new TextField("");
		outGoingBalanceText.setPadding(new Insets(10));
		outGoingBalanceText.setPromptText("For admin use only");

		lastTransactionText = new TextField();
		lastTransactionText.setPadding(new Insets(10));
		lastTransactionText.setPromptText("For admin use only");
		
		accountNrText = new TextField();
		accountNrText.setPadding(new Insets(10));
		accountNrText.setPromptText("For admin use only");

		Label accountNrLabel = new Label("Account Number");
		accountNrLabel.setPadding(new Insets(10));

		VBox text = new VBox(incomingBalanceFieldtext, saveText, withdrawText, outGoingBalanceText,
				lastTransactionText);
		text.setMinWidth(200);

		HBox accountNr = new HBox(accountNrLabel, accountNrText);

		HBox buttons = new HBox(10);
		Button save = new Button("Save");
		save.setOnAction(e -> {
			saveText.setVisible(true);
			withdrawText.setText("0");
		});
		Button withDraw = new Button("Withdraw");
		withDraw.setOnAction(e -> {
			withdrawText.setVisible(true);
			saveText.setText("0");
		});

		Button submitButton = new Button("Submit");
		submitButton.setPrefWidth(90);
		submitButton.setPadding(new Insets(5));

		submitButton.setOnAction(e -> {

			double balans1 = Double.parseDouble(incomingBalanceFieldtext.getText());

			double spar = Double.parseDouble(saveText.getText());
			double uttag = Double.parseDouble(withdrawText.getText());

			double balans3 = balans1 + spar - uttag;
			if (balans3 < 0) {
				a.setAlertType(AlertType.ERROR);
				a.show();

			}

			else {
				String balans4 = String.valueOf(balans3);
				outGoingBalanceText.setText(balans4);
				incomingBalanceFieldtext.setText(outGoingBalanceText.getText());

				if (saveText.isVisible()) {
					lastTransactionText.setText(saveText.getText());
					saveText.setText("");

				} else if (withdrawText.isVisible()) {
					lastTransactionText.setText(withdrawText.getText());
					withdrawText.setText("");
				}
				saveText.setVisible(false);
				withdrawText.setVisible(false);

				
				kund = new Kunder(kund.getName(), kund.getKontoNr(), kund.getPassWord(), kund.getDateOfAccount(),
						incomingBalanceFieldtext.getText(), lastTransactionText.getText());
				//kund=new Kunder(null,kund.getKontoNr(),null,kund.getDateOfAccount(),incomingBalanceFieldtext.getText(),lastTransactionText.getText());
			
				Main.customers.add(kund);
				table.getItems().add(kund);
				
				
						}

		});

		Button returnButton = new Button("Return");
		returnButton.setPrefWidth(90);
		returnButton.setPadding(new Insets(5));
		returnButton.setOnAction(e -> {
			

			Main returnscene = new Main();
			returnscene.start(stage);
		});
		
		table = new TableView<Kunder>();

		table.setMinWidth(400);
		TableColumn<Kunder, String> column3 = new TableColumn<>("Name");
		column3.setCellValueFactory(new PropertyValueFactory<Kunder, String>("name"));
		column3.prefWidthProperty().bind(table.widthProperty().multiply(.2));

		TableColumn<Kunder, String> column4 = new TableColumn<>("Account");
		column4.setCellValueFactory(new PropertyValueFactory<Kunder, String>("kontoNr"));
		column4.prefWidthProperty().bind(table.widthProperty().multiply(.2));

		TableColumn<Kunder, String> column5 = new TableColumn<>("Incoming Balance");
		column5.setCellValueFactory(new PropertyValueFactory<Kunder, String>("incomingBalance"));
		column5.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		
		TableColumn<Kunder, String> column6 = new TableColumn<>("Last Transaction");
		column6.setCellValueFactory(new PropertyValueFactory<Kunder, String>("latestTransaction"));
		column6.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		for(Kunder x:customers) {
			table.getItems().add(x);
		}

		table.getColumns().addAll(column4,column3, column5,column6);

		buttons.getChildren().addAll(save, withDraw, submitButton, returnButton);
		buttons.setPadding(new Insets(10));

		SplitPane textAndLabelssp = new SplitPane(labels, text,table);

		GridPane mainScene = new GridPane();

		mainScene.add(buttons, 0, 1, 1, 1);

		mainScene.add(textAndLabelssp, 0, 0, 1, 1);
		mainScene.add(accountNr, 0, 2, 1, 1);
		mainScene.setPadding(new Insets(5));
		mainScene.setMinWidth(680);
		Scene scene = new Scene(mainScene, 700, 300);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();

		return mainScene;
	}
	public static void mouseEvent() {
		table.setOnMousePressed(e -> {
			
			ObservableList<Kunder> selectedItems = table.getSelectionModel().getSelectedItems();
			String n = table.getItems().get(table.getSelectionModel().getSelectedIndex()).getKontoNr();
			String p = table.getItems().get(table.getSelectionModel().getSelectedIndex()).getLatestTransaction();
			String t = table.getItems().get(table.getSelectionModel().getSelectedIndex()).getIncomingBalance();
			String z = table.getItems().get(table.getSelectionModel().getSelectedIndex()).getDateOfAccount();

			incomingBalanceFieldtext.setText(t);
			lastTransactionText.setText(p);
			accountNrText.setText(n);
			
		});
	}

}
