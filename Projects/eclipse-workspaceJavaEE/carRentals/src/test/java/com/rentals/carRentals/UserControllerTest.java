package com.rentals.carRentals;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.rentals.carRentals.entity.User;
import com.rentals.carRentals.entity.UserRoles;
import com.rentals.carRentals.exception.ResourceNotFoundException;
import com.rentals.carRentals.repository.UserRepository;
import com.rentals.carRentals.services.UserService;

@SpringBootTest
public class UserControllerTest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	private UserRoles userRoles;

	@Test
	@Order(1)
	public void createUser() throws ResourceNotFoundException {
		User user = new User();
		user.setUsername("Mafia");
		user.setAddress("Sundsvall-Umeå");
		user.setUsername("username");
		user.setPassword("password");
		user.setRole(userRoles.Customer);
		userService.addUser(user);
		User user1 = userService.findUserById(5).getBody();
		System.out.println(user1.getName() + " or " + user.getName());/*has a problem*/
		assertNotNull(userService.findUserById(5).getBody());
	}

	@Test
	@Order(2)
	public void findAll() {
		List<User> list = userService.findAllUsers();
		assertTrue(list.size() > 0);
	}

	@Test
	@Order(3)
	public void findById() throws ResourceNotFoundException {
		User user = userService.findUserById(4).getBody();
		assertEquals("Nso", user.getName());
	}

	@Test
	@Order(4)
	public void updateUser() throws ResourceNotFoundException {
		User user = userRepository.findById(4).get();
		user.setName("Eric");
		user.setAddress(user.getAddress());
		user.setPassword(user.getPassword());
		user.setUsername(user.getUsername());
		user.setRole(user.getRole());

		userRepository.save(user);
		assertNotEquals("Nso", userService.findUserById(4).getBody());
	}

//	@Test
//	@Order(5)
//	public void DeleteUser() throws ResourceNotFoundException {
//		userService.deleteUser(2);
//		assertFalse(userRepository.existsById(2));
//	}
}
