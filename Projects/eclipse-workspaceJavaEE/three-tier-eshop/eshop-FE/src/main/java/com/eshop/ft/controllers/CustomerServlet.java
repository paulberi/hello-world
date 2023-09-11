package com.eshop.ft.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eshop.bt.to.CustomerTO;
import com.eshop.bt.vo.Customer;
import com.eshop.ft.beans.CustomerBean;

/**
 * Servlet implementation class CustomerServlet
 */
public class CustomerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	private static CustomerTO customerTO;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerServlet() {
        super();
        // TODO Auto-generated constructor stub
    }
    
    
    
    public void init() throws ServletException
    {
    	customerTO= new CustomerTO();
    }
    
    
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		
		String action= request.getServletPath();
		switch(action) {
		
		case "/new":
			showNewForm(request, response);
			break;
			
			
		case "/insert":
			insertCustomer(request,response);
			break;
			
			
		case "/delete":
			deleteCustomer(request, response);
			break;
			
			
		case "/edit":
			showEditForm(request, response);
			break;
		
			
		case "/update":
			updateCustomer(request, response);
			break;
			
			default:
				listCustomers(request, response);
				break;
		}

	}
	private void showNewForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("CustomerForm.jsp");
		dispatcher.forward(request, response);;
	}
	
	private void insertCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//long customerId =Integer.parseInt(request.getParameter("customerId"));
		
		String name =request.getParameter("name");
		String email =request.getParameter("email");
		String password =request.getParameter("password");
		String address =request.getParameter("address");
		
		
		Customer customer= new Customer();
		//customer.setCustomerId(customerId);
		customer.setName(name);
		customer.setEmail(email);
		customer.setPassword(password);
		customer.setAddress(address);
		
		customerTO.postCustomer(customer);
		
		response.sendRedirect("list");
		
	}
	
	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		
		long customerId =Integer.parseInt(request.getParameter("customerId"));
		
		try {
			//customerTO.deleteCustomer(customerId)
		}catch(Exception e) {
			e.printStackTrace();
		}
		response.sendRedirect("list");
	}
	
	
	private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		long customerId =Integer.parseInt(request.getParameter("customerId"));
		
		CustomerBean existingCustomer;
		
		try {
			
			existingCustomer = customerTO.getCustomer(customerId);

			request.setAttribute("customer",existingCustomer);
			RequestDispatcher dispatcher = request.getRequestDispatcher("CustomerForm.jsp");
			dispatcher.forward(request, response);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		long customerId =Integer.parseInt(request.getParameter("customerId"));
		String name =request.getParameter("name");
		String email =request.getParameter("email");
		String password =request.getParameter("password");
		String address =request.getParameter("address");
		
		Customer customer= new Customer();
		customer.setCustomerId(customerId);
		customer.setName(name);
		customer.setEmail(email);
		customer.setPassword(password);
		customer.setAddress(address);
		
	//	CustomerBean customer1=customerTO.updateCustomer(customerId);
		
		response.sendRedirect("list");
	}
	
	private void listCustomers(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		 
		try {
			
			List<CustomerBean> customerList = customerTO.getAllCustomers();
			for(CustomerBean cust:customerList) {
				System.out.println(cust.getCustomerId()+" with name "+cust.getName()+" and email"+cust.getEmail());
			}
			request.setAttribute("customerList", customerList);
			RequestDispatcher dispatcher = request.getRequestDispatcher("CustomerGet.jsp");
			dispatcher.forward(request, response);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	
	
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
//	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//		// TODO Auto-generated method stub
//		
//		
//		String customerId= request.getParameter("customerId");
//		String name =request.getParameter("name");
//		String email =request.getParameter("email");
//		String password =request.getParameter("password");
//		String address =request.getParameter("address");
//		
//		
//		Customer customer= new Customer();
//		customer.setCustomerId(Integer.parseInt(customerId));
//		customer.setName(name);
//		customer.setEmail(email);
//		customer.setPassword(password);
//		customer.setAddress(address);
//		
//		customerTO.postCustomer(customer);
//		
//		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/Views/CustomerGet.jsp");
//		dispatcher.forward(request, response);;
//		doGet(request, response);
//	}

}


//
//
//<dependency>
//<groupId>org.glassfish.jersey.core</groupId>
//<artifactId>jersey-server</artifactId>
//<version>2.7</version>
//</dependency>
//
//<dependency>
//<groupId>org.apache.geronimo.specs</groupId>
//<artifactId>geronimo-servlet_2.5_spec</artifactId>
//<version>1.2</version>
//<scope>provided</scope>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.core</groupId>
//<artifactId>jackson-core</artifactId>
//<version>2.3.3</version>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.core</groupId>
//<artifactId>jackson-databind</artifactId>
//<version>2.3.3</version>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.core</groupId>
//<artifactId>jackson-annotations</artifactId>
//<version>2.3.3</version>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.jaxrs</groupId>
//<artifactId>jackson-jaxrs-json-provider</artifactId>
//<version>2.3.3</version>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.module</groupId>
//<artifactId>jackson-module-jaxb-annotations</artifactId>
//<version>2.3.3</version>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.dataformat</groupId>
//<artifactId>jackson-dataformat-xml</artifactId>
//<version>2.3.3</version>
//</dependency>
//<dependency>
//<groupId>com.fasterxml.jackson.jaxrs</groupId>
//<artifactId>jackson-jaxrs-xml-provider</artifactId>
//<version>2.3.3</version>
//</dependency>
//<dependency>
//<groupId>org.glassfish.jersey.media</groupId>
//<artifactId>jersey-media-json-jackson</artifactId>
//<version>2.7</version>
//</dependency>
//<dependency>
//<groupId>org.glassfish.jersey.media</groupId>
//<artifactId>jersey-media-multipart</artifactId>
//<version>2.7</version>
//</dependency>
//<dependency>
//<groupId>com.googlecode.json-simple</groupId>
//<artifactId>json-simple</artifactId>
//<version>1.1.1</version>
//</dependency>
//<dependency>
//<groupId>com.jayway.restassured</groupId>
//<artifactId>rest-assured</artifactId>
//<version>2.3.1</version>
//<scope>test</scope>
//</dependency>
//<dependency>
//<groupId>org.seleniumhq.selenium</groupId>
//<artifactId>selenium-java</artifactId>
//<version>2.42.1</version>
//</dependency>
//<dependency>
//<groupId>org.testng</groupId>
//<artifactId>testng</artifactId>
//<version>6.1.1</version>
//<scope>test</scope>
//</dependency>
//
//<dependency>
//<groupId>org.glassfish.jersey.containers</groupId>
//<artifactId>jersey-container-servlet</artifactId>
//<version>2.22</version>
//</dependency>
//<dependency>
//<groupId>org.glassfish.jersey.ext</groupId>
//<artifactId>jersey-bean-validation</artifactId>
//<version>2.22</version>
//</dependency>
//<dependency>
//<groupId>org.glassfish.jersey.media</groupId>
//<artifactId>jersey-media-json-binding</artifactId>
//<version>3.0.2</version>
//</dependency>
//<dependency>
//<groupId>javax.json.bind</groupId>
//<artifactId>javax.json.bind-api</artifactId>
//<version>1.0</version>
//</dependency>
//
//<dependency>
//<groupId>org.eclipse</groupId>
//<artifactId>yasson</artifactId>
//<version>1.0</version>
//</dependency>
//
//<dependency>
//<groupId>org.glassfish</groupId>
//<artifactId>javax.json</artifactId>
//<version>1.1</version>
//</dependency>
//<dependency>
//<groupId>mysql</groupId>
//<artifactId>mysql-connector-java</artifactId>
//<version>6.0.6</version>
//</dependency>
//<dependency>
//<groupId>org.glassfish.jersey.inject</groupId>
//<artifactId>jersey-hk2</artifactId>
//<version>2.29.1</version>
//</dependency>
//<dependency>
//<groupId>org.glassfish.jersey.bundles</groupId>
//<artifactId>jaxrs-ri</artifactId>
//<version>2.7</version>
//</dependency>
//<dependency>
//<groupId>javax.ws.rs</groupId>
//<artifactId>javax.ws.rs-api</artifactId>
//<version>2.0.1</version>
//<scope>test</scope>
//</dependency>
//<dependency>
//<groupId>org.apache.cxf</groupId>
//<artifactId>cxf-rt-rs-client</artifactId>
//<version>3.0.2</version>
//<scope>test</scope>
//</dependency>
//<dependency>
//<groupId>org.codehaus.jackson</groupId>
//<artifactId>jackson-jaxrs</artifactId>
//<version>1.9.0</version>
//</dependency>
//
//
//
//
