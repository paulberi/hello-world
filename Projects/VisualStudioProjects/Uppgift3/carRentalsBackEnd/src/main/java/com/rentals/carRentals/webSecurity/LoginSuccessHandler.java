package com.rentals.carRentals.webSecurity;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException  {
		System.out.println("paul here checking onAuth");
		MyUserPrincipal userDetails=(MyUserPrincipal) authentication.getPrincipal();
		System.out.println("Username "+userDetails.getUsername());
		System.out.println("Password "+userDetails.getPassword());
		
		//String redirectUrl1 = request.getRemoteHost();
		String redirectUrl = request.getRemoteUser();
		
		Collection<? extends GrantedAuthority> authorities =userDetails.getAuthorities();
		authorities.forEach(auth->{
			System.out.println(auth.getAuthority());
		});
		
		if(userDetails.hasRole("Admin")) {
			redirectUrl="http://127.0.0.1:5500/home.html";
			System.out.println("admin");
		}
		else if(userDetails.hasRole("Customer")) {
			redirectUrl="http://127.0.0.1:5500/home.html";
			System.out.println("customer");
		}
		
		System.out.println("paul here checking on Auth end");
		response.sendRedirect(redirectUrl);
		
	}
		
}
