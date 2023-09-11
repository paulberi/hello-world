package com.sakila;

import java.sql.Connection;
import java.sql.SQLException;

public class Main {

	public static void main(String[] args) throws ClassNotFoundException {
		// TODO Auto-generated method stub
		//create a new connection from JDBCmysqlJDBCDatabase
		String url="jbdc:mysql://localhost:3306/sakila";
		String user="root";
		String password="Myfans83";
		
		try(Connection conn=SakilaDB.getConnection()){
			
			//print out a message
			System.out.println(String.format("Connected to database successfully ",conn.getCatalog()));
			
		}catch(SQLException ex) {
			System.out.println(ex.getMessage());
		}

	}

}
