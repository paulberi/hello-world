package com.rentals.carRentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentals.carRentals.entity.Car;
import com.rentals.carRentals.entity.User;


public interface CarRepository extends JpaRepository<Car, Integer>{
	Car findByName(String name);
}
