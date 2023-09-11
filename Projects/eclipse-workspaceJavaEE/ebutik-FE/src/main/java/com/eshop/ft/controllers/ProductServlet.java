package com.eshop.ft.controllers;

import java.io.IOException;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.eshop.bt.to.CustomerTO;
import com.eshop.bt.to.ProductTO;
import com.eshop.bt.vo.Product;
import com.eshop.ft.beans.CustomerBean;
import com.eshop.ft.beans.ProductBean;



/**
 * Servlet implementation class ProductServlet
 */
@WebServlet("/ProductServlet")
public class ProductServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private ProductTO productTO;
	
	 public void init() throws ServletException
	    {
	    	productTO= new ProductTO();
	    	HttpServletRequest request = null;
	    	HttpServletResponse response = null;
	    	try {
				listProducts(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	    }
       

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
		//getServletContext().getRequestDispatcher("/Product.jsp").forward(request, response);
		 
			
		
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub

		long productId=Long.parseLong(request.getParameter("productId"));
		String name=request.getParameter("name");
		double price=Double.parseDouble(request.getParameter("price"));
		String category=request.getParameter("category");
		int quantityAvailable=Integer.parseInt(request.getParameter("quantityAvailable"));
		
		Product product=new Product();
		product.setName(name);
		product.setPrice(price);
		product.setCategory(category);
		product.setProductId(productId);
		product.setQuantityAvailable(quantityAvailable);
		
		if(name==null || price==0 || category==null || quantityAvailable==0 || productId==0) {
			request.setAttribute("error", "You are missing of the inputs");
			doGet(request, response);
		}else {
			ProductBean prod= productTO.postProduct(product);
			System.out.println("Product has been created");
			System.out.println(prod.getName());
			response.sendRedirect("index.jsp");
			
		}
	}
	private void listProducts(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		
		try {
			
			List<ProductBean> productList = productTO.getAllProducts();
			for(ProductBean prod:productList) {
				System.out.println(prod.getProductId()+" with name "+prod.getName());
			}
			request.setAttribute("productList", productList);
			RequestDispatcher dispatcher = request.getRequestDispatcher("index.jsp");
			dispatcher.forward(request, response);
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
		

}
