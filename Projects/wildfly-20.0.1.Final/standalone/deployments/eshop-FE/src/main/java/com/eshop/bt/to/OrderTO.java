package com.eshop.bt.to;

import java.util.ArrayList;
import java.util.List;

import com.eshop.bt.vo.Order;
import com.eshop.dt.client.OrderClient;
import com.eshop.ft.beans.OrderBean;

public class OrderTO {
	
	public static List<OrderBean> getAllOrders() {
		List<OrderBean> orderList=new ArrayList<OrderBean>();
		OrderClient oc= new OrderClient();
		List<Order> list=oc.getAllOrders();
		for(Order order:list) {
			orderList.add(new OrderBean(order));
		}
		return orderList;
	}
	
	public static OrderBean getOrder (long id) {
		
		OrderClient oc= new OrderClient();
		Order order=oc.getOrderFromRest(id);
		return new OrderBean(order);
	}
	public static OrderBean postOrder(Order order1) {
		OrderClient oc=new OrderClient();
		Order order=oc.postOrderToRest(order1);
		return new OrderBean(order);
	}

}
