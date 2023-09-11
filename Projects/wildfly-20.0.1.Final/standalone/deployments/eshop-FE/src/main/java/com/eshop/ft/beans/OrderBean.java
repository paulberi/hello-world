package com.eshop.ft.beans;

import java.util.List;

import javax.validation.constraints.NotNull;

import com.eshop.bt.vo.Order;

public class OrderBean {

	@NotNull
	private long customerId;
	
	@NotNull
	private List<String> productList;
	private long orderId;
	
	public OrderBean(Order order) {
		
		this.orderId=order.getOrderId();
		this.customerId=order.getCustomerId();
		this.productList=order.getProductList();
		
	}
	public OrderBean( long orderId, long customerId,
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
