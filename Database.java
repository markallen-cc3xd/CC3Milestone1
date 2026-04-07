// File: DatabaseManager.md
import java.sql.*;

public class DatabaseManager {

    private static final String DB_URL = "jdbc:sqlite:museum.db";
    private static DatabaseManager instance;
    private Connection conn;

    private DatabaseManager() {
        initDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    private void initDatabase() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);

            // Only one table needed for reservations
            String sql = """
                CREATE TABLE IF NOT EXISTS reservations (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    visitorName TEXT NOT NULL,
                    timeSlot TEXT NOT NULL,
                    membershipID TEXT,
                    resTime DATETIME DEFAULT CURRENT_TIMESTAMP
                )
                """;

            try (Statement stmt = conn.createStatement()) {
                stmt.execute(sql);
            }

            System.out.println("Reservation Database ready (museum.db)");

        } catch (Exception e) {
            System.err.println("Database error: " + e.getMessage());
        }
    }

    // Save a new reservation
    public boolean saveReservation(Visitor visitor, String timeSlot) {
        String sql = "INSERT INTO reservations (visitorName, timeSlot, membershipID) VALUES (?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, visitor.getName());
            ps.setString(2, timeSlot);
            ps.setString(3, visitor.getMembershipID());
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Failed to save reservation: " + e.getMessage());
            return false;
        }
    }

    // Optional: Show recent reservations
    public void showRecentReservations() {
        String sql = "SELECT visitorName, timeSlot, resTime FROM reservations ORDER BY resTime DESC LIMIT 5";
        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            System.out.println("\n=== Recent Reservations ===");
            while (rs.next()) {
                System.out.printf("%s → %s (%s)%n", 
                    rs.getString("visitorName"), 
                    rs.getString("timeSlot"),
                    rs.getString("resTime"));
            }
        } catch (SQLException e) {
            System.err.println("Could not load reservations: " + e.getMessage());
        }
    }

    public void close() {
        try {
            if (conn != null) conn.close();
        } catch (Exception ignored) {}
    }
}
