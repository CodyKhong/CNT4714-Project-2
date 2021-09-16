/*
Name: Cody Khong
Course: CNT 4714 Summer 2021
Assignment title: Project 2 Two-Tier Client-Server Application Development With MySQL and JDBC”
Date: July 4, 2021
Class: ResultSetTableModel.java
*/
import java.sql.*;
import java.util.Properties;

import java.io.FileInputStream;
import java.io.IOException;

import javax.swing.table.AbstractTableModel;

import com.mysql.cj.jdbc.MysqlDataSource;

public class ResultSetTableModel extends AbstractTableModel {
	private Connection connection;
	private Statement statement;
	private ResultSet resultSet;
	private ResultSetMetaData metaData;
	private int numberOfRows;

	private boolean connectedToDatabase = false;

	public ResultSetTableModel(String query, Connection connection) throws SQLException, ClassNotFoundException {
		try {

			statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

			connectedToDatabase = true;

			setQuery(query);

		} 
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
			System.exit(1);
		} 
	} 

	public ResultSetTableModel(String text, Object establishConnection) {
		// TODO Auto-generated constructor stub
	}

	public Class getColumnClass(int column) throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// determine Java class of column
		try {
			String className = metaData.getColumnClassName(column + 1);

			// return Class object that represents className
			return Class.forName(className);
		} // end try
		catch (Exception exception) {
			exception.printStackTrace();
		} // end catch

		return Object.class; // if problems occur above, assume type Object
	} // end method getColumnClass

	// get number of columns in ResultSet
	public int getColumnCount() throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// determine number of columns
		try {
			return metaData.getColumnCount();
		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} // end catch

		return 0; // if problems occur above, return 0 for number of columns
	} // end method getColumnCount

	// get name of a particular column in ResultSet
	public String getColumnName(int column) throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// determine column name
		try {
			return metaData.getColumnName(column + 1);
		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} // end catch

		return "";
	} 

	// return number of rows in ResultSet
	public int getRowCount() throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		return numberOfRows;
	} // end method getRowCount

	// obtain value in particular row and column
	public Object getValueAt(int row, int column) throws IllegalStateException {
		// ensure database connection is available
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		// obtain a value at specified ResultSet row and column
		try {
			resultSet.next(); /* fixes a bug in MySQL/Java with date format */
			resultSet.absolute(row + 1);
			return resultSet.getObject(column + 1);
		} // end try
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} // end catch

		return "";
	} 

	public void setQuery(String query) throws SQLException, IllegalStateException {

		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");

		resultSet = statement.executeQuery(query);
		
	    Properties properties = new Properties();
	    FileInputStream filein = null;
	    MysqlDataSource dataSource = null;	
		 try {
		    	filein = new FileInputStream("oplog.properties");
		    	properties.load(filein);
		    	dataSource = new MysqlDataSource();
		    	dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
		    	dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME")); 	
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }
	 
		Connection connection = dataSource.getConnection();
		Statement statement = connection.createStatement();
		
	    // Execute a statement
		int result = statement.executeUpdate("UPDATE operationscount SET num_queries = num_queries + 1");
	    statement.close();
	    connection.close();
	    
		metaData = resultSet.getMetaData();

		resultSet.last(); // move to last row
		numberOfRows = resultSet.getRow(); // get row number
	    

		// notify JTable that model has changed
		fireTableStructureChanged();
	} // end method setQuery

// set new database update-query string
	public void setUpdate(String query) throws SQLException, IllegalStateException {
		int res;
		if (!connectedToDatabase)
			throw new IllegalStateException("Not Connected to Database");
		res = statement.executeUpdate(query);
		
	    Properties properties = new Properties();
	    FileInputStream filein = null;
	    MysqlDataSource dataSource = null;	
		 try {
		    	filein = new FileInputStream("oplog.properties");
		    	properties.load(filein);
		    	dataSource = new MysqlDataSource();
		    	dataSource.setURL(properties.getProperty("MYSQL_DB_URL"));
		    	dataSource.setUser(properties.getProperty("MYSQL_DB_USERNAME"));	 	
		    } catch (IOException e) {
		    	e.printStackTrace();
		    }


		Connection connection = dataSource.getConnection();
		Statement statement = connection.createStatement();
		
	    // Execute a statement
		int result = statement.executeUpdate("UPDATE operationscount SET num_updates = num_ + 1");
	    statement.close();
	    connection.close();
	    
		// notify JTable that model has changed
		fireTableStructureChanged();
	}


	public void disconnectFromDatabase() {
		if (!connectedToDatabase)
			return;

		try {
			statement.close();
			connection.close();
		} 
		catch (SQLException sqlException) {
			sqlException.printStackTrace();
		} 
		finally 
		{
			connectedToDatabase = false;
		} 
	}
}