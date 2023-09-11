package com.eshop.dt.client;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.eshop.bt.vo.Product;

public class ProductClient {
	
	public static Product getProductFromRest(long id) {
		String uri = "http://localhost:8080/eshop-BE/resources/products/" + id;
		Client client = ClientBuilder.newClient();

		Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get();
		System.out.println(response.getStatus());
		System.out.println(response.getEntity());
		Product product = response.readEntity(Product.class);

		return product;
	}
	public static List<Product> getAllProducts() {
		String uri = "http://localhost:8080/eshop-BE/resources/products/getProducts";
		Client client = ClientBuilder.newClient();
		List<Product> product= client.target(uri)
				.request(MediaType.APPLICATION_JSON)
				.get(Response.class)
				.readEntity(new GenericType<List<Product>>() {});
		return product;

	}
	public static Product postProductToRest(Product product) {
		
		String uri = "http://localhost:8080/eshop-BE/resources/products/postProduct";
		Client client = ClientBuilder.newClient();

		WebTarget target = client.target(uri);
		Response postResponse = target.request(MediaType.APPLICATION_JSON).post(Entity.json(product));
		Product newlyCreated = postResponse.readEntity(Product.class);
		return newlyCreated;
	}

}
