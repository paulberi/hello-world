package com.rentals.carRentals.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rentals.carRentals.entity.Car;
import com.rentals.carRentals.entity.Rental;
import com.rentals.carRentals.entity.RentalId;
import com.rentals.carRentals.entity.User;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.repository.CarRepository;
import com.rentals.carRentals.repository.RentalRepository;
import com.rentals.carRentals.repository.UserRepository;

@Service
public class RentalService {

	@Autowired
	private RentalRepository rentalRepository;
	@Autowired
	private CarRepository carRepository;
	@Autowired
	private UserRepository userRepository;
	
	

	public Rental getRental() {
		return new Rental();
	}

	public List<Rental> findAllRentals() {
		List<Rental> rentalList = new ArrayList<>();
		rentalRepository.findAll().forEach(rentalList::add);
		rentalList.forEach(rental->{
			System.out.println(rental.getRentalId());
		});
		return rentalList;
	}

	public List<Rental> findByCarId(Integer carId) {
		List<Rental> rentalLists = new ArrayList<>();
		rentalRepository.findAll().forEach(e -> {
			if (e.getCar().getCarId() == carId) {
				rentalLists.add(e);
			}
		});
		return rentalLists;

	}

	public List<Rental> findByUserId(Integer userId) {
		List<Rental> rentalLists = new ArrayList<>();
		rentalRepository.findAll().forEach(e -> {
			if (e.getUser().getUserId() == userId) {
				rentalLists.add(e);
			}
		});
		return rentalLists;

	}

	public ResponseEntity<Rental> findRentalById(Integer userId, Integer carId) throws ResourceNotFoundException {
		Rental rentalFromDB=new Rental();
		List<Rental> rentalList1 = new ArrayList<>();
		List<Rental> rentalList2 = new ArrayList<>();
		rentalRepository.findAll().forEach(e -> {
			if (e.getUser().getUserId() == userId) {
				rentalList1.add(e);
				for(Rental siftered:rentalList1) {
					if(siftered.getCar().getCarId()==carId) {
						rentalList2.add(siftered);
					}
				}
			}
		});
		for(Rental rental: rentalList2) {
			rentalFromDB=rental;
		}
		return ResponseEntity.ok().body(rentalFromDB);
	}

	public String addRental(Rental rental) throws ResourceNotFoundException {

		Car car = carRepository.findById(rental.getCar().getCarId())
				.orElseThrow(() -> new ResourceNotFoundException("Car not found for id :: " + rental.getCar().getCarId()));

		User user = userRepository.findById(rental.getUser().getUserId())
				.orElseThrow(() -> new ResourceNotFoundException("User not found for id :: " + rental.getUser().getUserId()));

		rental.setCar(car);
		rental.setUser(user);
		rentalRepository.save(rental);
		user.getUserRentals().add(rental);
		return "Order successfully created";

	}

	public ResponseEntity<Rental> updateRental(Integer userid, Integer carId, Rental rental) throws ResourceNotFoundException {
		List<Rental> rentalList1 = new ArrayList<>();
		List<Rental> rentalList2 = new ArrayList<>();
		rentalRepository.findAll().forEach(e -> {
			if (e.getUser().getUserId() == userid) {
				rentalList1.add(e);
				for(Rental siftered:rentalList1) {
					if(siftered.getCar().getCarId()==carId) {
						rentalList2.add(siftered);
					}
				}
			}
		});
		Rental updatedRental=new Rental();
		for(Rental rentalFromDB:rentalList2) {
			Car car = carRepository.findById(rental.getCar().getCarId())
					.orElseThrow(() -> new ResourceNotFoundException("Car not found for id :: " + rental.getCar().getCarId()));
			rentalFromDB.getCar().getCarId();
			rentalFromDB.setCar(car);

			User user = userRepository.findById(rental.getUser().getUserId())
					.orElseThrow(() -> new ResourceNotFoundException("User not found for id :: " + rental.getUser().getUserId()));
			rentalFromDB.getUser().getUserId();
			rentalFromDB.setUser(user);

			rentalFromDB.setDateOfBooking(rental.getDateOfBooking());
			rentalFromDB.setStartDate(rental.getStartDate());
			updatedRental=rentalRepository.save(rentalFromDB);
		}
		return ResponseEntity.ok().body(updatedRental);
		
	}

	public ResponseEntity<?> deleteRental(RentalId id) throws ResourceNotFoundException {
		rentalRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Rental not found for id :: " + id));
		rentalRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}

}
