package com.jsf.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.jsf.DAO.AlienDAO;
import com.jsf.entity.Alien;

/**
 * Servlet implementation class GetAlienController
 */
//@WebServlet("/getAlien")
public class GetAlienController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetAlienController() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		//response.getWriter().append("Served at: ").append(request.getContextPath());
		
		int aid=Integer.parseInt(request.getParameter("aid"));
		System.out.println("hello");
		AlienDAO dao= new AlienDAO();
		Alien a1= dao.getAlien(aid);
		System.out.println(a1);
		
		HttpSession session = request.getSession();
		session.setAttribute("alien", a1);
		
		response.sendRedirect("ShowAlien.jsp");
		//RequestDispatcher rd = request.getRequestDispatcher("ShowAlien.jsp");
		//rd.forward(request, response);
		
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
