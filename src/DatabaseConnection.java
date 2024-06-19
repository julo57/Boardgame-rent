package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
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

    public static void createNewTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + "    id integer PRIMARY KEY AUTOINCREMENT,\n"
                + "    name text NOT NULL\n"
                + ");";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Tabela users została utworzona.");
        } catch (SQLException e) {
            System.out.println("Błąd podczas tworzenia tabeli: " + e.getMessage());
        } catch (NullPointerException e) {
            System.out.println("Połączenie jest null, nie można utworzyć tabeli.");
        }
    }
}
