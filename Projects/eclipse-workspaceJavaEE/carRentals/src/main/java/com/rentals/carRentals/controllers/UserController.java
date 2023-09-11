package com.rentals.carRentals.controllers;

import java.io.IOException;
import java.util.List;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.rentals.carRentals.entity.Rental;
import com.rentals.carRentals.entity.Role;
import com.rentals.carRentals.entity.User;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.services.UserService;
import com.rentals.carRentals.webSecurity.LoginSuccessHandler;
import com.rentals.carRentals.webSecurity.UserDetailsAuth;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	Logger logger = LoggerFactory.getLogger(UserController.class);
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private LoginSuccessHandler successHandler;
	
	@Autowired
	private UserDetailsAuth userDetails;
	
	@GetMapping("/login")
	public ResponseEntity<?> userLogin( @RequestBody User user) throws IOException {
		userDetails.loadUserByUsername(user.getUsername());
		logger.info("all users found");
		return ResponseEntity.ok(user);
		
		
	}
	

	@GetMapping("/customers")
	public List<User> getAllUsers() {
		logger.info("all users found");
		
		return userService.findAllUsers();
	}
	@GetMapping("/roles")
	public List<Role> getAllRoles() {
		logger.info("all users found");
		return userService.findAllRoles();
	}

	@GetMapping("/defaultUser")
	public User getUser() {
		return userService.getUser();
	}
	
	@RequestMapping(path="/allUsers/getUser/{id}", method = RequestMethod.GET)
	public ResponseEntity<User> getUserById(@PathVariable("id") Integer id) throws ResourceNotFoundException {
		return userService.findUserById(id);
		
	}

	@RequestMapping(path = "/allUsers/postUser", method = RequestMethod.POST, consumes = "application/json")
	public String getUser(@RequestBody User user) throws ResourceNotFoundException {
		logger.info("user created");
		return userService.addUser(user);
	}
	
	@RequestMapping(path="/allUsers/update/{id}", method =RequestMethod.PUT)
	public ResponseEntity<User> updateUser(@PathVariable("id") Integer id, @RequestBody User user) throws ResourceNotFoundException{
		logger.info("user updated");
		return userService.updateUser(id, user);
		
	}

	@RequestMapping(path="allUsers/delete/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteUser(@PathVariable("id") Integer id) throws ResourceNotFoundException {
		logger.info("user deleted");
		return userService.deleteUser(id);
		
	}
}
