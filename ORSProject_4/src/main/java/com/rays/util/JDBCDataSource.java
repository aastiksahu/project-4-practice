package com.rays.util;

import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public final class JDBCDataSource {

	private JDBCDataSource() {

	}

	private static JDBCDataSource datasource = null;
	private static ComboPooledDataSource cpds = null;
	private static ResourceBundle rb = ResourceBundle.getBundle("com.rays.bundle.system");

	private static JDBCDataSource getInstance() {

		if (datasource == null) {

			datasource = new JDBCDataSource();
			datasource.cpds = new ComboPooledDataSource();

			try {
				datasource.cpds.setDriverClass(rb.getString("driver"));
				datasource.cpds.setJdbcUrl(rb.getString("url"));
				datasource.cpds.setUser(rb.getString("username"));
				datasource.cpds.setPassword(rb.getString("password"));
				datasource.cpds.setMinPoolSize(Integer.parseInt(rb.getString("minPoolSize")));
				datasource.cpds.setMaxPoolSize(Integer.parseInt(rb.getString("maxPoolSize")));
				datasource.cpds.setInitialPoolSize(Integer.parseInt(rb.getString("initialPoolSize")));
				datasource.cpds.setAcquireIncrement(Integer.parseInt(rb.getString("acquireIncrement")));
				datasource.cpds.setMaxIdleTime(Integer.parseInt(rb.getString("timeout")));
			} catch (PropertyVetoException e) {

				e.printStackTrace();
			}

		}
		return datasource;
	}

	public static Connection getConnection() throws SQLException {
		
		return getInstance().cpds.getConnection();
		
	}

	public static void closeConnection(Connection connection) {

		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void trnRollback(Connection connection) {
		
		if (connection != null) {
			try {
				connection.rollback();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
