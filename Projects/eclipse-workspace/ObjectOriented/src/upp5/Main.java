package upp5;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
import javafx.scene.text.Text;
import javafx.stage.Stage;
public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	//private static List<Account> transactions = new ArrayList<>();
	private static List<User> customers = new ArrayList<>();
	private static Alert a = new Alert(AlertType.NONE);
	private static TableView<User> table;
	private static User newCustomer;
	private static Account newAccount;
	static Text usernames = new Text("00");
	static Text accountNumbers = new Text("00");
	static Text accountDate = new Text("00");
	static TextField accountDeposits = new TextField("00");
	static TextField accountWithdrawals = new TextField("00");
	static Text accountOutBalances = new Text("00");
	static Text accountInBalances = new Text("00");
	static Text lastDeposit = new Text("00");
	static Text lastWithdrawal = new Text("00");
	static Button confirmButton = new Button("Confirm");
	static Button deleteButton = new Button("Exit");
	static Label numberLabel = new Label("Account Number");
	static Label dateLabel = new Label("Date of Account Creation");
	static Label depositLabel = new Label("Deposit Amount");
	static Label withdrawalLabel = new Label("Withdrawal Amount");
	static Label balansOut = new Label("Balance");
	static Label balansIn = new Label("Previous Balance");
	static Label lastdepoLabel = new Label("Last Deposit");
	static Label lastwithdraw = new Label("Last withdrawal");
	private static Account accountEntry;
	private static User accountCreation;

	
	@Override
	public void start(Stage stage) {

		GridPane welcomepage = new GridPane();
		Label usernameLabel = new Label("Användar Namn");

		Label passwordLabel = new Label("Lösenord");
		TextField usernameText = new TextField();
		usernameText.setPrefWidth(150);
		PasswordField passwordText = new PasswordField();
		passwordText.setPromptText("password");
		passwordText.setPrefWidth(150);
		Button createAccountButton = new Button("Skaffa konto");
		Button loginButton = new Button("Login");
		createAccountButton.setOnAction(e -> {
			registration(stage);
		});

		loginButton.setOnAction(e -> {
			
			/*
			loginButton.setOnAction(e -> {
				String usernameToLookFor = userNameTextField.getText();
				String passwordToLookFor = passwordTextField.getText();

				for (Kunder kund : account) {
					if (kund.getUserName().equals(usernameToLookFor) && kund.getPassWord().equals(passwordToLookFor)) {
						System.out.println("We have found the account!");
						TransactionAccount.account(stage);
						return;
					} else {
						
						a.setAlertType(AlertType.ERROR);
						a.show();
					}
				}
			});
*/
			
			if ((usernameText.getText().equals("king")) && (passwordText.getText().equals("king"))) {
				whenLoggedIn(stage);
			} else {
				a.setAlertType(AlertType.ERROR);
				a.show();
			}
		});
		HBox users = new HBox();

		users.getChildren().addAll(usernameLabel, usernameText);
		users.setPadding(new Insets(30));
		users.setSpacing(20);
		HBox pass = new HBox();
		pass.getChildren().addAll(passwordLabel, passwordText);
		pass.setPadding(new Insets(30));
		pass.setSpacing(55);
		HBox button = new HBox();
		button.getChildren().addAll(loginButton, createAccountButton);
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

	public static void registration(Stage stage) {

		SplitPane sp = new SplitPane();

		Label nameLabel = new Label("Name");
		nameLabel.setPadding(new Insets(5));
		Label accountNumberLabel = new Label("Desired Account number");
		accountNumberLabel.setPadding(new Insets(5));
		Label userNameLabel = new Label("Username");
		userNameLabel.setPadding(new Insets(5));
		Label passwordLabel = new Label("Password");
		passwordLabel.setPadding(new Insets(5));

		TextField nameText = new TextField();
		nameText.setPadding(new Insets(5));
		TextField accountNumber = new TextField();
		accountNumber.setPadding(new Insets(5));
		TextField userNameText = new TextField();
		userNameText.setPadding(new Insets(5));
		PasswordField passwordText = new PasswordField();
		passwordText.setPadding(new Insets(5));
		Button confirmAccountCreationbutton = new Button("Submit");
		Button resetAccountTablebutton = new Button("Delete");

		VBox labels = new VBox(35, nameLabel, accountNumberLabel, userNameLabel, passwordLabel,
				resetAccountTablebutton);
		labels.setPadding(new Insets(20));
		labels.setMaxWidth(300);

		VBox text = new VBox(35, nameText, accountNumber, userNameText, passwordText, confirmAccountCreationbutton);
		text.setPadding(new Insets(20));
		text.setMinWidth(200);

		confirmAccountCreationbutton.setOnAction(e -> {
			if (!table.isEditable()) {
				table.setEditable(true);
				table.getItems().add(newCustomer = new User(nameText.getText(), accountNumber.getText(),
						userNameText.getText(), passwordText.getText(), LocalDateTime.now(),null,null));
				nameText.setText("");
				accountNumber.setText("");
				userNameText.setText("");
				passwordText.setText("");
				
				customers.add(newCustomer);
		
				confirmAccountCreationbutton.setText("Return");
			} else {
				Main firstStage = new Main();
				firstStage.start(stage);
			}
		});
		resetAccountTablebutton.setOnAction(e -> {
			table.getItems().clear();
			Main firstStage = new Main();
			firstStage.start(stage);
		});

		table = new TableView<User>();

		TableColumn<User, String> column1 = new TableColumn<>("Name");
		column1.setCellValueFactory(new PropertyValueFactory<>("name"));
		column1.prefWidthProperty().bind(table.widthProperty().multiply(.3));
		TableColumn<User, String> column2 = new TableColumn<>("Account");
		column2.setCellValueFactory(new PropertyValueFactory<>("KontoNr"));
		column2.prefWidthProperty().bind(table.widthProperty().multiply(.3));
		TableColumn<User, String> column3 = new TableColumn<>("Username");
		column3.setCellValueFactory(new PropertyValueFactory<>("userName"));
		column3.prefWidthProperty().bind(table.widthProperty().multiply(.3));
		TableColumn<User, String> column4 = new TableColumn<>("Password");
		column4.setCellValueFactory(new PropertyValueFactory<>("passWord"));
		column4.prefWidthProperty().bind(table.widthProperty().multiply(.3));
		TableColumn<User, LocalDateTime> column5 = new TableColumn<>("Date of Creation");
		column5.setCellValueFactory(new PropertyValueFactory<>("dateOfAccount"));
		column5.prefWidthProperty().bind(table.widthProperty().multiply(.3));
		/*
		 * column5.setCellFactory(column->{ TableCell<User,LocalDateTime> cell= new
		 * TableCell<User,LocalDateTime>(){ private SimpleDateFormat format=new
		 * SimpleDateFormat("yyyy.MM.dd");
		 * 
		 * @Override protected void updateItem(LocalDateTime dateOfAccount,boolean
		 * empty) { super.updateItem(dateOfAccount, empty); if(empty) { setText(null);
		 * }else { setText(format.format(dateOfAccount)); } } }; return cell; });
		 */

		table.getColumns().addAll(column1, column2, column3, column4, column5);

		sp.getItems().addAll(labels, text, table);

		Scene scene = new Scene(sp, 700, 400);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();

	}

	public void whenLoggedIn(Stage stage) {

		Label assignMenu = new Label("Choose a service");
		assignMenu.setPadding(new Insets(10));
		assignMenu.setFont(Font.font("Times New Romans", 20));
		ComboBox<String> accountMenu = new ComboBox<String>();
		ObservableList<String> items = FXCollections.observableArrayList("Withdraw Money", "Save Money",
				"Check Account Balance", "Logout");
		accountMenu.setPrefSize(270, 40);
		accountMenu.getItems().addAll(items);
		HBox dropDownMenu = new HBox();
		dropDownMenu.getChildren().addAll(assignMenu, accountMenu);
		GridPane homePage = new GridPane();
		homePage.add(dropDownMenu, 0, 0);
		// homePage.add(accountMenu,1,0,4,4);
		Scene scene = new Scene(homePage, 500, 400);
		stage.setTitle("Welcome To your Account");
		stage.setScene(scene);
		stage.show();
		accountMenu.setOnAction(e -> {
			if (accountMenu.getValue().equals("Withdraw Money")) {
				
				withdrawMoney(stage);
			} else if (accountMenu.getValue().equals("Save Money")) {
				
				saveMoney(stage);
			} else if (accountMenu.getValue().equals("Check Account Balance")) {
				
				accountBalance(stage);
			} else if (accountMenu.getValue().equals("Logout")) {
				logout(stage);
			}
		});

	}

	private void logout(Stage stage) {
		// TODO Auto-generated method stub

		Main firstStage = new Main();
		firstStage.start(stage);
	}

	private void accountBalance(Stage stage) {
		// TODO Auto-generated method stub
		
		accountWithdrawals.setVisible(false);
		accountDeposits.setVisible(false);

		Label assignMenu = new Label("Choose a service");
		assignMenu.setPadding(new Insets(10));
		assignMenu.setFont(Font.font("Times New Romans", 20));
		ComboBox<String> accountMenu = new ComboBox<String>();
		ObservableList<String> items = FXCollections.observableArrayList("Withdraw Money", "Save Money",
				"Check Account Balance", "Logout");
		accountMenu.setPrefSize(270, 40);
		accountMenu.getItems().addAll(items);
		HBox dropDownMenu = new HBox();
		dropDownMenu.getChildren().addAll(assignMenu, accountMenu);
		GridPane homePage = new GridPane();
		homePage.add(dropDownMenu, 0, 0);
		withdrawalLabel.setPadding(new Insets(15, 0, 0, 0));
		SplitPane forTransactions = new SplitPane();
		VBox labels = new VBox(35, usernames, dateLabel, depositLabel, withdrawalLabel, balansOut, balansIn,
				confirmButton);
		labels.setPadding(new Insets(20));
		labels.setMaxWidth(300);
		VBox text = new VBox(35, accountNumbers, accountDate, accountDeposits, accountWithdrawals, accountOutBalances,
				accountInBalances, deleteButton);
		text.setPadding(new Insets(20));
		text.setMinWidth(200);
		forTransactions.getItems().addAll(labels, text);

		homePage.add(forTransactions, 0, 1);
		homePage.add(lastdepoLabel, 0, 2);
		homePage.add(lastDeposit, 0, 3);
		homePage.add(lastwithdraw, 1, 2);
		homePage.add(lastWithdrawal, 1, 3);
		
		confirmButton.setOnAction(e -> {
			
			accountInBalances.setText(accountOutBalances.getText());
			lastDeposit.setText(accountDeposits.getText());
			lastWithdrawal.setText(accountWithdrawals.getText());
			
			
			String hello=accountEntry.getBalans1();

			double balans1 = Double.parseDouble(accountInBalances.getText());
			double spar = Double.parseDouble(accountDeposits.getText());
			double uttag = Double.parseDouble(accountWithdrawals.getText());
			
			
			double balans = balans1 + spar - uttag;
			if (balans < 0) {
				a.setAlertType(AlertType.ERROR);
				a.show();

			}

			else if(balans>=0){

				String balans4 = String.valueOf(balans);
				accountOutBalances.setText(balans4);
				
			
				accountDeposits.setText(lastDeposit.getText());
				accountWithdrawals.setText(lastWithdrawal.getText());
				
				accountDeposits.setText("");
				accountWithdrawals.setText("");
				confirmButton.setText("Exit");
			}
		});
		deleteButton.setOnAction(e->{
			whenLoggedIn(stage);
		});
		newCustomer=new User();

		Scene scene = new Scene(homePage, 500, 600);
		stage.setTitle("Welcome To your Account");
		stage.setScene(scene);
		stage.show();
		

	}

	private void saveMoney(Stage stage) {
		// TODO Auto-generated method stub
		accountWithdrawals.setVisible(false);
		Label assignMenu = new Label("Choose a service");
		assignMenu.setPadding(new Insets(10));
		assignMenu.setFont(Font.font("Times New Romans", 20));
		ComboBox<String> accountMenu = new ComboBox<String>();
		ObservableList<String> items = FXCollections.observableArrayList("Withdraw Money", "Save Money",
				"Check Account Balance", "Logout");
		accountMenu.setPrefSize(270, 40);
		accountMenu.getItems().addAll(items);
		HBox dropDownMenu = new HBox();
		dropDownMenu.getChildren().addAll(assignMenu, accountMenu);
		GridPane homePage = new GridPane();
		homePage.add(dropDownMenu, 0, 0);
		withdrawalLabel.setPadding(new Insets(15, 0, 0, 0));
		SplitPane forTransactions = new SplitPane();
		VBox labels = new VBox(35, usernames, dateLabel, depositLabel, withdrawalLabel, balansOut, balansIn,
				confirmButton);
		labels.setPadding(new Insets(20));
		labels.setMaxWidth(300);
		VBox text = new VBox(35, accountNumbers, accountDate, accountDeposits, accountWithdrawals, accountOutBalances,
				accountInBalances, deleteButton);
		text.setPadding(new Insets(20));
		text.setMinWidth(200);
		forTransactions.getItems().addAll(labels, text);

		homePage.add(forTransactions, 0, 1);
		homePage.add(lastdepoLabel, 0, 2);
		homePage.add(lastDeposit, 0, 3);
		homePage.add(lastwithdraw, 1, 2);
		homePage.add(lastWithdrawal, 1, 3);
		
	confirmButton.setOnAction(e -> {
			
			accountInBalances.setText(accountOutBalances.getText());
			//lastDeposit.setText(accountDeposits.getText());
			//lastWithdrawal.setText(accountWithdrawals.getText());
			

			double balans1 = Double.parseDouble(accountInBalances.getText());
			double spar = Double.parseDouble(accountDeposits.getText());
			//double uttag = Double.parseDouble(accountWithdrawals.getText());
			
			
			double balans = balans1 + spar ;//- uttag;
			if (balans < 0) {
				a.setAlertType(AlertType.ERROR);
				a.show();

			}

			else if(balans>=0){

				String balans4 = String.valueOf(balans);
				accountOutBalances.setText(balans4);
				
			
				lastDeposit.setText(accountDeposits.getText());
				accountWithdrawals.setText(lastWithdrawal.getText());
				
				accountDeposits.setText("");
				//accountWithdrawals.setText("");
				
			}
		});
		deleteButton.setOnAction(e->{
			whenLoggedIn(stage);
		});
		
		
		
		Scene scene = new Scene(homePage, 500, 600);
		stage.setTitle("Welcome To your Account");
		stage.setScene(scene);
		stage.show();
	}

	private void withdrawMoney(Stage stage) {
		// TODO Auto-generated method stub
		accountDeposits.setVisible(false);
		Label assignMenu = new Label("Choose a service");
		assignMenu.setPadding(new Insets(10));
		assignMenu.setFont(Font.font("Times New Romans", 20));
		ComboBox<String> accountMenu = new ComboBox<String>();
		ObservableList<String> items = FXCollections.observableArrayList("Withdraw Money", "Save Money",
				"Check Account Balance", "Logout");
		accountMenu.setPrefSize(270, 40);
		accountMenu.getItems().addAll(items);
		HBox dropDownMenu = new HBox();
		dropDownMenu.getChildren().addAll(assignMenu, accountMenu);
		GridPane homePage = new GridPane();
		homePage.add(dropDownMenu, 0, 0);
		withdrawalLabel.setPadding(new Insets(15, 0, 0, 0));
		SplitPane forTransactions = new SplitPane();
		VBox labels = new VBox(35, usernames, dateLabel, depositLabel, withdrawalLabel, balansOut, balansIn,
				confirmButton);
		labels.setPadding(new Insets(20));
		labels.setMaxWidth(300);
		VBox text = new VBox(35, accountNumbers, accountDate, accountDeposits, accountWithdrawals, accountOutBalances,
				accountInBalances, deleteButton);
		text.setPadding(new Insets(20));
		text.setMinWidth(200);
		forTransactions.getItems().addAll(labels, text);

		homePage.add(forTransactions, 0, 1);
		homePage.add(lastdepoLabel, 0, 2);
		homePage.add(lastDeposit, 0, 3);
		homePage.add(lastwithdraw, 1, 2);
		homePage.add(lastWithdrawal, 1, 3);
		
confirmButton.setOnAction(e -> {
			
			accountInBalances.setText(accountOutBalances.getText());
			//lastDeposit.setText(accountDeposits.getText());
			//lastWithdrawal.setText(accountWithdrawals.getText());
			

			double balans1 = Double.parseDouble(accountInBalances.getText());
			//double spar = Double.parseDouble(accountDeposits.getText());
			double uttag = Double.parseDouble(accountWithdrawals.getText());
			
			
			double balans = balans1 - uttag;
			if (balans < 0) {
				a.setAlertType(AlertType.ERROR);
				a.show();

			}

			else if(balans>=0){

				String balans4 = String.valueOf(balans);
				accountOutBalances.setText(balans4);
				
			
				lastDeposit.setText(accountDeposits.getText());
				accountWithdrawals.setText(lastWithdrawal.getText());
				
				accountDeposits.setText("");
				//accountWithdrawals.setText("");
				
			}
		});
		deleteButton.setOnAction(e->{
			whenLoggedIn(stage);
		});
		
		
		Scene scene = new Scene(homePage, 500, 600);
		stage.setTitle("Welcome To your Account");
		stage.setScene(scene);
		stage.show();
	}

}
