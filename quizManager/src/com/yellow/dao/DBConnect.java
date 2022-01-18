package com.yellow.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
	public static void main(String[] args) {
		// test connect database
		if (getConnect() != null)
			System.out.println("Connect ok");
	}

	public static Connection getConnect() {
		Connection conn = null;
		try {
			Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			String connectStr = "jdbc:sqlserver://localhost;databaseName=quizManager;user=sa;password=It123456!";
			conn = DriverManager.getConnection(connectStr);
		} catch (ClassNotFoundException cnfe) {
			// TODO Auto-generated catch block
			cnfe.printStackTrace();
		} catch (SQLException sqle) {
			// TODO Auto-generated catch block
			sqle.printStackTrace();
		}
		return conn;
	}
}
