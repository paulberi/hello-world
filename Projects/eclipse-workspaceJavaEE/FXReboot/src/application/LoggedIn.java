package application;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class LoggedIn {
	
	Stage stage;
	
	public LoggedIn(Stage stage){
		this.stage=stage;
	}
	
	public void loggedInUser(String userName, String password) {
		System.out.println(userName+" "+password);
		
		GridPane grid=new GridPane();
		grid.setAlignment(Pos.CENTER);
		Label userInfo=new Label();
		
		if(userName.equals("Paul") && password.equals("beri")) {
			
			System.out.println(userName+" "+password);
			userInfo.setText("Username: "+userName+
					"\nPassword: XXXX"
					+ "\nis currently loggedIn");
		}
		Button button=new Button("Close");
		grid.add(userInfo, 0, 0);
		grid.add(button, 0, 1);
		
		button.setOnAction(e->{
			TextField ts=new TextField();
			ts.setPromptText("Thank you for testing");
			Scene scene=new Scene(ts);
			stage.setWidth(500);
			stage.setHeight(500);
			stage.setScene(scene);
			stage.show();
		});
		Scene scene=new Scene(grid);
		stage.setWidth(500);
		stage.setHeight(500);
		stage.setScene(scene);
		stage.show();
	}

}
