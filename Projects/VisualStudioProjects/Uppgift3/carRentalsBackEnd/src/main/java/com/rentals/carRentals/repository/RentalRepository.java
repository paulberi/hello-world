package com.rentals.carRentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentals.carRentals.entity.Rental;
import com.rentals.carRentals.entity.RentalId;

public interface RentalRepository extends JpaRepository<Rental, RentalId>{
	
	

}
