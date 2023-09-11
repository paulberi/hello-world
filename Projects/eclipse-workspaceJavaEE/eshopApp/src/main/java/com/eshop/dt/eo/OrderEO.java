package com.eshop.dt.eo;

import java.util.List;

import javax.inject.Named;


//@Entity
@Named
public class OrderEO {
	
	
	private long customerId;
	private List<String> productList;
	private long orderId;
	
	public OrderEO() {
		
	}
	public OrderEO( long orderId, long customerId,
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
