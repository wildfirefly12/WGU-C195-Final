package util;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/*
 * Establishes a connection to the database
 */
public class DBConnection {

    //JDBC
    private static final String protocol = "jdbc";
    private static final String vendorName = ":mysql:";
    private static final String ipAddress = "//3.227.166.251/";
    private static final String dbName = "U05eL7";

    //Concatenate JDBC URL
    private static final String jdbcUrl = protocol + vendorName + ipAddress + dbName;

    //Driver & connection interface reference
    private static final String driver = "com.mysql.cj.jdbc.Driver";
    private static Connection conn = null;

    //Username & password
    private static final String userName = "U05eL7";
    private static final String password = "53688481799";

    //connection
    public static Connection openConnection(){
        try {
            Class.forName(driver);
            conn = (Connection) DriverManager.getConnection(jdbcUrl, userName, password);
            System.out.println("Connection successful"); //Not printing for some reason!!!!!!!!
        } catch(ClassNotFoundException e){
            System.out.println(e.getMessage());
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }

        return conn;
    }

    public static void closeConnection(){
        try{
            conn.close();
            System.out.println("Connection closed");
        } catch(SQLException e){
            System.out.println(e.getMessage());
        }
    }

}
