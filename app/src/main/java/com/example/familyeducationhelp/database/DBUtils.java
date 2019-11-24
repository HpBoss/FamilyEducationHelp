package com.example.familyeducationhelp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBUtils {
    public DBUtils() {
    }

    private static Connection getConn() {
        Connection connection = null;
        // MySql驱动
        try {
            Class.forName("com.mysql.jdbc.Driver");// 动态加载类
            connection = DriverManager.getConnection("jdbc:mysql://47.102.206.167:3306/feh_db",
                    "root", "Kobe199688.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean registerUser(String phone){
        Connection connection = getConn();
        Statement stmt = null;
        try {
             stmt = connection.createStatement();
             String sqlInsert = "insert into user(phoneN) values('"+ phone +"')";
             String sqlQuery = "select * from user where phoneN = '"+ phone +"'";
             ResultSet rs = stmt.executeQuery(sqlQuery);
             if (!rs.next()){
                 int count = stmt.executeUpdate(sqlInsert);
                 if (count > 0){
                     return true;
                 }
             }
        }catch (SQLException e){
            e.printStackTrace();
        }finally {
            try{
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
        return false;
    }
}
