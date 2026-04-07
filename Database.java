// File: DatabaseManager.md
import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:museum.db";
    private static DatabaseManager instance;
    private Connection conn;

    private DatabaseManager() {
        init();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void init() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            // Create minimal tables
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("CREATE TABLE IF NOT EXISTS visitors (membershipID TEXT PRIMARY KEY, name TEXT, isMember BOOLEAN)");
                stmt.execute("CREATE TABLE IF NOT EXISTS reservations (id INTEGER PRIMARY KEY, visitorName TEXT, timeSlot TEXT, resTime DATETIME DEFAULT CURRENT_TIMESTAMP)");
                stmt.execute("CREATE TABLE IF NOT EXISTS occupancy (id INTEGER PRIMARY KEY, action TEXT, count INTEGER, ts DATETIME DEFAULT CURRENT_TIMESTAMP)");
            }
            System.out.println("✅ SQLite DB ready (museum.db)");
        } catch (Exception e) {
            System.err.println("DB init error: " + e.getMessage());
        }
    }

    // Save visitor
    public boolean saveVisitor(Visitor v) {
        String sql = "INSERT OR REPLACE INTO visitors VALUES(?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, v.getMembershipID());
            ps.setString(2, v.getName());
            ps.setBoolean(3, v.isMember());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Save visitor failed: " + e.getMessage());
            return false;
        }
    }

    // Save reservation
    public boolean saveReservation(String name, String slot) {
        String sql = "INSERT INTO reservations (visitorName, timeSlot) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, slot);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Save reservation failed: " + e.getMessage());
            return false;
        }
    }

    // Log occupancy change
    public boolean logOccupancy(String action, int count) {
        String sql = "INSERT INTO occupancy (action, count) VALUES (?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, action);
            ps.setInt(2, count);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Log occupancy failed: " + e.getMessage());
            return false;
        }
    }

    // Optional: Quick view of reservations
    public void showReservations() {
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM reservations ORDER BY resTime DESC LIMIT 10")) {
            System.out.println("\n=== Recent Reservations ===");
            while (rs.next()) {
                System.out.println(rs.getString("visitorName") + " → " + rs.getString("timeSlot"));
            }
        } catch (SQLException e) {
            System.err.println("Show reservations error: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (SQLException e) {
            // ignore
        }
    }
}
