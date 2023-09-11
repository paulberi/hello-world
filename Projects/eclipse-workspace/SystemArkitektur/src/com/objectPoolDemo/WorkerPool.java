package com.objectPoolDemo;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class WorkerPool {
    Queue<ServiceWorker> workers = new ConcurrentLinkedQueue<>();

    public WorkerPool(int numWorkers) {
        for(int i=0;i<numWorkers;i++) {
            workers.add(new ServiceWorker(i));
        }
    }

    public ServiceWorker getObject() {
        System.out.println("Current number of workers before object taken: " + this.workers.size());
        ServiceWorker worker = workers.poll();
        if(worker!=null) {
            System.out.println("Worker " + worker.getId() + " taken.");
        }
        return worker;
    }

    public void releaseObject(ServiceWorker worker) {
        this.workers.add(worker);
        System.out.println("Current number of workers after object released: " + this.workers.size());
        System.out.println("Worker " + worker.getId() + " released.");
    }

}
