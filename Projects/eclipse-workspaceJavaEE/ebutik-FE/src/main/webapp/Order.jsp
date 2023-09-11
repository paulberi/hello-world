<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.eshop.ft.beans.CustomerBean"%>
<%@ page import="java.util.List"%>
<%@page import="com.eshop.bt.to.CustomerTO"%>
<%@page import="com.eshop.bt.to.ProductTO"%>
<%@page import="com.eshop.ft.beans.ProductBean"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
</head>
<body>
	<header>
		<nav class="navbar navbar-expand-md navbar-dark"
			style="background-color: gray">

			<div>
				<a href="http://www.javaguides.net" class="navbar-brand">Eshop
					Application</a>
			</div>
			<ul class="navbar-nav">
				<li><a href="OrderList.jsp" class="nav-link">List of Orders</a></li>
			</ul>
			<ul class="navbar-nav">
				<li><a href="index.jsp" class="nav-link">List of Products</a></li>
			</ul>
		</nav>
	</header>
	<br>

	<div class="container col-md-5">
		<div class="card">
			<div class="card-body">

				<form action="OrderServlet" method="post">
				
					<fieldset class="form-group">
						<label>Order Id</label> <input type="text" id="orderId" placeholder="incremental from 1"
							name="orderId" required="required">
					</fieldset>
					
					<fieldset class="form-group">
						<label>Customer Id</label> <input type="number" id="customerId"
							name="customerId" required="required" placeholder="max 3">
					</fieldset>

					<fieldset class="form-group">
						<label>Product Name</label>
						
									<%
									ProductTO productTO = new ProductTO();
									List<ProductBean> products = productTO.getAllProducts();
									for (ProductBean prod : products) {
									%>
									<table>
									<tbody>
										<tr>
											<td>
												
						<input type="checkbox" name="productName" id="productName" value="<%=prod.getName()%>" />
						<option><%out.print(prod.getName()); %></option>
											</td>
						
						<%}	%>
					</fieldset>
									</table>
					<button type="submit" class="btn btn-success">Save</button>
				</form>
			</div>
		</div>
	</div>


</body>
</html>