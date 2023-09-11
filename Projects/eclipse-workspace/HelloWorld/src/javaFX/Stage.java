package javaFX;

import java.util.Timer;
import java.util.TimerTask;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

public class Stage extends Application {
	static int seconds = 0;
	static TimerTask task;

	public static void main(String args[]) {
		launch(args);
	}

	public static int start() {
	Timer timer=new Timer();
	
	task=new TimerTask() {

		@Override
		public void run() {
			// TODO Auto-generated method stub
			timer.scheduleAtFixedRate(task, 1000, 1000);
			seconds++;
		}
};
	return seconds =seconds+1;}

	@Override
	public void start(javafx.stage.Stage stage) throws Exception {
		// TODO Auto-generated method stub
		stage.setTitle("Timmer");
		StackPane pane = new StackPane();
		stage.setScene(new Scene(pane, 300, 300));
		Button button = new Button();
		button.setText("Click");
		button.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println(start());
				

			}
		});
		pane.getChildren().add(button);
		stage.show();

	}
	
}
