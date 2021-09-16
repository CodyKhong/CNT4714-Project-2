/*
Name: Cody Khong
Course: CNT 4714 Summer 2021
Assignment title: Project 2 Two-Tier Client-Server Application Development With MySQL and JDBC”
Date: July 4, 2021
Class: jdbc.java
*/

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class jdbc {
	private String DataBaseURL;
	private String DataBaseUsername;
	private String DataBasePassword;
	String mQuery = null;
	private Connection connection = null;

	public jdbc(String Driver, String username, String password) {
		this.DataBaseURL = Driver;
		this.DataBaseUsername = username;
		this.DataBasePassword = password;
	}


	public void EstablishConnection() throws SQLException, ClassNotFoundException {

		connection = DriverManager.getConnection(this.DataBaseURL, this.DataBaseUsername, this.DataBasePassword);
	}

	public void CloseConnection() throws SQLException {
		connection.close();
	}

	public Connection getConnection() {
		return this.connection;
	}

}