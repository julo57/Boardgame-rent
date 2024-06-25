package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseOperations {

    public static void insertBoardGame(String name, String category, int playTime, int age, String players, String description, String remarks, File imageFile) {
        String sql = "INSERT INTO board_games(name, category, play_time, age, players, description, remarks, image) VALUES(?,?,?,?,?,?,?,?)";

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
            pstmt.setBytes(8, fis.readAllBytes());

            pstmt.executeUpdate();
            System.out.println("Dodano grę planszową: " + name);
        } catch (SQLException | IOException e) {
            System.out.println(e.getMessage());
        }
    }

    public static List<Object[]> getAllBoardGames() {
        String sql = "SELECT image, name, category, play_time, age, players, description, remarks FROM board_games";
        List<Object[]> games = new ArrayList<>();

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                byte[] imageBytes = rs.getBytes("image");
                Object[] row = {
                    imageBytes,  // Image bytes
                    rs.getString("name"),
                    rs.getString("category"),
                    rs.getInt("play_time"),
                    rs.getInt("age"),
                    rs.getString("players"),
                    rs.getString("description"),
                    rs.getString("remarks")
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
}
