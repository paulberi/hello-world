package timer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

public class Timers  {
	
		static Timeline timeline;
		Button button1;
		Button button2;
		static long millis;
		static long seconds;
		static long mins;
		static boolean started= false;
		static Text text=new Text("00:00,0000");
		
		
		public static void timer(Timeline timeline) {
			
			timeline=new Timeline();
			DateTimeFormatter timeFormatter= DateTimeFormatter.ofPattern("mm:ss:SSS");
			//timeline.setCycleCount(Timeline.INDEFINITE);
			//timeline.setAutoReverse(false);
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
			
			/*
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
					}else {
						stops();
						button1.setText("start");
						started=false;
					}
					
				}
				
			});
			gp.add(text, 0, 0, 1, 1);
			gp.add(button1, 0,1,1,1);
			return gp;*/
			
			}
		public static void start() {
			timeline.play();
		}
		public static void stops() {
			timeline.stop();
		}
		
		}
		
