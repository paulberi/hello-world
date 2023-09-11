package com.rentals.carRentals.controllers;


import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.rentals.carRentals.entity.Car;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.services.CarService;

@CrossOrigin(origins = " * ", allowedHeaders = " * ")
@RestController
@RequestMapping("/api/v1/")
public class CarController {
	
	Logger logger = LoggerFactory.getLogger(CarController.class);
	

    @Autowired
    private CarService carService;
    

    @GetMapping("/defaultCar")
    public Car getCar() {
    	logger.info("Default Car ");
        return carService.getCar();
    }
    
   
    @GetMapping("/cars")
    public List<Car> getAllCars(){
    	logger.info("Here is a list of all our cars");
    	return carService.findAllCars();
    }
    
    @RequestMapping(path="/cars/{id}", method = RequestMethod.GET)
	public Car getCarById(@PathVariable("id") Integer id) throws ResourceNotFoundException {
    	logger.info(" car "+id);
		return carService.findCarById(id);
		
	}

    @RequestMapping(path = "/addcar", method = RequestMethod.POST, consumes = "application/json")
    public String getCar(@RequestBody 	Car car) {
    	logger.info("Car created");
        return carService.addCar(car);
    }
    @RequestMapping(path="/updatecar/{id}", method =RequestMethod.PUT)
	public ResponseEntity<Car> updateCar(@PathVariable("id") Integer id, @RequestBody Car car) throws ResourceNotFoundException{
		logger.info("Car updated Successfully");
    	return carService.updateCar(id, car);
		
	}
    @RequestMapping(path="/deletecar/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteCar(@PathVariable("id") Integer id) throws ResourceNotFoundException {
    	logger.info("Car deleted Successfully");
		return carService.deleteCar(id);
		
	}
    

}
