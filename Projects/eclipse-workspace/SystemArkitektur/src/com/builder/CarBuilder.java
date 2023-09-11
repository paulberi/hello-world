package com.builder;

public class CarBuilder {
    private Car car = new Car();

    public CarBuilder addColor(String color) {
        // TODO validate color
        car.setColor(color);
        return this;
    }

    public CarBuilder addBrand(String brand) {
        // TODO validate brand
        car.setBrand(brand);
        return this;
    }

    public CarBuilder setMaxSpeed(double maxSpeed) {
        // TODO Exception
        if(maxSpeed<50 || maxSpeed > 350) {
            throw new RuntimeException("Invalid maxSpeed");
        }
        car.setMaxSpeed(maxSpeed);
        return this;
    }

    public Car build() throws RuntimeException {
        // TODO Check if all conditions are fulfilled
        if(car.getBrand()==null) {
            throw new RuntimeException("Brand missing");
        }
        return car;
    }
}
