package com.eshopp.resources;


import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eshopp.entities.Customer;

public class CustomerClient {
	
	static WebTarget customerTarget;// =ResourceClient.baseTarget.path("customers");
	static Customer customer;
	static String listOfCustomers;
	static Response postResponse;
	
	
	//getting a single customer
	public void getSingleCustomer(long customerId) {
		WebTarget getCustomer=customerTarget.path("{customerId}");
		
		customer=getCustomer.resolveTemplate("customerId", customerId)
				.request(MediaType.APPLICATION_JSON)
				.get(Customer.class);
		
	}
	
	public void getAllCustomers(String path) {
		WebTarget getCustomers=customerTarget.path(path);
		
		listOfCustomers= getCustomers
				.request(MediaType.APPLICATION_JSON)
				.get(String.class);
	//	Customer customer= response.readEntity(Customer.class);
		System.out.println(listOfCustomers);
	}
	
	public void createCustomer(Customer customer, String path) {
		
		WebTarget postCustomer= customerTarget.path(path);
		postResponse  = postCustomer.request()
				.post(Entity.json(customer));
		
		//to check if the object was created
		Customer createdCustomer=postResponse.readEntity(Customer.class);
	}

}
