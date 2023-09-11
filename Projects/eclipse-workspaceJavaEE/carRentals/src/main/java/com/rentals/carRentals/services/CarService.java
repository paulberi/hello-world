package com.rentals.carRentals.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rentals.carRentals.entity.Car;
import com.rentals.carRentals.entity.User;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.repository.CarRepository;

@Service
public class CarService {

	@Autowired
	private CarRepository carRepository;
	
	public Car getCar() {
		return new Car();
	}
	
	public List<Car> findAllCars() {
		List<Car> carList=new ArrayList<>();
		carRepository.findAll().forEach(carList::add);
		return carList;
	}
	
	public Car findCarById(Integer id) throws ResourceNotFoundException {
		Car car=carRepository.findById(id).orElseThrow(() -> 
		new ResourceNotFoundException("Car not found for id :: "+id));
		
		return car;
	}
	
	public String addCar(Car car) {
		carRepository.save(car);
		return "Car successfully created";
	
	}
	
	public ResponseEntity<Car> updateCar(Integer id, Car car) throws ResourceNotFoundException {
		Car carFromDB=carRepository.findById(id).orElseThrow(() -> 
		new ResourceNotFoundException("Car not found for id :: "+id));
		carFromDB.setName(car.getName());
		carFromDB.setCarBrands(car.getCarBrands());
		carFromDB.setPassengers(car.getPassengers());
		carFromDB.setPrice(car.getPrice());
		carFromDB.setCarRentals(car.getCarRentals());
		carRepository.save(carFromDB);
		return ResponseEntity.ok().body(carFromDB);
	}
	
	public  ResponseEntity<?> deleteCar(Integer id) throws ResourceNotFoundException {
		carRepository.findById(id).orElseThrow(() -> 
		new ResourceNotFoundException("Car not found for id :: "+id));
		carRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
	
	
}
