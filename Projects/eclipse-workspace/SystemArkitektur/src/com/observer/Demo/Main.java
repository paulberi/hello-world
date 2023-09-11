package com.observer.Demo;

public class Main {

    public static void main(String[] args) {
        // Setup and register of listeners
        TrafficControl trafficControl = new TrafficControl();
        Vehicle vehicle1 = new Vehicle();
        vehicle1.addPropertyChangeListener(trafficControl);
        Vehicle vehicle2 = new Vehicle();
        vehicle2.addPropertyChangeListener(trafficControl);
        Vehicle vehicle3 = new Vehicle();
        vehicle3.addPropertyChangeListener(trafficControl);

        // Change values
        vehicle1.setSpeed(100);
        vehicle2.setDirection("W");
        vehicle3.setDirection("E");
    }
}
