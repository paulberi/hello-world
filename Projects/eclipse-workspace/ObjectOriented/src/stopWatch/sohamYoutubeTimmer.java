package stopWatch;

import java.util.Timer;
import java.util.TimerTask;

public class sohamYoutubeTimmer {
	
	int MillisecondsPassed =0;
	int secondsPassed=0;
	int minutesPassed=0;
	
	Timer myTimer =new Timer();
	TimerTask task= new TimerTask() {
		public void run() {
			MillisecondsPassed=MillisecondsPassed+1;
			secondsPassed=(MillisecondsPassed/1000)%60;
			minutesPassed=(MillisecondsPassed/60000)%60;
			String milliseconds_string=String.format("%02d", MillisecondsPassed);
			String seconds_string=String.format("%02d", secondsPassed);
			String minutes_string=String.format("%02d", minutesPassed);
			System.out.println(minutes_string+":"+seconds_string+":"+milliseconds_string);
			
			System.out.println("Seconds passed: "+MillisecondsPassed);
		}
		public void run1() {
			secondsPassed++;
			System.out.println("Seconds passed: "+secondsPassed);
		}
		public void run2() {
			minutesPassed++;
			System.out.println("Minutes Passed: "+minutesPassed);
		}
		
	};
	public void start () {
		myTimer.scheduleAtFixedRate(task, 1, 1);
		
	}
	public void start1() {
		myTimer.scheduleAtFixedRate(task, 1000, 1000);
	}
	public void start2() {
		myTimer.scheduleAtFixedRate(task, 60000, 60000);
	}

}
