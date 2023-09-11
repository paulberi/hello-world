package com.builder;

public class Main {

    public static void main(String[] args) {
	    CarBuilder builder = new CarBuilder();
	    Car car = builder
                    .addColor("Blue")
                    .addBrand("Volvo")
                    .setMaxSpeed(40)
                    .build();

	    System.out.println("Created a Car of brand " + car.getBrand() + " and color " + car.getColor() + " and max speed " + car.getMaxSpeed());
    }
}
