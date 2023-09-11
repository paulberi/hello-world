package com.eshop.dt.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eshop.bt.vo.Order;

public class OrderClient {
	
	public static Order getOrderFromRest(long id) {

		String uri = "http://localhost:8080/eshop-BE/resources/orders/" + id;
		Client client = ClientBuilder.newClient();

		Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get();
		System.out.println(response.getStatus());
		System.out.println(response.getEntity());
		Order order = response.readEntity(Order.class);

		return order;

	}

	public static List<Order> getAllOrders() {
		String uri = "http://localhost:8080/eshop-BE/resources/orders/getOrders";
		Client client = ClientBuilder.newClient();
		
		List<Order> order= client.target(uri)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<List<Order>>() {});
		return order;

	}

	public static Order postOrderToRest(Order order) {
		String uri = "http://localhost:8080/eshop-BE/resources/orders/postOrder";
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(uri);
		Response postResponse = target.request(MediaType.APPLICATION_JSON).post(Entity.json(order));
		Order newlyCreated = postResponse.readEntity(Order.class);
		return newlyCreated;
	}

}
