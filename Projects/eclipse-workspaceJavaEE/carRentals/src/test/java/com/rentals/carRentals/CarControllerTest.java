package com.rentals.carRentals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rentals.carRentals.entity.Car;
import com.rentals.carRentals.entity.CarBrands;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.repository.CarRepository;
import com.rentals.carRentals.services.CarService;

@SpringBootTest
public class CarControllerTest {
	
	@Autowired
	private CarRepository carRepository;

	@Autowired
	private CarService carService;
	
	
	private CarBrands carbrands;
	
	//CRUD test
	//mixing carService and carRepository just to get different levels involved in a single test
	//not sure if it makes sense but it works and please do comment if you have an advice on such a behavior
	
	@Test
	
	public void createCar() {
		Car car=new Car();
		car.setCarBrands(carbrands.Mercedes);
		car.setName("ml");
		car.setPassengers(4);
		car.setPrice(3000);
		carService.addCar(car);
		System.out.println(car.getName());
		//carRepository.save(car);
		assertNotNull(carRepository.findById(car.getCarId()).get());
	}
	@Test
	public void findAll() {
		List<Car> list = carService.findAllCars();
		assertTrue(list.size()>0);
		
	}
	@Test
	public void findById() throws ResourceNotFoundException {
		Car car=carService.findCarById(2); //using five here because prior to the car created above there are already 4 in the database
		System.out.println(car.getPrice());
		assertEquals(2000,car.getPrice());
	}
	
	@Test
	public void updateCar() throws ResourceNotFoundException {
		Car car=carService.findCarById(1);
		car.setPrice(3000);
		carRepository.save(car);
		assertNotEquals(5500, carRepository.findById(1).get().getPrice());
	}
	
	@Test
	public void DeleteCar() throws ResourceNotFoundException {
		carService.deleteCar(3);
		assertFalse(carRepository.existsById(3));
	}

}
