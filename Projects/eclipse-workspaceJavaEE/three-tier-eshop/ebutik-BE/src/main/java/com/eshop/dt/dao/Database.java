package com.eshop.dt.dao;

import java.util.HashMap;
import java.util.Map;

import com.eshop.bt.vo.Customer;
import com.eshop.bt.vo.Order;
import com.eshop.bt.vo.Product;

//MOCKED DATABASE AS PER THE SPECIFICATIONS OF THE INLÄMNINGSUPPGIFT
//FOLLOW UP HOW IT WORKS FOR A REAL DATABASE AND CHECK WITH ENTITYMANAGER
public class Database {
	
	private static Map<Long, Customer> customers =new HashMap<>();
	private static Map<Long, Product> products =new HashMap<>();
	private static Map<Long, Order> orders =new HashMap<>();

	
	public static Map<Long, Customer> getCustomers(){
		return customers;
	}
	public static Map<Long, Product> getProducts(){
		return products;
	}
	public static Map<Long, Order> getOrders(){
		return orders;
	}
	


}
