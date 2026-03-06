package gym.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBInitializer {

    public static void init(String url) {
        try {
            Class.forName("org.sqlite.JDBC");

            try (Connection conn = DriverManager.getConnection(url);
                 Statement stmt = conn.createStatement()) {

                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS clients (
                        id INTEGER PRIMARY KEY,
                        name TEXT NOT NULL,
                        email TEXT NOT NULL,
                        phone TEXT NOT NULL
                    );
                """);

                stmt.executeUpdate("""
                    CREATE TABLE IF NOT EXISTS sessions (
                        id INTEGER PRIMARY KEY,
                        client_id INTEGER NOT NULL,
                        date_time TEXT NOT NULL,
                        description TEXT NOT NULL,
                        FOREIGN KEY (client_id) REFERENCES clients(id)
                    );
                """);

                System.out.println(" Database initialized successfully.");

            } catch (Exception e) {
                System.err.println("Error initializing database: " + e.getMessage());
            }
        } catch (ClassNotFoundException e) {
            System.err.println(" Critical Error: SQLite JDBC Driver (org.sqlite.JDBC) not found. Verify JAR inclusion.");
        }
    }
}