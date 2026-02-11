package com.club.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class DBUtil {

	public static Connection getDBConnection() {
	    Connection con = null;
	    try {
	        Class.forName("oracle.jdbc.driver.OracleDriver");
	        con = DriverManager.getConnection(
	            "jdbc:oracle:thin:@localhost:1521:xe",
	            "system",
	            "veni862006"
	        );
	        con.setAutoCommit(false);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return con;
	}

}
