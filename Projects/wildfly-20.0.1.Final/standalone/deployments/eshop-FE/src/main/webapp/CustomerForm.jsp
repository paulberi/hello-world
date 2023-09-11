<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
			<a href="http://www.javaguides.net" class="navbar-brand">Eshop Application</a>
		</div>
		<ul class ="navbar-nav">
			<li><a href="<%=request.getContextPath() %>/list" 
			class ="nav-link">Customers</a></li>
		</ul>
		</nav>
	</header>
	<br>
	
	<div class="container col-md-5">
		<div class="card">
			<div class="card-body">
				<c:if test="${customer !=null }">
					<form action= "update" method ="post">
				</c:if>
				
				<c:if test="${customer ==null }">
					<form action="insert" method="post">
				</c:if>
				
				<caption>
					<h2>
						<c:if test="${customer!=null }">Edit User</c:if>
						<c:if test="${customer==null }">Add New User</c:if>
					</h2>
				</caption>
				<c:if test="${customer !=null }">
					<input type="hidden" name="customerId" value="<c:out value='${customer.customerId }'/>"/>
				</c:if>
				
				<fieldset class="form-group">
					<label>Customer Name</label> <input type="text" 
					value="<c:out value='${customer.name }'/>" class="form-control" name="name" required="required">
				</fieldset>
				<fieldset class="form-group">
					<label>Customer Email</label> <input type="text" 
					value="<c:out value='${customer.email }'/>" class="form-control" name="email">
				</fieldset>
				<fieldset class="form-group">
					<label>Customer Password</label> <input type="text" 
					value="<c:out value='${customer.password }'/>" class="form-control" name="password">
				</fieldset>
				<fieldset class="form-group">
					<label>Customer Address</label> <input type="text" 
					value="<c:out value='${customer.address }'/>" class="form-control" name="address">
				</fieldset>
				
				<button type="submit" class="btn btn-success">Save</button>
				</form>
			</div>
		</div>
	</div>

</body>
</html>