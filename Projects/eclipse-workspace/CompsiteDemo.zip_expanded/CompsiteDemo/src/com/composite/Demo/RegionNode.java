package com.composite.Demo;

public class RegionNode extends Node {

    public RegionNode(String name) {
        super(name);
    }

    @Override
    double getTemperature() {
        double temperature = 0.0;
        for(Node node : this.getChildren()) {
            temperature += node.getTemperature();
        }
        return temperature/this.getChildren().size();
    }
}
