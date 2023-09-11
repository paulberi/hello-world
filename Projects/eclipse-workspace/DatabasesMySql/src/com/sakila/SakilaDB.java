package com.sakila;

import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SakilaDB {
	public static Connection getConnection() throws SQLException, ClassNotFoundException {

	Connection conn=null;
	
	Class.forName(Driver.class.getName());

	//create a connection to the database
	conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/sakila", "root", "Myfans83");
	
/*	Statement st=conn.createStatement();
	ResultSet rs=st.executeQuery("select * from candidates;");
	
	while(rs.next()) {
		System.out.println(rs.getString( "id"  + "first_name"  +  " last_name"  + "dob" +" phone " +"email"));
	}*/
	return conn;
	
}


}
