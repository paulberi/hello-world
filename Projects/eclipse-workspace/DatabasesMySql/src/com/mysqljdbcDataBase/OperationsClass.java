package com.mysqljdbcDataBase;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OperationsClass {
	
	public static void functionality() throws ClassNotFoundException, IOException {

		//create a new connection from JDBCmysqlJDBCDatabase
		
		String sql="SELECT first_name, last_name,email "+"FROM candidates";
		
		try {
				Connection conn=JDBCmysqlJDBCDatabase.setConnection();
			Statement stmt=conn.createStatement();
			ResultSet rs= stmt.executeQuery(sql);
			
			while(rs.next()) {
				System.out.println(rs.getString("first_name")+"\t               "+
			rs.getString("last_name")+"\t         "+
			rs.getString("email"));
			}
			//print out a message
			System.out.println(String.format("Connected to database successfully "));
			
		}catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}
	}

}
