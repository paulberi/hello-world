package com.objectPoolDemo;

import java.util.Random;

public class ServiceWorker {
    private int id;

    public ServiceWorker(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String createContent() {
        String result = "";
        Random random = new Random();
        for(int i=0;i<10000000;i++) {
            if(i%1000000==0) {
                result += random.nextInt(10);
            }
        }
        return result;
    }
}
