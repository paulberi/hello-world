package com.eshop.bt.to;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.eshop.bt.vo.Customer;
import com.eshop.dt.dao.Database;
import com.eshop.dt.eo.CustomerEO;

@Named
@ApplicationScoped
public class CustomerService implements ServiceDAO<Customer,CustomerEO>{
	
	
	private Map<Long, Customer> customers =Database.getCustomers();
	
	
	public CustomerService() {
		CustomerEO cust1=new CustomerEO(1,"c1 Customer", "c1@gmail.com","password", "Umeå");
		CustomerEO cust2=new CustomerEO(2,"c2 Customer", "c2@gmail.com", "password","Sundsvall");
		CustomerEO cust3=new CustomerEO(3,"c3 Customer", "c3@gmail.com", "password","Ersboda");
		
		
		customers.put(1L, new Customer(cust1.getCustomerId(), cust1.getName(), cust1.getEmail(),cust1.getPassword(), cust1.getAddress()));
		customers.put(2L, new Customer(cust2.getCustomerId(), cust2.getName(), cust2.getEmail(),cust2.getPassword(), cust2.getAddress()));
		customers.put(3L, new Customer(cust3.getCustomerId(), cust3.getName(), cust3.getEmail(),cust3.getPassword(), cust3.getAddress()));
		
	}
	


	@Override
	public Customer create(CustomerEO cust1) throws SQLException {
		// TODO Auto-generated method stub
		Customer customer=new Customer(cust1.getCustomerId(), cust1.getName(), cust1.getEmail(),cust1.getPassword(), cust1.getAddress());
		
		customer.setCustomerId(customers.size()+1);
		customers.put(customer.getCustomerId(), customer);
		return customer;
	}

	@Override
	public List<Customer> findAll() throws SQLException {
		// TODO Auto-generated method stub
		System.out.println("hello");
		return new ArrayList<Customer>(customers.values());
	}

	@Override
	public Customer find(long id) throws SQLException {
		// TODO Auto-generated method stub
		return customers.get(id);
	}

	public Customer update(long id,CustomerEO cust1) throws SQLException {
		// TODO Auto-generated method stub
		if(cust1.getCustomerId()<=0 && cust1.getCustomerId()!=id ){
			return null;
		}
		Customer customer=new Customer();
		customer.setCustomerId(cust1.getCustomerId());
		customer.setName(cust1.getName());
		customer.setAddress(cust1.getAddress());
		customer.setEmail(cust1.getEmail());
		customer.setPassword(cust1.getPassword());
		
		
		customers.put(customer.getCustomerId(), customer);
		return customer;
	}

	@Override
	public  Customer delete(long id) throws SQLException {
		// TODO Auto-generated method stub
		return customers.remove(id);
	}
}
