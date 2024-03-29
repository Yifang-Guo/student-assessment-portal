package org.example.demo5;

/**
 * Database configuration information. Please replace with your own DB_URL and PASSWORD.
 * @author Yifang
 * @version 1.0
 **/
public class DBConfig {
    // JDBC driver name and database URL
    static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    public static final String DB_URL = "jdbc:mysql://localhost:3306/coursesdb3?autoReconnect=true&useSSL=false";

    // Database Credentials
    public static final String USER = "root";
    public static final String PASSWORD = "root";
}
