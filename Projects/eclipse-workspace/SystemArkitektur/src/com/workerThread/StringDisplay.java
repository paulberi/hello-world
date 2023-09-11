package com.workerThread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class StringDisplay {
	Queue<String> display=new ConcurrentLinkedQueue<>();
	
	private static StringDisplay stringdisplay=new StringDisplay();
	
	public static StringDisplay getInstance() {
		return stringdisplay;
	}
	public synchronized void addDisplay(String displays) {
		display.add(displays);
		
	}
	public synchronized String getDisplay() {
		return display.poll();
	}

}
