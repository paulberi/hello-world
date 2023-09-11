package com.workerThread;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class JobStringQueue {
	
	Queue<Job>jobs=new ConcurrentLinkedQueue<>();
	
	private static JobStringQueue queue= new JobStringQueue();
	
	public static JobStringQueue getInstance() {
		return queue;
	}
	
	public synchronized void addJob(Job job) {
		System.out.println("number of jobs in queue before job added "+ this.jobs.size());
		jobs.add(job);
	}
	public synchronized Job getJob() {
		System.out.println("number of jobs in queue before removal "+ this.jobs.size());
		return this.jobs.poll();
	}
	

}
