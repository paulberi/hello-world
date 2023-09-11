package application;
	
import javafx.application.Application;

import javafx.stage.Stage;
import javafx.scene.Scene;


public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) throws Exception {
			try {
			
			Scene scene= new Scene(Setup.frameAndTimerLauncher(),350,400);
			
			stage.setTitle("Stoppuret");
			
			stage.setScene(scene);
			
			stage.show();
			}catch(NullPointerException e) {
					System.out.println("NullPointerException thrown!");
				}
			}

			// Using if-else condition:
			static void bar(String x){
				if(x == null)
					System.out.println("First character: " + x.charAt(0));
				else
					System.out.println("NullPointerException thrown!");
		
		
		
	}	
}