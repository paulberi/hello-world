package timer;


import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;

public class Main extends Application{
	

	boolean started = true;
	Button button;
	Button button1;
	static long millis;
	static long seconds;
	static long mins;
	static Text timer1;
	public static void main(String[] args) {
		
		
		launch(args);
	}
	static Timeline timeline;;
	static Text text=new Text("00:00,0000");
	
	
	public static void timer() {
		
		timeline=new Timeline();
		timeline = new Timeline(new KeyFrame(Duration.millis(1), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (millis == 1000) {
					seconds++;
					millis = 0;
				}
				if (seconds == 60) {
					mins++;
					seconds = 0;
				}
				text.setText((((mins / 10) == 0) ? "0" : "") + mins + ":" + (((seconds / 10) == 0) ? "0" : "") + seconds
						+ ":" + (((millis / 10) == 0) ? "00" : (((millis / 100) == 0) ? "0" : ""))
						+ millis++);
				
				System.out.println((((mins / 10) == 0) ? "0" : "") + mins + ":" + (((seconds / 10) == 0) ? "0" : "") + seconds
						+ ":" + (((millis / 10) == 0) ? "00" : (((millis / 100) == 0) ? "0" : ""))
						+ millis++);
				}
			}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.setAutoReverse(false);
		
		
	}
	public static  void start() {
		timeline.play();
	}
	public static  void stops() {
		timeline.stop();
	}
	
	Timers time=new Timers();
	
		
	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		//	timer1.setFont(Font.font("Times New Romans", 20));

		
		
		try {
			GridPane gp=new GridPane();
			Button button1=new Button("start");
			button1.setOnAction(new EventHandler<ActionEvent>() {

				@Override
				public void handle(ActionEvent arg0) {
					// TODO Auto-generated method stub
					if (!started) {
						start();
						started=true;
						button1.setText("stop");
					}
					else {
						stops();
						button1.setText("start");
						started=false;
					}
					
				}
				
			});
			gp.add(text, 0, 0, 1, 1);
			gp.add(button1, 0,1,1,1);
			BorderPane bp=new BorderPane();
			Scene scene = new Scene(gp, 510, 500);
		

			stage.setTitle("Person Register");

			stage.setScene(scene);

			stage.show();
		} catch (NullPointerException e) {
			System.out.println("NullPointerException thrown!");
		}
	}
}
