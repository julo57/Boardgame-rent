package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    public static void insertBoardGame(String name, String category, String playTime, String age, String players, String description, String remarks, String quantity, File imageFile) {
        String sql = "INSERT INTO board_games(name, category, play_time, age, players, description, remarks, quantity, total_quantity, image) VALUES(?,?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(imageFile)) {

            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setString(3, playTime);
            pstmt.setString(4, age);
            pstmt.setString(5, players);
            pstmt.setString(6, description);
            pstmt.setString(7, remarks);
            pstmt.setString(8, quantity);
            pstmt.setString(9, quantity); // Set total_quantity the same as initial quantity
            pstmt.setBytes(10, fis.readAllBytes());

            pstmt.executeUpdate();
            System.out.println("Dodano grę planszową: " + name);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Object[]> getAllBoardGames() {
        String sql = "SELECT id, name, category, play_time, age, players, description, remarks, quantity, total_quantity, image FROM board_games";
        List<Object[]> games = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                byte[] imageBytes = rs.getBytes("image");
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getString("play_time"),
                    rs.getString("age"),
                    rs.getString("players"),
                    rs.getString("description"),
                    rs.getString("remarks"),
                    rs.getString("quantity"),
                    rs.getString("total_quantity"),
                    imageBytes  // Image bytes
                };
                games.add(row);
                System.out.println("Loaded game: " + rs.getString("name")); // Debugging
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return games;
    }

    public static void deleteBoardGame(int id) {
        String sql = "DELETE FROM board_games WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Usunięto grę planszową o id: " + id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertRental(String userName, String gameId, String rentalDate, String quantity) {
        String sql = "INSERT INTO rentals(user_name, game_id, rental_date, quantity) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            pstmt.setString(2, gameId);
            pstmt.setString(3, rentalDate);
            pstmt.setString(4, quantity);

            pstmt.executeUpdate();
            System.out.println("Dodano wypożyczenie: " + userName + " - Game ID: " + gameId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Object[]> getAllRentals() {
        String sql = "SELECT rentals.id, rentals.user_name, board_games.name AS game_name, rentals.rental_date, rentals.quantity "
                + "FROM rentals "
                + "JOIN board_games ON rentals.game_id = board_games.id";
        List<Object[]> rentals = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("user_name"),
                    rs.getString("game_name"),
                    rs.getString("rental_date"),
                    rs.getString("quantity"),
                    "Oddaj"  // Button text for actions
                };
                rentals.add(row);
                System.out.println("Loaded rental: " + rs.getString("user_name") + " - " + rs.getString("game_name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return rentals;
    }

    public static void deleteRental(int id) {
        String sql = "DELETE FROM rentals WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Usunięto wypożyczenie o id: " + id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void returnRental(String rentalId, String returnDate) {
        String insertHistorySql = "INSERT INTO rental_history (id, user_name, game_id, rental_date, return_date, quantity) "
                + "SELECT id, user_name, game_id, rental_date, ?, quantity FROM rentals WHERE id = ?";
        String deleteRentalSql = "DELETE FROM rentals WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement insertStmt = conn.prepareStatement(insertHistorySql);
             PreparedStatement deleteStmt = conn.prepareStatement(deleteRentalSql)) {

            conn.setAutoCommit(false);

            // Insert into rental_history table
            insertStmt.setString(1, returnDate);
            insertStmt.setString(2, rentalId);
            insertStmt.executeUpdate();

            // Delete from rentals table
            deleteStmt.setString(1, rentalId);
            deleteStmt.executeUpdate();

            conn.commit();
            System.out.println("Rental returned and moved to history: " + rentalId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Object[]> getAllRentalHistory() {
        String sql = "SELECT rental_history.id, rental_history.user_name, board_games.name AS game_name, "
                + "rental_history.rental_date, rental_history.return_date, rental_history.quantity "
                + "FROM rental_history "
                + "JOIN board_games ON rental_history.game_id = board_games.id";
        List<Object[]> history = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("user_name"),
                    rs.getString("game_name"),
                    rs.getString("rental_date"),
                    rs.getString("return_date"),
                    rs.getString("quantity")
                };
                history.add(row);
                System.out.println("Loaded rental history: " + rs.getString("user_name") + " - " + rs.getString("game_name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return history;
    }

    public static void insertUser(String userName) {
        String sql = "INSERT INTO users(name) VALUES(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            pstmt.executeUpdate();
            System.out.println("Dodano użytkownika: " + userName);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static void insertUserIfNotExists(String userName) {
        String sqlCheck = "SELECT id FROM users WHERE name = ?";
        String sqlInsert = "INSERT INTO users(name) VALUES(?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmtCheck = conn.prepareStatement(sqlCheck);
             PreparedStatement pstmtInsert = conn.prepareStatement(sqlInsert)) {

            pstmtCheck.setString(1, userName);
            ResultSet rs = pstmtCheck.executeQuery();
            if (!rs.next()) {
                pstmtInsert.setString(1, userName);
                pstmtInsert.executeUpdate();
                System.out.println("Dodano użytkownika: " + userName);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Object[]> getAllUsers() {
        String sql = "SELECT id, name FROM users";
        List<Object[]> users = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("name")
                };
                users.add(row);
                System.out.println("Loaded user: " + rs.getString("name"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return users;
    }

    public static int getGameIdByName(String gameName) {
        String sql = "SELECT id FROM board_games WHERE name = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, gameName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }

    public static void updateGameQuantity(int gameId, int newQuantity) {
        String sql = "UPDATE board_games SET quantity = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, newQuantity);
            pstmt.setInt(2, gameId);
            pstmt.executeUpdate();
            System.out.println("Zaktualizowano ilość gry o id: " + gameId);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int getGameTotalQuantity(int gameId) {
        String sql = "SELECT total_quantity FROM board_games WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Integer.parseInt(rs.getString("total_quantity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public static int getGameQuantity(int gameId) {
        String sql = "SELECT quantity FROM board_games WHERE id = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, gameId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return Integer.parseInt(rs.getString("quantity"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return 0;
    }
    
    public static int getUserIdByName(String userName) {
        String sql = "SELECT id FROM users WHERE name = ?";
        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return -1;
    }
}
