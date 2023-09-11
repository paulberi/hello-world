package com.proxy;

public class Main {

    public static void main(String[] args) {
	    // Datafile without proxy
        System.out.println(new CSVDataFile("example_path").getData());

        double[] data=new CSVDataFile("example_path").getData();
        for(int i=0;i<data.length; i++) {
        	System.out.println(data[i]);
        }
        // Datafile with proxy
        System.out.println(new DataFileProxy("example_path").getData());
    }
}
