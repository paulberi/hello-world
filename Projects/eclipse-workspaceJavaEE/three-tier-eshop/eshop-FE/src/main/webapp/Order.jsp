<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
</head>
<body>

<h2 align="center">Selected Product Details</h2>

<hr width=300 >

<%

String[] dropdownValues = request.getParameterValues("dropdown");

int count=1;

if(dropdownValues!=null){

%>

<table align="center" >

<tr>

<th>S.No.</th>

<th>Product Name</th>

</tr>

<%

for(int i = 0; i < dropdownValues.length; i++){

if(!dropdownValues[i].equals("All")){

%>

<tr>

<td>

<% out.println(""+count+" . "); %>

</td>

<td>

<% out.println(dropdownValues[i]); %>

</td>

</tr>

<%

count++;

}

}

%>

</table>

<% }else{ %>

<table align="center" >

<tr>

<td><% out.println("You not select any Product."); %></td>

</tr>

</table>

<% } %>

</body>
</html>