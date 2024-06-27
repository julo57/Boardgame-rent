package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    public static void insertBoardGame(String name, String category, int playTime, int age, String players, String description, String remarks, int quantity, File imageFile) {
        String sql = "INSERT INTO board_games(name, category, play_time, age, players, description, remarks, quantity, image) VALUES(?,?,?,?,?,?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             FileInputStream fis = new FileInputStream(imageFile)) {

            pstmt.setString(1, name);
            pstmt.setString(2, category);
            pstmt.setInt(3, playTime);
            pstmt.setInt(4, age);
            pstmt.setString(5, players);
            pstmt.setString(6, description);
            pstmt.setString(7, remarks);
            pstmt.setInt(8, quantity);
            pstmt.setBytes(9, fis.readAllBytes());

            pstmt.executeUpdate();
            System.out.println("Dodano grę planszową: " + name);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Object[]> getAllBoardGames() {
        String sql = "SELECT id, name, category, play_time, age, players, description, remarks, quantity, image FROM board_games";
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
                    rs.getInt("play_time"),
                    rs.getInt("age"),
                    rs.getString("players"),
                    rs.getString("description"),
                    rs.getString("remarks"),
                    rs.getInt("quantity"),
                    imageBytes
                };
                games.add(row);
                System.out.println("Loaded game: " + rs.getString("name")); // Debugowanie
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

    // Methods for handling rentals

    public static void insertRental(String userName, int gameId, String rentalDate, int quantity) {
        String sql = "INSERT INTO rentals(user_name, game_id, rental_date, quantity) VALUES(?,?,?,?)";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userName);
            pstmt.setInt(2, gameId);
            pstmt.setString(3, rentalDate);
            pstmt.setInt(4, quantity);

            pstmt.executeUpdate();
            System.out.println("Dodano wypożyczenie: " + userName + " - Game ID: " + gameId + " - Ilość: " + quantity);
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
                    rs.getInt("quantity")
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

    // Methods for handling users

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

    public static void deleteUser(int id) {
        String sql = "DELETE FROM users WHERE id = ?";

        try (Connection conn = DatabaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("Usunięto użytkownika o id: " + id);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
