<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page import="com.eshop.ft.beans.ProductBean"%>
<%@ page import="java.util.List"%>
<%@page import="com.eshop.bt.to.ProductTO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
	<header>
		<nav class="navbar navbar-expand-md navbar-dark"
			style="background-color: gray">

			<div>
				<a>Eshop Application</a>
			</div>
		</nav>
	</header>
	<div class="row">

		<div class="container">
			<div class="container text-left">

				<ul class="navbar-nav">
					<li><a href="Order.jsp" class="nav-link">Make an Order</a></li>
				</ul>
				<ul class="navbar-nav">
					<li><a href="Product.jsp" class="nav-link">Create a
							product</a></li>
				</ul>
				<ul class="navbar-nav">
					<li><a href="OrderList.jsp" class="nav-link">List of
							Orders</a></li>
				</ul>
			</div>
			<br>

			<h3 class="text-center">List of Products</h3>
			<hr>
			<br>

			<table class="table table-bordered">

				<thead>
					<tr>
						<th>          Product Id</th>
						<th>Name</th>
						<th>Price</th>
						<th>Category</th>
						<th>Stock</th>
					</tr>
				</thead>
				<%
				ProductTO productTO = new ProductTO();
				List<ProductBean> productList = productTO.getAllProducts();
				for (ProductBean prod : productList) {
				%>
				<tbody>
					<tr>
						<td>
							<%out.print(prod.getProductId());%>
						</td>
						<td>
							<%out.print(prod.getName());%>
						</td>
						<td>
							<%out.print(prod.getPrice());%>
						</td>
						<td>
							<%out.print(prod.getCategory());%>
						</td>
						<td>
							<%out.print(prod.getQuantityAvailable());%>
						</td>
					</tr>
					<%
					}
					%>



				</tbody>
			</table>


		</div>
</body>
</html>
