package com.mycompany.museum;

import java.sql.*;
import java.util.ArrayList;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:" + System.getProperty("user.dir") + "/museum.db";
    
    public void initializeDatabase() {
        String createReservationsTable = """
            CREATE TABLE IF NOT EXISTS reservations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                visitor_name TEXT NOT NULL,
                membership_id TEXT,
                is_member INTEGER NOT NULL,
                time_slot TEXT NOT NULL,
                reservation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String createWaitingQueueTable = """
            CREATE TABLE IF NOT EXISTS waiting_queue (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                visitor_name TEXT NOT NULL,
                group_size INTEGER NOT NULL,
                joined_queue TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        String createOccupancyLogTable = """
            CREATE TABLE IF NOT EXISTS occupancy_log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                occupancy INTEGER NOT NULL,
                log_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
        """;
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            
            stmt.execute(createReservationsTable);
            stmt.execute(createWaitingQueueTable);
            stmt.execute(createOccupancyLogTable);
            
            System.out.println("Database initialized at: " + System.getProperty("user.dir") + "/museum.db");
        } catch (SQLException e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
    
    public void saveReservation(Visitor visitor, String timeSlot) {
        String sql = "INSERT INTO reservations (visitor_name, membership_id, is_member, time_slot) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, visitor.getName());
            pstmt.setString(2, visitor.getMembershipID());
            pstmt.setInt(3, visitor.isMember() ? 1 : 0);
            pstmt.setString(4, timeSlot);
            pstmt.executeUpdate();
            
            System.out.println("Reservation saved to database.");
        } catch (SQLException e) {
            System.err.println("Error saving reservation: " + e.getMessage());
        }
    }
    
    public ArrayList<String> loadReservations() {
        ArrayList<String> reservations = new ArrayList<>();
        String sql = "SELECT visitor_name, time_slot, reservation_date FROM reservations ORDER BY reservation_date DESC";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                String reservation = rs.getString("visitor_name") + " - " + 
                                   rs.getString("time_slot") + " (" + 
                                   rs.getString("reservation_date") + ")";
                reservations.add(reservation);
            }
        } catch (SQLException e) {
            System.err.println("Error loading reservations: " + e.getMessage());
        }
        
        return reservations;
    }
    
    public void saveToWaitingQueue(String name, int groupSize) {
        String sql = "INSERT INTO waiting_queue (visitor_name, group_size) VALUES (?, ?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, name);
            pstmt.setInt(2, groupSize);
            pstmt.executeUpdate();
            
            System.out.println("Queue entry saved to database.");
        } catch (SQLException e) {
            System.err.println("Error saving to queue: " + e.getMessage());
        }
    }
    
    public void logOccupancy(int occupancy) {
        String sql = "INSERT INTO occupancy_log (occupancy) VALUES (?)";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setInt(1, occupancy);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error logging occupancy: " + e.getMessage());
        }
    }
    
    public void displayAllReservations() {
        String sql = "SELECT * FROM reservations ORDER BY reservation_date DESC";
        
        System.out.println("\n--- Database Reservations ---");
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            boolean hasResults = false;
            while (rs.next()) {
                hasResults = true;
                System.out.println("ID: " + rs.getInt("id"));
                System.out.println("  Visitor: " + rs.getString("visitor_name"));
                System.out.println("  Member: " + (rs.getInt("is_member") == 1 ? "Yes" : "No"));
                System.out.println("  Time Slot: " + rs.getString("time_slot"));
                System.out.println("  Date: " + rs.getString("reservation_date"));
                System.out.println("  ---");
            }
            
            if (!hasResults) {
                System.out.println("No reservations in database.");
            }
        } catch (SQLException e) {
            System.err.println("Error displaying reservations: " + e.getMessage());
        }
    }
    
    public int getTotalReservationCount() {
        String sql = "SELECT COUNT(*) as count FROM reservations";
        
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            return rs.getInt("count");
        } catch (SQLException e) {
            System.err.println("Error getting count: " + e.getMessage());
            return 0;
        }
    }
}
