package src.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

        // Search Bar
        JTextField searchBar = new JTextField("WYSZUKIWARKA (GLOBALNA NA WSZYSTKIE LISTY)");
        searchBar.setHorizontalAlignment(JTextField.CENTER);
        searchBar.setPreferredSize(new Dimension(1000, 30));
        panel.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"OBRAZ", "NAZWA", "KATEGORIA", "CZAS GRY", "WIEK", "L. graczy", "OPIS", "UWAGI", "ILOŚĆ"};
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

        // Add game and user buttons
        JPanel buttonPanel = new JPanel();
        JButton addGameButton = new JButton("DODAJ GRĘ");
        JButton addUserButton = new JButton("DODAJ UŻYTKOWNIKA");
        JButton addRentalButton = new JButton("DODAJ WYPOŻYCZENIE");
        buttonPanel.add(addGameButton);
        buttonPanel.add(addUserButton);
        buttonPanel.add(addRentalButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add functionality to add game button
        addGameButton.addActionListener(e -> addNewGame());

        // Add functionality to add user button
        addUserButton.addActionListener(e -> addNewUser());

        // Add functionality to add rental button
        addRentalButton.addActionListener(e -> addNewRental());

        System.out.println("Table components placed."); // Debugowanie
    }

    private void loadGamesFromDatabase() {
        List<Object[]> games = DatabaseOperations.getAllBoardGames();

        for (Object[] game : games) {
            // Convert image bytes to ImageIcon
            byte[] imageBytes = (byte[]) game[9];
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
            game[9] = imageIcon;
            tableModel.addRow(game);
            System.out.println("Added game to table: " + game[1]); // Debugowanie
        }

        System.out.println("All games loaded into table.");
    }

    private void addNewGame() {
        List<Object[]> games = DatabaseOperations.getAllBoardGames();

        String[] gameNames = games.stream()
                .map(game -> (String) game[1])
                .toArray(String[]::new);

        JComboBox<String> gameComboBox = new JComboBox<>(gameNames);
        JTextField userNameField = new JTextField(20);
        JTextField quantityField = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Wybierz grę:"));
        inputPanel.add(gameComboBox);
        inputPanel.add(new JLabel("Nazwa użytkownika:"));
        inputPanel.add(userNameField);
        inputPanel.add(new JLabel("Ilość sztuk:"));
        inputPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Dodaj nowe wypożyczenie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String selectedGame = (String) gameComboBox.getSelectedItem();
            String userName = userNameField.getText();
            int quantity = Integer.parseInt(quantityField.getText());

            if (selectedGame != null && !userName.isEmpty()) {
                // Znajdź ID gry na podstawie nazwy
                int gameId = games.stream()
                        .filter(game -> selectedGame.equals(game[1]))
                        .map(game -> (Integer) game[0])
                        .findFirst()
                        .orElse(-1);

                if (gameId != -1) {
                    String rentalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    DatabaseOperations.insertRental(userName, gameId, rentalDate, quantity);
                    System.out.println("Dodano wypożyczenie: " + userName + " - " + selectedGame + " o " + rentalDate + " - Ilość: " + quantity);
                } else {
                    JOptionPane.showMessageDialog(this, "Nie znaleziono gry w bazie danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Wszystkie pola są wymagane!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addNewUser() {
        JTextField userNameField = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Nazwa użytkownika:"));
        inputPanel.add(userNameField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Dodaj nowego użytkownika", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String userName = userNameField.getText();

            if (!userName.isEmpty()) {
                DatabaseOperations.insertUser(userName);
                System.out.println("Dodano użytkownika: " + userName);
            } else {
                JOptionPane.showMessageDialog(this, "Nazwa użytkownika jest wymagana!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addNewRental() {
        List<Object[]> games = DatabaseOperations.getAllBoardGames();
        List<Object[]> users = DatabaseOperations.getAllUsers();

        String[] gameNames = games.stream()
                .map(game -> (String) game[1])
                .toArray(String[]::new);
        String[] userNames = users.stream()
                .map(user -> (String) user[1])
                .toArray(String[]::new);

        JComboBox<String> gameComboBox = new JComboBox<>(gameNames);
        JComboBox<String> userComboBox = new JComboBox<>(userNames);
        JTextField quantityField = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));
        inputPanel.add(new JLabel("Wybierz grę:"));
        inputPanel.add(gameComboBox);
        inputPanel.add(new JLabel("Wybierz użytkownika:"));
        inputPanel.add(userComboBox);
        inputPanel.add(new JLabel("Ilość sztuk:"));
        inputPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Dodaj nowe wypożyczenie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            String selectedGame = (String) gameComboBox.getSelectedItem();
            String selectedUser = (String) userComboBox.getSelectedItem();
            int quantity = Integer.parseInt(quantityField.getText());

            if (selectedGame != null && selectedUser != null && quantity > 0) {
                // Znajdź ID gry na podstawie nazwy
                int gameId = games.stream()
                        .filter(game -> selectedGame.equals(game[1]))
                        .map(game -> (Integer) game[0])
                        .findFirst()
                        .orElse(-1);

                if (gameId != -1) {
                    String rentalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                    DatabaseOperations.insertRental(selectedUser, gameId, rentalDate, quantity);
                    System.out.println("Dodano wypożyczenie: " + selectedUser + " - " + selectedGame + " o " + rentalDate + " - Ilość: " + quantity);
                } else {
                    JOptionPane.showMessageDialog(this, "Nie znaleziono gry w bazie danych.", "Błąd", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(this, "Wszystkie pola są wymagane i ilość musi być większa od zera!", "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
