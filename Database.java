// Database.java
import java.sql.*;
import java.util.*;

public class Database {

    private static Connection conn;
    private static final String DB_URL = "jdbc:sqlite:museum.db";

    public static void connect() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            createTables();
            System.out.println("Connected to SQLite database (museum.db)");
        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    private static void createTables() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS time_slots (id INTEGER PRIMARY KEY, time TEXT UNIQUE, capacity INTEGER)");
            stmt.execute("CREATE TABLE IF NOT EXISTS bookings (slot_time TEXT, visitor_name TEXT)");
            stmt.execute("CREATE TABLE IF NOT EXISTS reservations (id INTEGER PRIMARY KEY, name TEXT, group_size INTEGER, slot TEXT, membership TEXT, timestamp DATETIME DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS wait_queue (id INTEGER PRIMARY KEY, name TEXT, group_size INTEGER, arrival DATETIME DEFAULT CURRENT_TIMESTAMP)");
            stmt.execute("CREATE TABLE IF NOT EXISTS occupancy (id INTEGER PRIMARY KEY CHECK(id=1), count INTEGER DEFAULT 0)");

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM time_slots");
            if (rs.next() && rs.getInt(1) == 0) {
                initializeDefaultSlots(stmt);
            }
        }
    }

    private static void initializeDefaultSlots(Statement stmt) throws SQLException {
        String[] slots = {
            "INSERT INTO time_slots (time, capacity) VALUES ('9:00 AM - 10:00 AM', 2)",
            "INSERT INTO time_slots (time, capacity) VALUES ('10:00 AM - 11:00 AM', 2)",
            "INSERT INTO time_slots (time, capacity) VALUES ('11:00 AM - 12:00 PM', 2)",
            "INSERT INTO time_slots (time, capacity) VALUES ('1:00 PM - 2:00 PM', 3)",
            "INSERT INTO time_slots (time, capacity) VALUES ('2:00 PM - 3:00 PM', 3)",
            "INSERT INTO time_slots (time, capacity) VALUES ('3:00 PM - 4:00 PM', 3)"
        };
        for (String sql : slots) stmt.execute(sql);
    }

    public static void close() {
        try {
            if (conn != null && !conn.isClosed()) conn.close();
        } catch (Exception ignored) {}
    }

    // Time Slots
    public static List<String[]> getAllTimeSlots() {
        List<String[]> slots = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT time, capacity FROM time_slots ORDER BY id")) {
            while (rs.next()) {
                String time = rs.getString("time");
                int capacity = rs.getInt("capacity");
                int booked = getBookedCount(time);
                slots.add(new String[]{time, String.valueOf(capacity), String.valueOf(booked)});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return slots;
    }

    private static int getBookedCount(String time) {
        try (PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM bookings WHERE slot_time = ?")) {
            ps.setString(1, time);
            ResultSet rs = ps.executeQuery();
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static boolean assignToSlot(String time, String visitorName) {
        int capacity = 0;
        try (PreparedStatement ps = conn.prepareStatement("SELECT capacity FROM time_slots WHERE time = ?")) {
            ps.setString(1, time);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) capacity = rs.getInt("capacity");
        } catch (Exception e) {
            return false;
        }

        if (getBookedCount(time) >= capacity) return false;

        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO bookings (slot_time, visitor_name) VALUES (?, ?)")) {
            ps.setString(1, time);
            ps.setString(2, visitorName);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // Reservations
    public static void saveReservation(String name, int groupSize, String slot, String membership) {
        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO reservations (name, group_size, slot, membership) VALUES (?, ?, ?, ?)")) {
            ps.setString(1, name);
            ps.setInt(2, groupSize);
            ps.setString(3, slot.isEmpty() ? "Auto-assigned" : slot);
            ps.setString(4, membership.isEmpty() ? "N/A" : membership);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getAllReservations() {
        List<String[]> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, group_size, slot, membership FROM reservations ORDER BY id")) {
            while (rs.next()) {
                list.add(new String[]{
                    rs.getString("name"),
                    String.valueOf(rs.getInt("group_size")),
                    rs.getString("slot"),
                    rs.getString("membership")
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static void removeReservation(String name) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM reservations WHERE name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Wait Queue
    public static void addVisitorToQueue(String name, int groupSize) {
        try (PreparedStatement ps = conn.prepareStatement("INSERT INTO wait_queue (name, group_size) VALUES (?, ?)")) {
            ps.setString(1, name);
            ps.setInt(2, groupSize);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeVisitorFromQueue(String name) {
        try (PreparedStatement ps = conn.prepareStatement("DELETE FROM wait_queue WHERE name = ?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static List<String[]> getCurrentQueue() {
        List<String[]> list = new ArrayList<>();
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name, group_size FROM wait_queue ORDER BY arrival")) {
            while (rs.next()) {
                list.add(new String[]{rs.getString("name"), String.valueOf(rs.getInt("group_size"))});
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }

    public static int getTotalPeopleWaiting() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT COALESCE(SUM(group_size), 0) FROM wait_queue")) {
            return rs.next() ? rs.getInt(1) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void serveNextVisitor() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, name, group_size FROM wait_queue ORDER BY arrival LIMIT 1")) {
            if (rs.next()) {
                int id = rs.getInt("id");
                String name = rs.getString("name");
                int groupSize = rs.getInt("group_size");
                System.out.println("Now serving: " + name + " (Group of " + groupSize + ")");
                try (PreparedStatement ps = conn.prepareStatement("DELETE FROM wait_queue WHERE id = ?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }
            } else {
                System.out.println("No visitors in queue.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Occupancy
    public static int getCurrentOccupancy() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT count FROM occupancy WHERE id=1")) {
            return rs.next() ? rs.getInt("count") : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    public static void updateOccupancy(int newValue) {
        try (PreparedStatement ps = conn.prepareStatement("UPDATE occupancy SET count = ? WHERE id=1")) {
            ps.setInt(1, Math.max(0, newValue));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
                         }
