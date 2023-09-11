package com.eshop.dt.client;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.client.ClientConfig;
import org.glassfish.jersey.jackson.JacksonFeature;

import com.eshop.bt.to.CustomerTO;
import com.eshop.bt.to.OrderTO;
import com.eshop.bt.to.ProductTO;
import com.eshop.bt.vo.Customer;
import com.eshop.bt.vo.Order;
import com.eshop.bt.vo.Product;
import com.eshop.ft.beans.CustomerBean;
import com.eshop.ft.beans.OrderBean;
import com.eshop.ft.beans.ProductBean;


public class JaxrsClient {
	static CustomerTO custom;
	static ProductTO prod;
	static OrderTO ordern;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		getCustomers();
		getCustomerObject();
		CustomerBean cust=custom.getCustomer(2);
		System.out.println(cust.getName());
		System.out.println(cust.getEmail());
		System.out.println(cust.getAddress());
//		Customer customer = new Customer(4, "Ambe", "Ambe@ymail.com", "password", "Bafut");
//		CustomerBean cust1=custom.postCustomer(customer);
//		System.out.println(cust1.getName() + " successfully created");
		List<CustomerBean> list=custom.getAllCustomers();
		System.out.println("checking the getall method");
		for(CustomerBean customer:list) {
			System.out.println(customer.getAddress()+" "+customer.getEmail()+" "+ customer.getName());
		}
		
		ProductBean pb=prod.getProduct(3);
		System.out.println(pb.getName()+" "+pb.getCategory()+" "+pb.getPrice());
		Product prod1 =new Product(8, "Ngashinga",89.0, "Ndzereng",45);
		ProductBean prodpost=prod.postProduct(prod1);
		List<ProductBean> plist=prod.getAllProducts();
		System.out.println("checking the getall products method");
		for(ProductBean prodb:plist) {
			System.out.println(prodb.getName()+" "+prodb.getCategory());
		}
		
		System.out.println("--------------------------");
		System.out.println("working on orders now");
		List<String> prodlist=new ArrayList<String>();
		prodlist.add("Rice");
		prodlist.add("Ngashinga");
		Order order = new Order(1,1,prodlist);
		Order order1=new Order(2,3,prodlist);
		Order order2=new Order(3,2,prodlist);
		OrderBean orderp1=ordern.postOrder(order);
		OrderBean orderp2=ordern.postOrder(order1);
		OrderBean orderp3=ordern.postOrder(order2);
		List<OrderBean> olist=ordern.getAllOrders();
		for(OrderBean or:olist) {
			System.out.println(or.getCustomerId()+" "+or.getProductList().toString()+" \nThis confirms orderpost success");
		}
	}
	public static void getCustomers() {
		String uri = "http://localhost:8080/eshop-BE/resources/customers/getCustomers";
		Client client = ClientBuilder.newClient();

		String response = client.target(uri).request(MediaType.APPLICATION_JSON).get(String.class);
		// Customer customer= response.readEntity(Customer.class);
		System.out.println(response);
		char[] list=response.toCharArray();
		System.out.println(list);
	}
	public static void getCustomerObject() {
		String uri = "http://localhost:8080/eshop-BE/resources/customers/2";
		Client client = ClientBuilder.newClient();
		Response response = client.target(uri).request(MediaType.APPLICATION_JSON).get();
		System.out.println(response.getStatus());
		//System.out.println(response.getEntity());
		//Object cust=response.getEntity().getClass();
		Customer customer = response.readEntity(Customer.class);
		System.out.println(customer.getAddress()+" wow, i got a customer object");
		//System.out.println(cust.toString());
	}
	public static void listAllCustomers() {
		String uri = "http://localhost:8080/eshop-BE/resources/customers/getCustomers";
		Client client = ClientBuilder.newClient();
		ClientConfig config=new ClientConfig();
		config.register(JacksonFeature.class);
		
		List<Customer> customer= client.target(uri).request(MediaType.APPLICATION_JSON).get(Response.class)
				.readEntity(new GenericType<List<Customer>>() {});
		System.out.println(customer);
		for(Customer cust: customer) {
			System.out.println(cust.getAddress()+" "+cust.getEmail()+" "+ cust.getName());
		}
	}
}
