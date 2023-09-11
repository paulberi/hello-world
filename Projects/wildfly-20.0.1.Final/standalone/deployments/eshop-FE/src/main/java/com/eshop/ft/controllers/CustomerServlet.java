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
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		String customerId= request.getParameter("customerId");
		String name =request.getParameter("name");
		String email =request.getParameter("email");
		String password =request.getParameter("password");
		String address =request.getParameter("address");
		
		
		Customer customer= new Customer();
		customer.setCustomerId(Integer.parseInt(customerId));
		customer.setName(name);
		customer.setEmail(email);
		customer.setPassword(password);
		customer.setAddress(address);
		
		customerTO.postCustomer(customer);
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("/WEB-INF/Views/CustomerGet.jsp");
		dispatcher.forward(request, response);;
		doGet(request, response);
	}

}













//
//<div align="center">
//<h1>New Customer</h1>
//<form action="<%=request.getContextPath()%>/CustomerServlet" method="post">
//<table style="with:80%">
//<tr>
//<td>Customer ID</td>
//<td><input type="text" name ="customerId"/></td>
//</tr>
//<tr>
//<td>Full Name</td>
//<td><input type="text" name ="name"/></td>
//</tr>
//<tr>
//<td>Email</td>
//<td><input type="text" name ="email"/></td>
//</tr>
//<tr>
//<td>Password</td>
//<td><input type="password" name ="password"/></td>
//</tr>
//<tr>
//<td>Address</td>
//<td><input type="text" name ="address"/></td>
//</tr>
//<tr>
//<td></td>
//<td><input type="submit" value ="customer"/></td>
//</tr>
//</table>
//</form>
//</div>
//
//



//from doGet method
//int customerId=Integer.parseInt(request.getParameter("customerId"));
//CustomerTO customerTO=new CustomerTO();
//CustomerBean customer =customerTO.getCustomer(customerId);
//System.out.println(customer.getCustomerId()+ " is the id");
//
//request.setAttribute("customer", customer);
//RequestDispatcher dispatcher = request.getRequestDispatcher("CustomerGet.jsp");
//dispatcher.forward(request, response);
