package c195;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.ResultSetMetaData;

public class DBconnection {

    private static Connection databaseConn;

    public static void connect() {

        try {
            Class.forName("com.mysql.jdbc.Driver");

            databaseConn = DriverManager.getConnection("jdbc:mysql://52.206.157.109:3306/U03tpa?"
                    + "user=U03tpa&password=53688080971");

        } catch (ClassNotFoundException ce) {
            System.out.println("Add the right library to your compile");
            ce.printStackTrace();
        } catch (Exception e) {
            System.out.println("Something went wrong");
            e.printStackTrace();
        }

    }

    public static Connection getConnection() {
        return databaseConn;
    }

    public static void closeConnection() {
        if (databaseConn == null) {
            System.out.println("The connection is null");
        }
        try {
            databaseConn.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getInformation() {
        String sqlStatement = "SELECT * FROM user";
        try {
            PreparedStatement pst = databaseConn.prepareStatement(sqlStatement);
            ResultSet rst = pst.executeQuery();
            ResultSetMetaData rsmd = rst.getMetaData();

            int numCols = rsmd.getColumnCount();

            while (rst.next()) {
                for (int i = 1; i <= numCols; i++) {
                    String columnValue = rst.getString(i);
                    System.out.println(columnValue + " " + rsmd.getColumnName(i));
                }
            }

            pst.executeUpdate();

        } catch (SQLException se) {
            se.printStackTrace();
        }
    }

}
