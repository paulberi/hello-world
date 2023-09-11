package com.DTODemo;

import java.io.Serializable;

public class CarDTO implements Serializable {
    private long id;
    private String color;
    private String brand;
    private double maxSpeed;

    public CarDTO(Car car) {
        this.id = car.getId();
        this.color = car.getColor();
        this.brand = car.getBrand();
        this.maxSpeed = car.getMaxSpeed();
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
