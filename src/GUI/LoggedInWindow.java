package src.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import src.DatabaseOperations;

public class LoggedInWindow extends JFrame {
    private Controllers controllers;
    private DefaultTableModel tableModel;

    public LoggedInWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
        loadGamesFromDatabase();
    }

    private void createAndShowGUI() {
        setTitle("Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600); // Adjusted size for better table visibility

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navbar = controllers.createNavbar(this);
        mainPanel.add(navbar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        placeComponents(contentPanel);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
        
        System.out.println("GUI created and shown."); // Debugowanie
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // Table
        String[] columnNames = {"OBRAZ", "NAZWA", "KATEGORIA", "CZAS GRY", "WIEK", "L. graczy", "OPIS", "UWAGI"};
        tableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 0) {
                    return ImageIcon.class;
                } else {
                    return String.class;
                }
            }
        };
        JTable gameTable = new JTable(tableModel);
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        gameTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(gameTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        System.out.println("Table components placed."); // Debugowanie
    }

    private void loadGamesFromDatabase() {
        List<Object[]> games = DatabaseOperations.getAllBoardGames();

        for (Object[] game : games) {
            // Convert image bytes to ImageIcon
            byte[] imageBytes = (byte[]) game[0];
            ImageIcon imageIcon = null;
            if (imageBytes != null) {
                try {
                    ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                    BufferedImage bufferedImage = ImageIO.read(bais);
                    imageIcon = new ImageIcon(bufferedImage);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            game[0] = imageIcon;
            tableModel.addRow(game);
            System.out.println("Added game to table: " + game[1]); // Debugowanie
        }

        System.out.println("All games loaded into table.");
    }
}
