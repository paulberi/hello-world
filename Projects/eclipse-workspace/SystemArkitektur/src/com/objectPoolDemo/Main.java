package com.objectPoolDemo;

public class Main {

    public static void main(String[] args) {
	    WorkerPool workerPool = new WorkerPool(5);
        ServiceWorker worker1 = workerPool.getObject();
        ServiceWorker worker2 = workerPool.getObject();
        ServiceWorker worker3 = workerPool.getObject();
        ServiceWorker worker4 = workerPool.getObject();
        ServiceWorker worker5 = workerPool.getObject();
        System.out.println(worker1.createContent());
        workerPool.releaseObject(worker1);
        System.out.println(worker2.createContent());
        workerPool.releaseObject(worker2);
        System.out.println(worker3.createContent());
        workerPool.releaseObject(worker3);
        System.out.println(worker4.createContent());
        workerPool.releaseObject(worker4);
        System.out.println(worker5.createContent());
        workerPool.releaseObject(worker5);
    }
}
