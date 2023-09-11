package com.builder;

public class Car {
    private String color;
    private String brand;
    private double maxSpeed;

    public Car() {

    }

    public Car(String color, String brand, double maxSpeed) {
        this.color = color;
        this.brand = brand;
        this.maxSpeed = maxSpeed;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getMaxSpeed() {
        return maxSpeed;
    }

    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }
}
