package com.rentals.carRentals.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rentals.carRentals.entity.User;

public interface UserRepository  extends JpaRepository<User, Integer> {

	User findByUsername(String username);
}
