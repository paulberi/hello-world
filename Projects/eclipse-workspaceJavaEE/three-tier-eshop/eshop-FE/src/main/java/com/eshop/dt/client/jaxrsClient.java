package com.eshop.dt.client;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;

public class jaxrsClient {
	
	public static void main(String[] args ) {
		Client client = ClientBuilder.newClient();
		System.out.println(client);
	}

}
