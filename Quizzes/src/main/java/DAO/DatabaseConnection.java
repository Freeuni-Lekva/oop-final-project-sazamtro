package DAO;

import java.sql.*;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/sazamtro";  //Use your database!!!
    private static final String ACCOUNT = "root";
    private static final String PASSWORD = "2334";      //Change to yours!!!!!



    public static Connection getConnection(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, ACCOUNT, PASSWORD);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
