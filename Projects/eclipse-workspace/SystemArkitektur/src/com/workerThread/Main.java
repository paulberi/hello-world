package com.workerThread;

public class Main {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		JobStringQueue.getInstance().addJob(new Job("String 1",StringWorkerThread.createString()));
		JobStringQueue.getInstance().addJob(new Job("String 2",StringWorkerThread.createString()));
		JobStringQueue.getInstance().addJob(new Job("String 3",StringWorkerThread.createString()));
		
		new Thread(new StringWorkerThread()).start();

		System.out.println(StringDisplay.getInstance().getDisplay());
		Thread.sleep(1000);
		System.out.println(StringDisplay.getInstance().getDisplay());
		Thread.sleep(6000);
		System.out.println(StringDisplay.getInstance().getDisplay());
		System.out.println(StringDisplay.getInstance().getDisplay());
	}

}
