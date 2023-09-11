package com.eshopp.resources;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eshopp.entities.Customer;
import com.eshopp.entities.messageBodyReader.CustomerBodyReader;

public class ResourceClient {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("How are you guys");
		getAllDemo();
		getOneDemo();
		getOneDemo2();
		//gettingAllUsingTheObject();
		//creatingAnObject();
		
		
	}

public static String getAllDemo() {
	String uri = "http://localhost:8080/eshopp/resources/customers/getCustomers";
	Client client = ClientBuilder.newClient();

	String response = client.target(uri).request(MediaType.APPLICATION_JSON).get(String.class);
	// Customer customer= response.readEntity(Customer.class);
	System.out.println(response);
	char[] list=response.toCharArray();
	System.out.println(list);
	return response;
}

public static String getOneDemo() {
	String uri = "http://localhost:8080/eshopp/resources/customers/1";
	Client client = ClientBuilder.newClient();

	Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get();
	String cust = response.readEntity(String.class);
	
	System.out.println(cust);
	return cust;
}

public static void getOneDemo2() {
	String uri = "http://localhost:8080/eshopp/resources/customers/2";
	Client client = ClientBuilder.newBuilder().register(CustomerBodyReader.class).build();
	
	Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get();
	//Builder builder = target.request(MediaType.APPLICATION_JSON);
	//Response response = builder.get();
	System.out.println(response.getStatus());
	System.out.println(response.getEntity());

	String customer = response.readEntity(Customer.class).getAddress();
	System.out.println(customer);
}

public static String gettingAllUsingTheObject() {
	String uri = "http://localhost:8080/eshopp/resources/customers/getCustomers";
	Client client = ClientBuilder.newClient();

	String customer = client.target(uri).request(MediaType.APPLICATION_JSON).get(String.class);
	// Customer customer= response.readEntity(Customer.class);
	System.out.println(customer);
	return uri;
}

public static void creatingAnObject() {

	Customer customer = new Customer(1, "Ambe", "Ambe@ymail.com", "password", "Bafut");
	String uri = "http://localhost:8080/eshopp/resources/customers/2";
	Client client = ClientBuilder.newClient();

	WebTarget target = client.target(uri);
	Response postResponse = target.request(MediaType.APPLICATION_JSON).post(Entity.json(customer));
	Customer newlyCreated = postResponse.readEntity(Customer.class);
	System.out.println(customer.getEmail());
}
}