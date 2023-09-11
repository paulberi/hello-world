package com.eshop.bt.to;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

import com.eshop.bt.vo.Order;
import com.eshop.dt.dao.Database;
import com.eshop.dt.eo.OrderEO;


@Named
@ApplicationScoped
public class OrderService implements ServiceDAO<Order, OrderEO>{
	
	private Map<Long, Order> orders =Database.getOrders();
	


	@Override
	public Order create(OrderEO ordereo) throws SQLException {
		// TODO Auto-generated method stub
		
		Order order=new Order(ordereo.getOrderId(), ordereo.getCustomerId(),ordereo.getProductList());
		order.setOrderId((orders.size()+1));
		orders.put(order.getOrderId(), order);
		return order;
	}

	@Override
	public List<Order> findAll() throws SQLException {
		// TODO Auto-generated method stub
		return new ArrayList<Order>(orders.values());
	}

	@Override
	public Order find(long id) throws SQLException {
		// TODO Auto-generated method stub
		return orders.get(id);
	}

	@Override
	public Order update(long id ,OrderEO order1) throws SQLException {
		// TODO Auto-generated method stub
		if(order1.getOrderId()<=0 && order1.getOrderId()!=id) {
			return null;
		}
		Order order=new Order();
		order.setOrderId(order1.getOrderId());
		order.setCustomerId(order1.getCustomerId());
		order.setProductList(order1.getProductList());
		orders.put(order.getOrderId(), order);
		return order;
	}

	@Override
	public Order delete(long id) throws SQLException {
		// TODO Auto-generated method stub
		return orders.remove(id);
	}

}
