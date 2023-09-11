<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="com.eshop.ft.beans.OrderBean"%>
<%@ page import="java.util.List"%>
<%@page import="com.eshop.bt.to.OrderTO"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>
<body>
	<header>
		<nav class="navbar navbar-expand-md navbar-dark"
			style="background-color: gray">

			<div>
				<a href="http://www.javaguides.net" class="navbar-brand">Eshop
					Application</a>
			</div>
		</nav>
	</header>
	<div class="row">

		<div class="container">
			<div class="container text-left">

				<a href="Order.jsp" class="btn btn-success">Make an order</a>
			</div>
			<br>

			<h3 class="text-center">List of Orders</h3>
			<hr>
			<br>

			<table class="table table-bordered">

				<thead>
					<tr>
						<th>OrderId</th>
						<th>CustomerId</th>
					</tr>
				</thead>
				<%
				OrderTO orderTO = new OrderTO();
				List<OrderBean> orderList = orderTO.getAllOrders();
				for (OrderBean order : orderList) {
					out.print("order id is:"+order.getOrderId());
				%>
				<tbody>
					<tr>
						<td>
							<%out.print(order.getOrderId());%>
						</td>
						<td>
							<%out.print(order.getCustomerId());%>
						</td>
					</tr>
					<%
					}
					%>
				</tbody>
			</table>


		</div>
</body>
</body>
</html>