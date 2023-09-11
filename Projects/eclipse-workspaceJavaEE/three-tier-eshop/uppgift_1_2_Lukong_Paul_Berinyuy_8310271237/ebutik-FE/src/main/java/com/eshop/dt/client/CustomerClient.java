package com.eshop.dt.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eshop.bt.vo.Customer;

public class CustomerClient {

	public static Customer getCustomerFromRest(long id) {

		String uri = "http://localhost:8080/eshop-BE/resources/customers/" + id;
		Client client = ClientBuilder.newClient();

		Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get();
		System.out.println(response.getStatus());
		System.out.println(response.getEntity());
		Customer customer = response.readEntity(Customer.class);

		return customer;

	}

	public static List<Customer> getAllCustomers() {
		String uri = "http://localhost:8080/eshop-BE/resources/customers/getCustomers";
		Client client = ClientBuilder.newClient();
		
		List<Customer> customer= client.target(uri)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<List<Customer>>() {});
		return customer;
		

	}

	public static Customer postCustomerToRest(Customer customer) {
		// Customer customer = new Customer(1, "Ambe", "Ambe@ymail.com", "password",
		// "Bafut");
		String uri = "http://localhost:8080/eshop-BE/resources/customers/postCustomer";
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(uri);
		Response postResponse = target.request(MediaType.APPLICATION_JSON).post(Entity.json(customer));
		Customer newlyCreated = postResponse.readEntity(Customer.class);
		System.out.println(customer.getEmail());
		return newlyCreated;
	}

}
