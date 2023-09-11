package com.company;

public class Main {

    public static void main(String[] args) {
	    // Datafile without proxy
        System.out.println(new CSVDataFile("example_path").getData());

        // Datafile with proxy
        System.out.println(new DataFileProxy("example_path").getData());
    }
}
