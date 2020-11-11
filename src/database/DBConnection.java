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
    
    public static void executeUpdate(String query) throws SQLException {
        statement.executeUpdate(query);
    }
    
    public static ResultSet executeQuery(String query) throws SQLException {
        return statement.executeQuery(query);
    }
    
    public static int getIntResult(String query) throws SQLException {
        ResultSet rs = statement.executeQuery(query);
        rs.next();
        return rs.getInt(1);
    }
    
    public static void createConnection() throws ClassNotFoundException, SQLException {
        Class.forName("oracle.jdbc.driver.OracleDriver");
        String url = "jdbc:oracle:thin:@localhost:1521:XE";
        String username = "HR";
        String password = "hr";
        connection = DriverManager.getConnection(url, username, password);
        statement = connection.createStatement();
    }
}