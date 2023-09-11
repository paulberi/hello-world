package com.composite.Demo;

import java.util.Random;

public class SensorNode extends Node {
    private double temperature;

    public SensorNode(String name) {
        super(name);
        this.temperature = new Random().nextDouble()*45;
    }

    @Override
    double getTemperature() {
        return this.temperature;
    }
}
