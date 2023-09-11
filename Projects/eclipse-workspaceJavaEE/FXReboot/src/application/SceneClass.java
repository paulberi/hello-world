package application;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class SceneClass {
	
	Stage stage;
	
	public SceneClass(Stage stage) {
		this.stage=stage;
	}
	
	public void login() {
		GridPane grid=new GridPane();
		grid.setMaxWidth(500);
		grid.setMaxHeight(500);
		grid.setAlignment(Pos.CENTER);
		grid.setHgap(10);
		grid.setVgap(10);
		
		TextField userName=new TextField();
		userName.setPadding(new Insets(10, 10, 10, 10));
		PasswordField password=new PasswordField();
		password.setPadding(new Insets(10, 10, 10, 10));
		Button submit= new Button("Login");
		submit.setPadding(new Insets(10, 10, 10, 10));
		
		grid.add(userName, 0, 0);
		grid.add(password, 0, 1);
		grid.add(submit, 0, 2);
		
		submit.setOnAction(e->{
			LoggedIn userInfor=new LoggedIn(stage);
			String userName1=userName.getText();
			String password1=password.getText();
			System.out.println(userName1+" "+ password1);
			userInfor.loggedInUser(userName1, password1);
		});
		
		Scene scene=new Scene(grid);
		
		stage.setWidth(500);
		stage.setHeight(500);
		stage.setScene(scene);
		stage.show();
	}


}
