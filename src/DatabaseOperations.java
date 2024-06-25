package src;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseOperations {

    // Existing user-related methods...

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

    public static void selectAllBoardGames() {
        String sql = "SELECT id, name, category, play_time, age, players, description, remarks FROM board_games";

        try (Connection conn = DatabaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                System.out.println(
                    "ID: " + rs.getInt("id") +
                    ", Name: " + rs.getString("name") +
                    ", Category: " + rs.getString("category") +
                    ", Play Time: " + rs.getInt("play_time") +
                    ", Age: " + rs.getInt("age") +
                    ", Players: " + rs.getString("players") +
                    ", Description: " + rs.getString("description") +
                    ", Remarks: " + rs.getString("remarks")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
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

    public static void main(String[] args) {
        // Example usage:
        File imageFile = new File("path/to/your/image.png"); // Replace with the actual path to your image
        insertBoardGame("Example Game", "Strategy", 60, 12, "2-4 players", "A fun strategy game", "No remarks", imageFile);
        selectAllBoardGames();
        deleteBoardGame(1); // Replace with the actual ID to delete
    }
}
