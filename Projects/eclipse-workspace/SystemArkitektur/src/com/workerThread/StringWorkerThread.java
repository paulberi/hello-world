package com.workerThread;

import java.util.Random;

import adapterPatternOvning.Logger;

public class StringWorkerThread implements Runnable {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true) {
			Job job= JobStringQueue.getInstance().getJob();
			if(job!=null) {
				System.out.println("String generation in progress");
				
				String text=job.getTitle()+ "\n"+job.getText();
				StringDisplay.getInstance().addDisplay(text);
				
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public static String createString() {
		// TODO Auto-generated method stub
		String result="";
		for(int i=0; i<10;i++) {
			result+=new Random().nextInt(10);
		}
		return result;
	}
}
