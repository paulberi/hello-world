package com.observer.Demo;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Vehicle {
    private double speed = 50;
    private String direction = "S";

    private PropertyChangeSupport propertyChangeSupport;

    public Vehicle() {
        this.propertyChangeSupport = new PropertyChangeSupport(this);
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        // Report change
        double oldSpeed = this.speed;
        this.speed = speed;
        this.propertyChangeSupport.firePropertyChange("Speed", oldSpeed, this.speed);
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        // Report change
        String oldDirection = this.direction;
        this.direction = direction;
        this.propertyChangeSupport.firePropertyChange("Direction", oldDirection, this.direction);
    }
}
