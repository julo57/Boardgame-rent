package src;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:database.db";

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL);
            System.out.println("Połączenie z SQLite zostało nawiązane.");
        } catch (SQLException e) {
            System.out.println("Błąd podczas nawiązywania połączenia: " + e.getMessage());
        }
        return conn;
    }

    public static void createSectionTable(String sectionName) {
        String sql = "CREATE TABLE IF NOT EXISTS " + sectionName + " (\n"
                + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "    article TEXT,\n"
                + "    word TEXT NOT NULL,\n"
                + "    translation TEXT NOT NULL\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela " + sectionName + " została utworzona.");
        } catch (SQLException e) {
            System.out.println("Błąd podczas tworzenia tabeli: " + e.getMessage());
        }
    }

    
}
