<%@page import="com.eshop.ft.beans.CustomerBean"%>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!DOCTYPE html>


<html>
<head>
<meta charset="UTF-8">
<title>Eshop Application</title>
<link rel="stylesheet"
	href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
	integrity="sha384-ggOyROiXCbMQv3Xipma34MD+dH/1fQ784/J6cY/iJTQUOhcWr7x9JvoRxT2MZw1T"
	crossorigin="anonymous">
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
				<li><a href="<%=request.getContextPath()%>/list"
					class="nav-link">Customers</a></li>
			</ul>
		</nav>
	</header>
	<br>

	<div class="row">

		<div class="container">
			<h3 class="text-center">List of Customers</h3>
			<hr>
			<div class="container text-left">

				<a href="<%=request.getContextPath()%>/new" class="btn btn-success">Add
					new customer</a>
			</div>
			<br>

			<table class="table table-bordered">

				<thead>
					<tr>
						<th>CustomerId</th>
						<th>Name</th>
						<th>Email</th>
						<th>Password</th>
						<th>Address</th>
						<th>Actions</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="customer" items="${customerList}" >
						<tr>
							<td><c:out value="${customer.customerId }"/></td>
							<td><c:out value="${customer.name}" /></td>
							<td><c:out value="${customer.email }" /></td>
							<td><c:out value="${customer.password }" /></td>
							<td><c:out value="${customer.address }" /></td>
							<td><a
								href="edit?customerId=<c:out value='${customer.customerId }' />">Edit</a>
								&nbsp;&nbsp;&nbsp;&nbsp; <a
								href="delete?customerId=<c:out value ='${customer.customerId }' />">Delete</a></td>
						</tr>
					</c:forEach>

				</tbody>
			</table>


		</div>
	</div>
</body>
</html>