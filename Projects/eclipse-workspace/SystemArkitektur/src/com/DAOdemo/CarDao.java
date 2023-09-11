package com.DAOdemo;

import java.util.ArrayList;
import java.util.List;

public class CarDao {

    // Database
    private List<Car> cars = new ArrayList<>();
    private long currentId = 1;

    public CarDao() {
        this.cars.add(new Car(currentId++, "red", "Audi", 200));
        this.cars.add(new Car(currentId++, "blue", "Volvo", 210));
    }

    public List<Car> getAll() {
        // TODO Use DB connection to get all Cars
        return this.cars;
    }

    public Car get(long id) {
        Car car = null;
        for(Car c : this.cars) {
            if(c.getId()==id) {
                car = c;
                break;
            }
        }
        return car;
    }

    public void save(Car car) {
        car.setId(currentId++);
        cars.add(car);
    }

    public void update(Car car) {
        for(Car c : this.cars) {
            if(c.getId()==car.getId()) {
                c.setColor(car.getColor());
                c.setBrand(car.getBrand());
                c.setMaxSpeed(car.getMaxSpeed());
                break;
            }
        }
    }

    public void delete(Car car) {
        this.cars.remove(car);
    }

}
