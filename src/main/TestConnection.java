package main;

import db.DBConnection;
import java.sql.*;

public class TestConnection {
    
    public static void main(String[] args) {
        System.out.println("==========================================");
        System.out.println("   DATABASE CONNECTION TEST");
        System.out.println("==========================================\n");
        
        Connection conn = DBConnection.getConnection();
        
        if (conn != null) {
            try {
                Statement stmt = conn.createStatement();
                
                // Test 1: Count Tables
                ResultSet rs = stmt.executeQuery(
                    "SELECT COUNT(*) as table_count FROM information_schema.tables " +
                    "WHERE table_schema = 'survey_db'"
                );
                if (rs.next()) {
                    System.out.println("📊 Total Tables: " + rs.getInt("table_count"));
                }
                
                // Test 2: Count Admins
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM admin");
                if (rs.next()) {
                    System.out.println("👨‍💼 Total Admins: " + rs.getInt("count"));
                }
                
                // Test 3: Count Users
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
                if (rs.next()) {
                    System.out.println("👥 Total Users: " + rs.getInt("count"));
                }
                
                // Test 4: Count Surveys
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM survey");
                if (rs.next()) {
                    System.out.println("📝 Total Surveys: " + rs.getInt("count"));
                }
                
                // Test 5: Count Categories
                rs = stmt.executeQuery("SELECT COUNT(*) as count FROM survey_categories");
                if (rs.next()) {
                    System.out.println("📁 Survey Categories: " + rs.getInt("count"));
                }
                
                // Test 6: Show Admin Details
                rs = stmt.executeQuery("SELECT username, email, role FROM admin LIMIT 1");
                if (rs.next()) {
                    System.out.println("\n🔑 Admin Login Credentials:");
                    System.out.println("   Username: " + rs.getString("username"));
                    System.out.println("   Role: " + rs.getString("role"));
                }
                
                System.out.println("\n✅ All Tests Passed!");
                System.out.println("==========================================");
                
                stmt.close();
                
            } catch (SQLException e) {
                System.err.println("\n❌ Error executing queries!");
                e.printStackTrace();
            }
        } else {
            System.err.println("\n❌ Connection Failed!");
        }
        
        DBConnection.closeConnection();
    }
}