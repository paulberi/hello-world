package com.composite.Demo;

public class Main {

    public static void main(String[] args) {
	    // Add regions
        Node root = new RegionNode("Sverige");
        Node regionA = new RegionNode("Norrland");
        root.addChild(regionA);
        Node regionAA = new RegionNode("Sundsvall");
        regionA.addChild(regionAA);
        Node regionAB = new RegionNode("Luleå");
        regionA.addChild(regionAB);

        // Add sensors
        Node sensorA = new SensorNode("Sensor A");
        Node sensorB = new SensorNode("Sensor B");
        Node sensorC = new SensorNode("Sensor C");
        Node sensorD = new SensorNode("Sensor D");
        Node sensorE = new SensorNode("Sensor E");

        regionAA.addChild(sensorA); // Sundsvall
        regionAA.addChild(sensorB); // Sundsvall
        regionAA.addChild(sensorC); // Sundsvall
        regionAB.addChild(sensorD); // Luleå
        regionAB.addChild(sensorE); // Luleå


        System.out.println("Sensor A temperature: " + sensorA.getTemperature());
        System.out.println("Sensor B temperature: " + sensorB.getTemperature());
        System.out.println("Sensor C temperature: " + sensorC.getTemperature());
        System.out.println("Region AA (Sundsvall) temperature: " + regionAA.getTemperature());

        System.out.println("Sensor D temperature: " + sensorD.getTemperature());
        System.out.println("Sensor E temperature: " + sensorE.getTemperature());
        System.out.println("Region AB (Luleå) temperature: " + regionAB.getTemperature());

        System.out.println("Region Sverige temperature: " + root.getTemperature());


        // Use of findByName recursive method
        System.out.println("Sundsvall by name temperature: " + root.getSensorByName("Sundsvall").getTemperature());
        System.out.println("Sensor A by name temperature: " + root.getSensorByName("Sensor A").getTemperature());


    }
}
