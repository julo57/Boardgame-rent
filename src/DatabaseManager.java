package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:database.db";

    public static void createTables() {
        String createBoardGamesTable = "CREATE TABLE IF NOT EXISTS board_games (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL,\n"
                + " category TEXT,\n"
                + " play_time INTEGER,\n"
                + " age INTEGER,\n"
                + " players TEXT,\n"
                + " description TEXT,\n"
                + " remarks TEXT,\n"
                + " quantity INTEGER,\n"
                + " image BLOB\n"
                + ");";

        String createUsersTable = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL\n"
                + ");";

        String createRentalsTable = "CREATE TABLE IF NOT EXISTS rentals (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " user_name TEXT NOT NULL,\n"
                + " game_id INTEGER NOT NULL,\n"
                + " rental_date TEXT NOT NULL,\n"
                + " quantity INTEGER NOT NULL,\n"
                + " FOREIGN KEY (game_id) REFERENCES board_games(id)\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(createBoardGamesTable);
            stmt.execute(createUsersTable);
            stmt.execute(createRentalsTable);
            System.out.println("Tables 'users', 'board_games', and 'rentals' created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
