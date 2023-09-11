package com.mysqljdbcDataBase;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.mysql.cj.jdbc.Driver;

public class JDBCmysqlJDBCDatabase {

	/*
	 * @author mysqltutorial.org
	 * 
	 * @return a Connection object
	 * 
	 * @throws SQLException
	 * 
	 ***/

	public static Connection setConnection() throws SQLException, ClassNotFoundException, IOException {

		Connection conn = null;
		try {
			String url = "jdbc:mysql://localhost:3306/mysqljdbc?serverTimezone=UTC";
			String username = "root";
			String password = "Myfans83";

			// create a connection to the database

			conn = DriverManager.getConnection(url, username, password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return conn;

	}

}
