package com.rentals.carRentals.webSecurity;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.rentals.carRentals.entity.User;
import com.rentals.carRentals.repository.UserRepository;

@Service
public class UserDetailsAuth implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = null;
		final List<User> userList=userRepository.findAll();
		for(User user1:userList) {
			if(user1.getUsername().equals(username)) {
				user=user1;
				System.out.println(user);
			}
		}
		if(user!=null) {
			System.out.println("not null print out");
			System.out.println(user);
			return new MyUserPrincipal(user);
			
		}
		System.out.println("not null print out");
		throw new UsernameNotFoundException("Cound not find user with username: "+username);
	}
	



}

