package com.DAOdemo;

public class Car {
    private long id;
    private String color;
    private String brand;
    private double maxSpeed;

    public Car(long  id, String color, String brand, double maxSpeed) {
        this.id = id;
        this.color = color;
        this.brand = brand;
        this.maxSpeed = maxSpeed;
    }

    public Car(String color, String brand, double maxSpeed) {
        this.id = id;
        this.color = color;
        this.brand = brand;
        this.maxSpeed = maxSpeed;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
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
