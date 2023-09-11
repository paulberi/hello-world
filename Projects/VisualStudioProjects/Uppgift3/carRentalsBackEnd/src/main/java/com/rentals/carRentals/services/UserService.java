package com.rentals.carRentals.services;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.rentals.carRentals.entity.Car;
import com.rentals.carRentals.entity.Rental;
import com.rentals.carRentals.entity.Role;
import com.rentals.carRentals.entity.User;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.repository.CarRepository;
import com.rentals.carRentals.repository.RentalRepository;
import com.rentals.carRentals.repository.RoleRepository;
import com.rentals.carRentals.repository.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RentalRepository rentalrep;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private CarService carService;

	public User getUser() {
		return new User();
	}

	public List<User> findAllUsers() {
		List<User> userList = new ArrayList<>();
		userRepository.findAll().forEach(userList::add);
		return userList;
	}
	
	public List<Role> findAllRoles() {
		List<Role> roleList = new ArrayList<>();
		roleRepository.findAll().forEach(roleList::add);
		return roleList;
	}

	public ResponseEntity<User> findUserById(Integer id) throws ResourceNotFoundException {
		User user = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("User not found for id :: " + id));

		return ResponseEntity.ok().body(user);

	}

	public User findByName(String name) {
		return userRepository.findByUsername(name);
	}

	public User saveUser(User user) {
		User newUser = new User();
		newUser.setAddress(user.getAddress());
		newUser.setName(user.getName());
		newUser.setUsername(user.getUsername());
		newUser.setPassword(user.getPassword());
		newUser.setRole(user.getRole());
		newUser.getUserRentals().addAll(user.getUserRentals().stream().map(rental -> {
			Integer id = rental.getCar().getCarId();
			Car car = new Car();
			try {
				car = carService.findCarById(id);
			} catch (ResourceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Rental newRental = new Rental();
			newRental.setCar(car);
			newRental.setUser(newUser);
			newRental.setDateOfBooking(rental.getDateOfBooking());
			newRental.setStartDate(rental.getStartDate());
			return newRental;
		}).collect(Collectors.toList()));
		return userRepository.save(newUser);
	}

	public String addUser(User user) throws ResourceNotFoundException {
		userRepository.save(user);
		return "User created";
	}



	public ResponseEntity<User> updateUser(Integer id, User user) throws ResourceNotFoundException {
		User userFromDB = userRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Car not found for id :: " + id));
		userFromDB.setAddress(user.getAddress());
		userFromDB.setName(user.getName());
		userFromDB.setPassword(user.getPassword());
		userFromDB.setUsername(user.getUsername());
		userFromDB.setPassword(user.getPassword());
		userFromDB.setRole(user.getRole());
		userFromDB.setUserRentals(user.getUserRentals());
		System.out.println("does the application come upp to here?");
		userRepository.save(userFromDB);
		System.out.println("does the application come upp to here?");
		return ResponseEntity.ok().body(userFromDB);
	}

	public ResponseEntity<?> deleteUser(Integer id) throws ResourceNotFoundException {
		userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found for id :: " + id));
		userRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}


}
