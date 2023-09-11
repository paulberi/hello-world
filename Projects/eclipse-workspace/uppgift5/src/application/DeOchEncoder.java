package application;

/*import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

public class DeOchEncoder extends Application {
	
	private static final String SERIALIZED_FILE_NAME = null;
	private static TextField firstText;
	private static TextField secondText;
	private static TextField thirdText;
	private static TextField text;
	private static Object[] transactions;
	private static String balans1;

	private static LocalDateTime date;
	private static Alert a=new Alert(AlertType.NONE);
	private static Account kund;
	static LoginInfor login;
	static int c = 0;
	static Stage reg;
	static Object[] record;
	static TableView table1;
	static TableView table;
	static Object[] customer;
	ArrayList<Object>data=new ArrayList<Object>();
	

	static String name;
	
	static TextField taskText;
	static Object[] recordList;
	static int spar;
	int uttag;
	int balans;
public void start(Stage stage) {
		
	
		GridPane welcomepage=new GridPane();
		Label user=new Label("Användar Namn");
		Label password=new Label("Lösenord");
		
		TextField user1=new TextField();
		user1.setPromptText("Användars Namn");
		user1.setPrefWidth(150);
		
		PasswordField password1=new PasswordField();
		password1.setPromptText("password");
		password1.setPrefWidth(150);
		
		Button button2=new Button("Skaffa konto");
		Button button1= new Button("Login");

		
		HBox users=new HBox();
		users.getChildren().addAll(user, user1);
		users.setPadding(new Insets(30));
		users.setSpacing(20);
		
		HBox pass=new HBox();
		pass.getChildren().addAll(password,password1);
		pass.setPadding(new Insets(30));
		pass.setSpacing(55);
		
		HBox button=new HBox();
		button.getChildren().addAll(button1,button2);
		button.setPadding(new Insets(30));
		button.setSpacing(40);
		button.setAlignment(Pos.CENTER);
		
		
		
	//skaffar vi kunder/starta konto
		
		button2.setOnAction(e -> {
			
			registration(stage);
		});
		
		//här är du i kundens kontor /loga in i konton
		button1.setOnAction(e->{
			if(user1.getText().equals("king")&& (password1.getText().equals("king")) ){
				account(stage);
			}
			else {
				a.setAlertType(AlertType.ERROR);
				a.show();
			}
		
			
		});
		
		
		
		welcomepage.addRow(1,users);
		welcomepage.addRow(3, pass);
		welcomepage.addRow(5, button);
		
		
		Scene scene = new Scene(welcomepage, 350, 300);
		stage.setTitle("Välkomen till Bankomaten");
		stage.setScene(scene);
		stage.show();
	}
	
	public static SplitPane registration(Stage stage) {
		
		
		GridPane grid=new GridPane();
		
		
	
		SplitPane sp=new SplitPane();
		
		Label name=new Label("Name");
		name.setPadding(new Insets(5));
		Label birth=new Label("Date of birth");
		birth.setPadding(new Insets(5));
		Label account=new Label("Desired Account number");
		account.setPadding(new Insets(5));
		Label user=new Label("Username");
		user.setPadding(new Insets(5));
		Label password=new Label("Password");
		password.setPadding(new Insets(5));
		
		
		TextField user1=new TextField();
		user1.setPadding(new Insets(5));
		TextField password1=new TextField();
		password1.setPadding(new Insets(5));
		
		TextField name1=new TextField();
		name1.setPadding(new Insets(5));
		
		TextField birth1=new TextField();
		birth1.setPadding(new Insets(5));
		TextField account1=new TextField();
		account1.setPadding(new Insets(5));
		TextField user2= new TextField();
		user2.setPadding(new Insets(5));
		PasswordField password2=new PasswordField();
		password2.setPadding(new Insets(5));
		Button button=new Button("Submit");
		Button button1=new Button("Delete");
		
		VBox labels=new VBox(35,name,birth,account,user,password,button1);
		labels.setPadding(new Insets(20));
		labels.setMaxWidth(300);
		
		VBox text=new VBox(35,name1,birth1,account1,user1,password1,button);
		text.setPadding(new Insets(20));
		text.setMinWidth(200);
		
		button.setOnAction(e->{
				if(!table1.isEditable()) {
					table1.setEditable(true);
					table1.getItems().add(login=new LoginInfor (name1.getText(),birth1.getText(),account1.getText(),user1.getText(),password1.getText(),LocalDateTime.now()));
					
					name1.setText("");
					birth1.setText("");
					account1.setText("");
					user1.setText("");
					password1.setText("");
					button.setText("Return");
					
					customer=table1.getItems().toArray();
				
				
				}
			});
			
		button1.setOnAction(e->{
			table1.getItems().clear();
		});
		
		
		table1 = new TableView<LoginInfor>();

		TableColumn<LoginInfor, String> column1 = new TableColumn<>("Name");
		column1.setCellValueFactory(new PropertyValueFactory<>("name"));
		column1.prefWidthProperty().bind(table1.widthProperty().multiply(.2));
		TableColumn<LoginInfor, String> column2 = new TableColumn<>("Date Of Birth");
		column2.setCellValueFactory(new PropertyValueFactory<>("dob"));
		column2.prefWidthProperty().bind(table1.widthProperty().multiply(.2));
		TableColumn<LoginInfor, String> column3 = new TableColumn<>("Account");
		column3.setCellValueFactory(new PropertyValueFactory<>("KontoNr"));
		column3.prefWidthProperty().bind(table1.widthProperty().multiply(.2));
		TableColumn<LoginInfor, String> column4 = new TableColumn<>("Username");
		column4.setCellValueFactory(new PropertyValueFactory<>("userName"));
		column4.prefWidthProperty().bind(table1.widthProperty().multiply(.2));
		TableColumn<LoginInfor, String> column5 = new TableColumn<>("Password");
		column5.setCellValueFactory(new PropertyValueFactory<>("passWord"));
		column5.prefWidthProperty().bind(table1.widthProperty().multiply(.2));
		TableColumn<LoginInfor, String> column6 = new TableColumn<>("Date of Creation");
		column6.setCellValueFactory(new PropertyValueFactory<>("dateOfAccount"));
		column6.prefWidthProperty().bind(table1.widthProperty().multiply(.2));

		
		
		table1.getColumns().addAll(column1,column2,column3,column4,column5,column6);
	
	
		
		sp.getItems().addAll(labels,text,table1);
		
		Scene scene = new Scene(sp,700,400);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();
		return sp;
		
	}
	public static GridPane account(Stage stage) {
		
		HBox buttons = new HBox(10);

		Button add = new Button("Submit");
		add.setPrefWidth(90);
		add.setPadding(new Insets(5));
		

		Button finished = new Button("Return");
		finished.setPrefWidth(90);
		finished.setPadding(new Insets(5));
		finished.setOnAction(e->{
			DeOchEncoder returnscene=new DeOchEncoder();
			returnscene.start(stage);
		});
		Button erase=new Button("Delete");
		erase.setPrefWidth(90);
		erase.setPadding(new Insets(5));
		erase.setOnAction(e->{
			table.getItems().clear();
		});
	
		buttons.getChildren().addAll(erase,add, finished);
		buttons.setPadding(new Insets(10));
		

		

		

		table = new TableView<Account>();
		
		
		
		TableColumn<Account, String> column4 = new TableColumn<Account,String>("IncomingBalance");
		column4.setCellValueFactory(new PropertyValueFactory<Account,String>("balans1"));
		column4.prefWidthProperty().bind(table.widthProperty().multiply(.2));

		TableColumn<Account, String> column1 = new TableColumn<Account,String>("Spara");
		column1.setCellValueFactory(new PropertyValueFactory<Account,String>("spar"));
		column1.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		
	

		TableColumn<Account, String> column2 = new TableColumn<Account,String>("Uttag");
		column2.setCellValueFactory(new PropertyValueFactory<Account,String>("uttag"));
		column2.prefWidthProperty().bind(table.widthProperty().multiply(.3));
		
		TableColumn<Account, String> column3 = new TableColumn<Account,String>("OutgoingBalans");
		column3.setCellValueFactory(new PropertyValueFactory<Account,String>("balans"));
		column3.prefWidthProperty().bind(table.widthProperty().multiply(.3));
		
		TableColumn<Account, String> column5 = new TableColumn<Account,String>("Date");
		column5.setCellValueFactory(new PropertyValueFactory<Account,String>("date"));
		column5.prefWidthProperty().bind(table.widthProperty().multiply(.3));
	
		table.getColumns().addAll(column4,column1, column2, column3,column5);
		table.setEditable(true);
		
		Label sparaLabel = new Label("Spara");
		sparaLabel.setFont(new Font("New Times Romans", 13));
		sparaLabel.setPadding(new Insets(10));
		sparaLabel.minWidth(100);

		Label uttagLabel = new Label("Uttag");
		uttagLabel.setFont(new Font("New Times Romans", 13));
		uttagLabel.setPadding(new Insets(10));

		Label balansLabel = new Label("Balans");
		balansLabel.setFont(new Font("New Times Romans", 13));
		balansLabel.setPadding(new Insets(10));
		
		Label balansLabel1=new Label("Current Balance");
		
		
		TextField text=new TextField("");
		text.setPadding(new Insets(10));
		text.setPromptText("Write down the amount on your outgoing balance, if none write 0(zero)");
		//text.maxWidth(300);

		TextField firstText = new TextField("");
		firstText.setPadding(new Insets(10));
		firstText.setPromptText("How much do you want to save, if nothing write 0(zero)");
		firstText.maxWidth(100);
	
		
		TextField secondText = new TextField("");
		secondText.setPadding(new Insets(10));
		secondText.setPromptText("How much do you want to withdraw, if nothing write 0(zero)");
		secondText.setText("");
		secondText.maxWidth(100);

		TextField thirdText = new TextField("");
		thirdText.setPadding(new Insets(10));
		thirdText.setPromptText("Balance");
		thirdText.maxWidth(100);
		
		
		add.setOnAction(e->{
			
			
			double balans1=Double.parseDouble(text.getText());
			double spar=Double.parseDouble(firstText.getText());
			double uttag =Double.parseDouble(secondText.getText());
	
			double balans3=balans1+spar-uttag;
			if(balans3<0) {
				a.setAlertType(AlertType.ERROR);
				a.show();
			
			}if(text.getText().equals("")||firstText.getText().equals("")||secondText.getText().equals("")) {
				a.setAlertType(AlertType.ERROR);
				a.show();
			}
			
			else{
				
				String balans4=String.valueOf(balans3);
				System.out.println("hello");
				thirdText.setText(balans4);
			
				
				table.getItems().add(kund=new Account(text.getText(),firstText.getText(),secondText.getText(),thirdText.getText(),LocalDateTime.now()));
				transactions= table.getItems().toArray();
				System.out.println(transactions.toString());
				
				text.setText("");
				firstText.setText("");
				secondText.setText("");
				thirdText.setText("");
				
				
			
			}
				
			
			
		});
		
		GridPane mainScene = new GridPane();

		mainScene.add(buttons, 5, 15, 1, 1);
		mainScene.add(balansLabel1, 0, 11, 1, 1);
		mainScene.add(sparaLabel, 0, 12, 1, 1);
		mainScene.add(uttagLabel, 0, 13, 1, 1);
		mainScene.add(balansLabel, 0,14,1,1);

		mainScene.add(text, 3, 11, 6, 1);
		mainScene.add(firstText, 3, 12, 6, 1);
		mainScene.add(secondText, 3, 13, 6, 1);
		mainScene.add(thirdText,3,14,1,1);

		mainScene.add(table, 0, 0, 10, 10);
		mainScene.setPadding(new Insets(5));
		Scene scene = new Scene(mainScene, 600, 400);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();
		return mainScene;
}
	public void serialize() {
		
		
		XMLEncoder encoder = null;
		try {
			encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
			System.out.println("File stream open and encoder created");
			encoder.writeObject(data);
			System.out.println("object written");
			encoder.flush();
			encoder.close();
			System.out.println("file stream closed");
			

		} catch (FileNotFoundException fileNotFound) {
			System.out.println("ERROR: While creating or opening the File Staff.xml");

		}	
	}


	public static void main(String[] args) {
		launch(args);
	}
}*/
//bottom of main page starts here
/*	
public static SplitPane registration(Stage stage) {
		
		
		GridPane grid=new GridPane();
		
		
	
		SplitPane sp=new SplitPane();
		
		Label name=new Label("Name");
		name.setPadding(new Insets(5));
		Label birth=new Label("Date of birth");
		birth.setPadding(new Insets(5));
		Label account=new Label("Desired Account number");
		account.setPadding(new Insets(5));
		Label user=new Label("Username");
		user.setPadding(new Insets(5));
		Label password=new Label("Password");
		password.setPadding(new Insets(5));
		
		
		
		
		TextField name1=new TextField();
		name1.setPadding(new Insets(5));
		
		TextField birth1=new TextField();
		birth1.setPadding(new Insets(5));
		TextField account1=new TextField();
		account1.setPadding(new Insets(5));
		TextField user1= new TextField();
		user1.setPadding(new Insets(5));
		PasswordField password1=new PasswordField();
		password1.setPadding(new Insets(5));
		Button button=new Button("Submit");
		Button button1=new Button("Delete");
		
		VBox labels=new VBox(35,name,birth,account,user,password,button1);
		labels.setPadding(new Insets(20));
		labels.setMaxWidth(300);
		
		VBox text=new VBox(35,name1,birth1,account1,user1,password1,button);
		text.setPadding(new Insets(20));
		text.setMinWidth(200);
		
		button.setOnAction(e->{
				if(!table1.isEditable()) {
					table1.setEditable(true);
					
					table1.getItems().add(login=new LoginInfor(name1.getText(),birth1.getText(),account1.getText(),user1.getText(),password1.getText(),LocalDateTime.now()));
					name1.setText("");
					birth1.setText("");
					account1.setText("");
					user1.setText("");
					password1.setText("");
					button.setText("Return");
					accounts = table1.getItems().toArray();
					data.add(accounts);
					save();
			
				}else {
					Main returnscene=new Main();
					returnscene.start(stage);
					
				}
			
		});
		button1.setOnAction(e->{
			table1.getItems().clear();
		});
		
		
		table1 = new TableView<LoginInfor>();

		TableColumn<LoginInfor, String> column1 = new TableColumn<>("Name");
		column1.setCellValueFactory(new PropertyValueFactory<>("name"));
		column1.prefWidthProperty().bind(table1.widthProperty().multiply(.3));
		TableColumn<LoginInfor, String> column2 = new TableColumn<>("Date Of Birth");
		column2.setCellValueFactory(new PropertyValueFactory<>("dob"));
		column2.prefWidthProperty().bind(table1.widthProperty().multiply(.3));
		TableColumn<LoginInfor, String> column3 = new TableColumn<>("Account");
		column3.setCellValueFactory(new PropertyValueFactory<>("KontoNr"));
		column3.prefWidthProperty().bind(table1.widthProperty().multiply(.3));
		TableColumn<LoginInfor, String> column4 = new TableColumn<>("Username");
		column4.setCellValueFactory(new PropertyValueFactory<>("userName"));
		column4.prefWidthProperty().bind(table1.widthProperty().multiply(.3));
		TableColumn<LoginInfor, String> column5 = new TableColumn<>("Password");
		column5.setCellValueFactory(new PropertyValueFactory<>("passWord"));
		column5.prefWidthProperty().bind(table1.widthProperty().multiply(.3));
		TableColumn<LoginInfor, String> column6 = new TableColumn<>("Date of Creation");
		column6.setCellValueFactory(new PropertyValueFactory<>("dateOfAccount"));
		column6.prefWidthProperty().bind(table1.widthProperty().multiply(.3));

		
		table1.getColumns().addAll(column1,column2,column3,column4,column5,column6);
		
		for(Object x: accounts) {
			table1.getItems().add(x);
		}
		
		sp.getItems().addAll(labels,text,table1);
		
		Scene scene = new Scene(sp,700,400);
		stage.setTitle("Fill in your personal details");
		stage.setScene(scene);
		stage.show();
		return sp;
		
		
	}
	public static GridPane account(Stage stage) {
		
		
		
		Label label=new Label("Ingångs balans");
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
		column1.setCellValueFactory(new PropertyValueFactory<Account,String>("spar"));
		column1.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		
	
	
		TableColumn<Account, String> column2 = new TableColumn<>("Uttag");
		column2.setCellValueFactory(new PropertyValueFactory<Account,String>("uttag"));
		column2.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		
		TableColumn<Account, String> column3 = new TableColumn<>("OutGoingBalance");
		column3.setCellValueFactory(new PropertyValueFactory<Account,String>("balans"));
		column3.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		
		TableColumn<Account, String> column4 = new TableColumn<>("IncomingBalance");
		column4.setCellValueFactory(new PropertyValueFactory<Account,String>("balans1"));
		column4.prefWidthProperty().bind(table.widthProperty().multiply(.2));
		
		TableColumn<Account, String> column5 = new TableColumn<>("Date");
		column5.setCellValueFactory(new PropertyValueFactory<Account,String>("date"));
		column5.prefWidthProperty().bind(table.widthProperty().multiply(.3));
	
		table.getColumns().addAll(column4,column1, column2, column3,column5);
		
		
		for(Object y:transactions) {
			table.getItems().add(y);
		}

		
		HBox buttons = new HBox(10);
	
		Button add = new Button("Submit");
		add.setPrefWidth(150);
		add.setPadding(new Insets(5));
		
		add.setOnAction(e->{
			
			
			double balans1=Double.parseDouble(text.getText());
			double spar=Double.parseDouble(firstText.getText());
			double uttag =Double.parseDouble(secondText.getText());
	
			double balans3=balans1+spar-uttag;
			if(balans3<0) {
				a.setAlertType(AlertType.ERROR);
				a.show();
			
			}
			
			else{
				
				String balans4=String.valueOf(balans3);
				thirdText.setText(balans4);
			
				
				table.getItems().add(kund=new Account(text.getText(),firstText.getText(),secondText.getText(),thirdText.getText(),LocalDateTime.now()));
				transactions= table.getItems().toArray();
				System.out.println(transactions.toString());
				data.add(transactions);
				//System.out.println(data.toString());
				text.setText("");
				firstText.setText("");
				secondText.setText("");
				thirdText.setText("");
			
				save1();
			}
		});
		
		Button finished = new Button("Return");
		finished.setPrefWidth(150);
		finished.setPadding(new Insets(5));
		finished.setOnAction(e->{
			
			Main returnscene=new Main();
			returnscene.start(stage);
		});
		Button delete=new Button("Delete");
		delete.setPrefWidth(150);
		delete.setPadding(new Insets(5));
		delete.setOnAction(e->{
			table.getItems().clear();
		});
		
	
		buttons.getChildren().addAll(delete,add, finished);
		buttons.setPadding(new Insets(10));
		
		GridPane mainScene = new GridPane();
	
		mainScene.add(buttons, 5, 15, 1, 1);
		mainScene.add(label, 0, 11, 1, 1);
		mainScene.add(firstLabel, 0, 12, 1, 1);
		mainScene.add(secondLabel, 0, 13, 1, 1);
		mainScene.add(thirdLabel, 0, 14, 1, 1);
	
		mainScene.add(text, 3, 11,6,1);
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
	
	public static void save()
	{
		XMLEncoder encoder = null;
	try {
		encoder = new XMLEncoder(new BufferedOutputStream(new FileOutputStream(SERIALIZED_FILE_NAME)));
		System.out.println("File stream open and encoder created");
		encoder.writeObject(accounts);
		System.out.println("object written");
		encoder.flush();
		encoder.close();
		System.out.println("file stream closed");
		

	} catch (FileNotFoundException fileNotFound) {
		System.out.println("ERROR: While creating or opening the File Staff.xml");

	}
	}
	public static void save1()
	{
		//System.out.println(data.toString());
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
	public static void decode() {
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
			System.out.println("File stream opened XMDecoder created");
			
		}catch(FileNotFoundException e) {
			System.out.println("ERROR: xml file not found");
		}
		accounts=(Object[]) decoder.readObject();
		decoder.close();
		System.out.println("file read from accounts xml");

		System.out.println(accounts.toString());
	
	}
	public static void decode1() {
		XMLDecoder decoder1=null;
		try {
			decoder1=new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME1)));
			System.out.println("File stream opened XMDecoder created");
			
		}catch(FileNotFoundException e) {
			System.out.println("ERROR: xml file not found");
		}
		transactions=(Object[]) decoder1.readObject();
		decoder1.close();
		System.out.println("file read from transaction xml");
		
		System.out.println(transactions.toString());
	}
	
	
	
	public static void decode() {
		XMLDecoder decoder=null;
		try {
			decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(SERIALIZED_FILE_NAME)));
			System.out.println("File stream opened XMDecoder created");
			
		}catch(FileNotFoundException e) {
			System.out.println("ERROR: xml file not found");
		}
		deserialise=(ArrayList<Object>) decoder.readObject();
		decoder.close();
		System.out.println("file read");
		//här tildelar man alla object til sin ursprung object array
		accounts=(Object[])deserialise.get(0);
		transactions=(Object[]) deserialise.get(1);
		System.out.println(accounts.toString());
		System.out.println(transactions.toString());
		//lada upp data på tabellerna
		for(Object x: accounts) {
			table1.getItems().add(x);
		}
		for(Object y:transactions) {
			table.getItems().add(y);
		}
	}


*/
	