package com.rentals.carRentals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rentals.carRentals.entity.Car;
import com.rentals.carRentals.entity.Rental;
import com.rentals.carRentals.entity.User;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.repository.CarRepository;
import com.rentals.carRentals.repository.RentalRepository;
import com.rentals.carRentals.repository.UserRepository;
import com.rentals.carRentals.services.RentalService;

@SpringBootTest
public class RentalControllerTest {
	
	@Autowired
	RentalRepository rentalRepository;
	
	@Autowired
	RentalService rentalService;
	
	@Autowired
	UserRepository useRepository;
	
	@Autowired
	CarRepository carRepository;
	
	@Test
	@Order(1)
	public void testOfCreate() throws ResourceNotFoundException {
		Rental rental=new Rental();
		
		Car car=carRepository.findById(2).get();
		User user=useRepository.findById(2).get();
		rental.setCar(car);
		rental.setUser(user);
		rental.getRentalId().getCarId();
		rental.getRentalId().getUserId();
		rental.setDateOfBooking(new Date());
		rental.setStartDate(new Date());
		rentalService.addRental(rental);
		assertNotNull(rentalRepository.findAll());
	}
	
	@Test
	@Order(2)
	public void findAll() {
		List<Rental> list= rentalService.findAllRentals();
		assertTrue(list.size()>0);
	}
	@Test
	@Order(3)
	public void findById() {
		List<Rental> rentalList1 = new ArrayList<>();
		List<Rental> rentalList2 = new ArrayList<>();
		rentalRepository.findAll().forEach(e -> {
			if (e.getUser().getUserId() == 2) {
				rentalList1.add(e);
				for(Rental siftered:rentalList1) {
					if(siftered.getCar().getCarId()==2) {
						rentalList2.add(siftered);
					}
				}
			}
		});
		for(Rental rental:rentalList2) {
			assertEquals(2, rental.getCar().getCarId(), rental.getUser().getUserId());
		}
		
	}
	@Test
	@Order(4)
	public void updateRental() throws ResourceNotFoundException {
		List<Rental> rentalList1 = new ArrayList<>();
		List<Rental> rentalList2 = new ArrayList<>();
		rentalRepository.findAll().forEach(e -> {
			if (e.getUser().getUserId() == 2) {
				rentalList1.add(e);
				for(Rental siftered:rentalList1) {
					if(siftered.getCar().getCarId()==2) {
						rentalList2.add(siftered);
					}
				}
			}
		});
		for(Rental rental:rentalList2) {
			rental.setDateOfBooking(new Date());
			rental.setStartDate(new Date());
			rentalService.addRental(rental);
			
		}
		List<Rental> rentalList3 = new ArrayList<>();
		List<Rental> rentalList4 = new ArrayList<>();
		rentalRepository.findAll().forEach(e -> {
			if (e.getUser().getUserId() == 2) {
				rentalList3.add(e);
				for(Rental siftered:rentalList3) {
					if(siftered.getCar().getCarId()==2) {
						rentalList4.add(siftered);
					}
				}
			}
		});
		for(Rental rental:rentalList4) {
			assertNotEquals(5500, rental.getStartDate());
		}
	}
	
	
	
	
}