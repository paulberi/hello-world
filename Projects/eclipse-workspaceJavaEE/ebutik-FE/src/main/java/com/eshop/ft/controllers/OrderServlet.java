package com.eshop.ft.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.eshop.bt.to.CustomerTO;
import com.eshop.bt.to.OrderTO;
import com.eshop.bt.to.ProductTO;
import com.eshop.bt.vo.Order;
import com.eshop.bt.vo.Product;
import com.eshop.ft.beans.CustomerBean;
import com.eshop.ft.beans.OrderBean;
import com.eshop.ft.beans.ProductBean;

/**
 * Servlet implementation class OrderServlet
 */
@WebServlet("/OrderServlet")
public class OrderServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static OrderTO orderTO;

	public void init() throws ServletException {
		orderTO = new OrderTO();

	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		long customerId = Long.parseLong(request.getParameter("customerId"));
		long orderId = Long.parseLong(request.getParameter("orderId"));
		
		
		List<String> productList = new ArrayList<>();
		String[] fromCheckBox = request.getParameterValues("productName");
		for(int i=0; i<fromCheckBox.length; i++) {
			System.out.println("is it working");
			System.out.println(fromCheckBox[i]);
			productList.add(fromCheckBox[i]);
		}
		
		CustomerTO customerTO= new CustomerTO();
		
		List<CustomerBean> customers=customerTO.getAllCustomers();
		
		HttpSession session= request.getSession();
		for(CustomerBean cust:customers) {
			long custId=cust.getCustomerId();
			session.setAttribute("custId", custId);
		}
		session.setAttribute("customers", customers);
		//session to be continued had serious problems with launching Lists from 
		//servlets will work on that in the week against developing the shoping cart
		
		Order order = new Order();
		order.setOrderId(orderId);
		order.setCustomerId(customerId);
		order.setProductList(productList);

		if (productList.isEmpty() || customerId == 0 || orderId == 0 || customerId>3) {
			request.setAttribute("error", "You are missing some of the inputs");
			doGet(request, response);
		} else {
			OrderBean order1 = orderTO.postOrder(order);
			System.out.println("Order has been created");
			System.out.println(order1.getOrderId());
			response.sendRedirect("OrderList.jsp");

		}
	}

}
