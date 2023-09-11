package com.eshop.dt.client;

import java.util.List;


import com.eshop.bt.to.CustomerTO;
import com.eshop.bt.to.OrderTO;
import com.eshop.bt.to.ProductTO;
import com.eshop.bt.vo.Customer;
import com.eshop.bt.vo.Product;
import com.eshop.ft.beans.CustomerBean;
import com.eshop.ft.beans.OrderBean;
import com.eshop.ft.beans.ProductBean;

public class jaxrsClient {
	
	public static void main(String[] args) {
		
//		System.out.println("hello");
//		ProductTO orderTO=new ProductTO();
		CustomerTO customerTO= new CustomerTO();
//		CustomerBean customer5=customerTO.getCustomer(2);
//		System.out.println(customer5.getEmail()+" this customer is working");
//
//		
//		List<ProductBean> customer = orderTO.getAllProducts();
//		for(ProductBean bean1:customer) {
//			System.out.println(bean1.getName());
//		}
		List<CustomerBean>custbean=customerTO.getAllCustomers();
		for(CustomerBean bean:custbean) {
			System.out.println("yeah yeah "+bean.getEmail()+" password "+bean.getPassword());
		}
//		Product product1=new Product(8,"Banana", 89.9,"Fruit", 79);
//		ProductBean product=orderTO.postProduct(product1);
//		System.out.println(product.getName());
	}

}
