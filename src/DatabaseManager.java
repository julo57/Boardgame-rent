package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:database.db";

    public static void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS board_games (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL,\n"
                + " category TEXT,\n"
                + " play_time INTEGER,\n"
                + " age INTEGER,\n"
                + " players TEXT,\n"
                + " description TEXT,\n"
                + " remarks TEXT,\n"
                + " image BLOB\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'board_games' created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createRentalTable() {
        String url = "jdbc:sqlite:your-database-name.db";

        String sql = "CREATE TABLE IF NOT EXISTS rentals (\n"
                + "	id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + "	user_name TEXT NOT NULL,\n"
                + "	game_name TEXT NOT NULL,\n"
                + "	rental_date TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'rentals' created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
