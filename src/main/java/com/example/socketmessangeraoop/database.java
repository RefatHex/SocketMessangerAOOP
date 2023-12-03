package com.example.socketmessangeraoop;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class database {
    public static Connection connectDB(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection("jdbc:mysql://localhost/easyneeds","root","refat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}