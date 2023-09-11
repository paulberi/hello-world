<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
	
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
				<a href="http://www.javaguides.net" class="navbar-brand">Eshop
					Application</a>
			</div>
			<ul class="navbar-nav">
				<li><a href="index.jsp" class="nav-link">List Products</a></li>
			</ul>
		</nav>
	</header>
	<br>
	<div class="container col-md-5">
		<div class="card">
			<div class="card-body">
				<form method="post" action="ProductServlet">
				
					<fieldset class="form-group">
						<label>Product Id</label> <input type="text" id="productId"
							name="productId" required="required">
					</fieldset>

					<fieldset class="form-group">
						<label>Product Name</label> <input type="text" id="name"
							name="name" required="required">
					</fieldset>
					<fieldset class="form-group">
						<label>Price</label> <input type="number" id="price" name="price">
					</fieldset>
					<fieldset class="form-group">
						<label>Category</label> <input type="text" id="category"
							name="category">
					</fieldset>
					<fieldset class="form-group">
						<label>Quantity in Stock</label> <input type="number"
							id="quantityAvailable" name="quantityAvailable">
					</fieldset>

					<button type="submit">Save</button>
				</form>
			</div>
		</div>
	</div>

</body>
</html>