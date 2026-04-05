package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    
    // Database Configuration
    private static final String URL = "jdbc:mysql://localhost:3306/survey_db";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "shr123";  // Your password
    
    private static Connection connection = null;

    // Get Connection Method
    public static Connection getConnection() {
        try {
            // Load MySQL Driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            
            // Establish Connection
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            
            System.out.println("✅ Database Connected Successfully!");
            
        } catch (ClassNotFoundException e) {
            System.err.println("❌ MySQL Driver not found!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("❌ Database Connection Failed!");
            e.printStackTrace();
        }
        
        return connection;
    }
    
    // Close Connection Method
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Database Connection Closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}