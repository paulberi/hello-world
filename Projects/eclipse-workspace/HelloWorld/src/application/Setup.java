package application;
import com.sun.glass.ui.Timer;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class Setup extends Timmer{

	public static BorderPane frameAndTimerLauncher() {
		
		BorderPane bp= new BorderPane();
		GridPane gp = new GridPane();
		
		text1.setPrefSize(350, 70);
		text1.setFont(Font.font("Times New Romans", 30));
		text1.setPromptText("00:00:00:0000000");
		
		millistring=String.format("%7d", milliseconds);
		secondstring=String.format("%2d", seconds);
		minutestring=String.format("%2d", minutes);
		hourstring=String.format("%2d", hours);
		text1.setText(hourstring+":"+minutestring+":"+secondstring+":"+millistring);
		
		gp.setPadding(new Insets(10,10,10,10));
		gp.getChildren().add(text1);
		
		Button button1= new Button("Start");
		button1.setPadding(new Insets(10));
		button1.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent timer) {
				// TODO Auto-generated method stub
				if(timer.getSource()==button1) {
					start();
					if(started==false) {
						started=true;
						button1.setText("Stop");
						start();
					}
					else {
						started=false;
						button1.setText("Start");
						stop();
					}
				}
			}
			
		});
		
		Button button2=new Button("Reset");
		button2.setPadding(new Insets(10));
		button2.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent timer) {
				// TODO Auto-generated method stub
				if(timer.getSource()==button2) {
					reset();
				}
			}
			
		});
		
		ToolBar buttonBar=new ToolBar();
		buttonBar.getItems().addAll(button1,button2);
		
		TextArea text2= new TextArea();
		text2.setPromptText("Inga sparade tider");
		text2.setText("");
		text2.setPadding(new Insets(10));
		
		
		bp.setTop(gp);
		bp.setCenter(buttonBar);
		bp.setBottom(text2);
		
		
		
		
		return bp;
		
		
	}
	/*
	   startButton.setText("Stop");
       DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("mm:ss,SSS");
       tm = new Timeline();
       tm.setCycleCount(Animation.INDEFINITE);
       KeyFrame myFrame = new KeyFrame(Duration.millis(1), (e) -> {
           myTime = myTime.plus(1, ChronoUnit.MILLIS);
           showTimer.setText(myTime.format(timeFormatter).toString());
       });
       tm.getKeyFrames().add(myFrame);
       tm.play();
   }

   public void stopTimer() {
       tm.stop();
       startButton.setText("Start");
       myTime = LocalTime.of(0, 0, 0, 0);
       showTimer.setText("00:00,000");

   }*/
}
