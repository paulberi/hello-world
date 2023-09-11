package application;

import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class Timmer{
	
	
	static boolean started=false;
	static int milliseconds;
	static int seconds;
	static int minutes;
	static int hours;
	
	static String millistring;
	static String secondstring;
	static String minutestring;
	static String hourstring;
	static TextArea text1=new TextArea();
	
	static Timer mytimer= new Timer(1, new ActionListener() {

		@Override
		public void actionPerformed(ActionEvent timer) {
			// TODO Auto-generated method stub
			milliseconds=milliseconds+1;
			seconds=(milliseconds/1000)%60;
			minutes=(milliseconds/60000)%60;
			hours=(milliseconds/3600000);
			
			millistring=String.format("%7d", milliseconds);
			secondstring=String.format("%2d", seconds);
			minutestring=String.format("%2d", minutes);
			hourstring=String.format("%2d", hours);
			
			text1.setText(hourstring+":"+minutestring+":"+secondstring+":"+millistring);
			System.out.println(hourstring+":"+minutestring+":"+secondstring+":"+millistring);
		}
		
	});
	
	public static void stop() {
		mytimer.stop();
	}
	public static void start() {
		mytimer.start();
	}
	public static void reset() {
		mytimer.stop();
		milliseconds=0;
		seconds=0;
		minutes=0;
		hours=0;
		
		millistring = String.format("%2d", milliseconds);
		secondstring=String.format("%2d", seconds);
		minutestring= String.format("%2d", minutes);
		hourstring=String.format("%2d", hours);
		text1.setText(hourstring+":"+minutestring+":"+secondstring+":"+millistring);
	}

}
