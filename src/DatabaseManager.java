package src;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {

    private static final String URL = "jdbc:sqlite:database.db";

    public static void createTables() {
        // Drop existing tables
        

        // Create new tables
        createBoardGamesTable();
        createUsersTable();
        createRentalTable();
        createRentalHistoryTable(); // Add this line to create the rental_history table
    }

    public static void dropTables() {
        String dropBoardGamesTable = "DROP TABLE IF EXISTS board_games;";
        String dropUsersTable = "DROP TABLE IF EXISTS users;";
        String dropRentalsTable = "DROP TABLE IF EXISTS rentals;";
        String dropRentalHistoryTable = "DROP TABLE IF EXISTS rental_history;"; // Add this line to drop the rental_history table

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(dropBoardGamesTable);
            stmt.execute(dropUsersTable);
            stmt.execute(dropRentalsTable);
            stmt.execute(dropRentalHistoryTable); // Add this line to execute the drop statement for rental_history
            System.out.println("Existing tables dropped successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createBoardGamesTable() {
        String sql = "CREATE TABLE IF NOT EXISTS board_games (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL,\n"
                + " category TEXT,\n"
                + " play_time TEXT,\n"
                + " age TEXT,\n"
                + " players TEXT,\n"
                + " description TEXT,\n"
                + " remarks TEXT,\n"
                + " quantity TEXT,\n"
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

    public static void createUsersTable() {
        String sql = "CREATE TABLE IF NOT EXISTS users (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " name TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'users' created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createRentalTable() {
        String sql = "CREATE TABLE IF NOT EXISTS rentals (\n"
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
                + " user_name TEXT NOT NULL,\n"
                + " game_id INTEGER NOT NULL,\n"
                + " rental_date TEXT NOT NULL,\n"
                + " quantity TEXT NOT NULL,\n"
                + " FOREIGN KEY (game_id) REFERENCES board_games(id)\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'rentals' created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void createRentalHistoryTable() {
        String sql = "CREATE TABLE IF NOT EXISTS rental_history (\n"
                + " id INTEGER PRIMARY KEY,\n"
                + " user_name TEXT NOT NULL,\n"
                + " game_id INTEGER NOT NULL,\n"
                + " rental_date TEXT NOT NULL,\n"
                + " return_date TEXT,\n"
                + " quantity TEXT NOT NULL\n"
                + ");";

        try (Connection conn = DriverManager.getConnection(URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
            System.out.println("Table 'rental_history' created successfully.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
