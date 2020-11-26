package database;

import java.sql.*;

public class DBConnection {
    private static Connection connection;
    private static Statement statement;
    
    private DBConnection() {
    }
    
    public static Connection getConnection() {
        return connection;
    }
    
    public static void executeUpdate(String query) {
        try {
            statement.executeUpdate(query);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
    }
    
    public static ResultSet executeQuery(String query) {
        ResultSet resultSet = null;
        try {
            resultSet = statement.executeQuery(query);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return resultSet;
    }
    
    public static int getIntResult(String query) {
        int result = -1;
        try {
            ResultSet rs = statement.executeQuery(query);
            rs.next();
            result = rs.getInt(1);
        } catch (SQLException sqlException) {
            System.out.println(sqlException.getMessage());
        }
        return result;
    }
    
    public static void createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "GMS";
        String password = "gms";
        connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
    }
}