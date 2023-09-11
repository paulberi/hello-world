package com.workerThreadPool;

public class WorkerThread implements Runnable{
	
	private String message;
	
	public WorkerThread(String message) {
		this.message=message;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println(Thread.currentThread().getName()+"(Start) Message = "+message);
		
		processmessage();
		
		System.out.println(Thread.currentThread().getName()+"(Start) Message = "+"(End)");
	}
	

	private void processmessage() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
