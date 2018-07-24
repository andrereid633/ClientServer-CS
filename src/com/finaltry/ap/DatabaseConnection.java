package com.finaltry.ap;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

	public Connection getConnection(){
		final String DRIVER ="com.mysql.jdbc.Driver";
		final String DB_PORT = "3306";
		final String DB_NAME = "ARDDB";
		final String PASSWORD = "";
		final String USERNAME = "root";
		final String URL = "jdbc:mysql://localhost:" + DB_PORT + "/" + DB_NAME;
		
		try{
			Class.forName(DRIVER);
			Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			return connection;
		}catch(SQLException e){
			System.err.println("SIGN-IN-USER: " + e.getMessage());
		} catch (ClassNotFoundException e){
			System.err.println("SIGN-IN-USER: " + e.getMessage());
		}
		return null;
	}
}
