package com.eshop.bt.vo;

import java.util.List;

//import javax.ejb.Stateful;
import javax.validation.constraints.NotNull;

//@Stateful
public class Order {
	
	@NotNull
	private long customerId;
	
	@NotNull
	private List<String> productList;
	private long orderId;
	
	public Order() {
		
	}
	public Order( long orderId, long customerId,
			List<String> productList) {

		this.orderId=orderId;
		this.customerId=customerId;
		this.productList=productList;
		
		
	
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getOrderId() {
		return orderId;
	}
	public void setOrderId(long orderId) {
		this.orderId = orderId;
	}
	public List<String> getProductList() {
		return productList;
	}
	public void setProductList(List<String> productList) {
		this.productList = productList;
	}

}
