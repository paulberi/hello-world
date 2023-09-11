package com.eshop.bt.to;

import java.util.ArrayList;
import java.util.List;

import com.eshop.bt.vo.Customer;
import com.eshop.dt.client.CustomerClient;
import com.eshop.ft.beans.CustomerBean;

public class CustomerTO {
	
	public static List<CustomerBean> getAllCustomers() {
		List<CustomerBean> customerList=new ArrayList<CustomerBean>();
		CustomerClient cc= new CustomerClient();
		List<Customer> list=cc.getAllCustomers();
		for(Customer cust:list) {
			customerList.add(new CustomerBean(cust));
		}
		return customerList;
	}
	
	public static CustomerBean getCustomer (long id) {
		
		CustomerClient cc= new CustomerClient();
		Customer customer=cc.getCustomerFromRest(id);
		return new CustomerBean(customer);
	}
	public static CustomerBean postCustomer(Customer customer1) {
		CustomerClient cc=new CustomerClient();
		Customer customer=cc.postCustomerToRest(customer1);
		return new CustomerBean(customer);
	}

}
