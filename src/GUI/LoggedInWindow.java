package src.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
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
        JTextField nameField = new JTextField(20);
        JTextField categoryField = new JTextField(20);
        JTextField playTimeField = new JTextField(20);
        JTextField ageField = new JTextField(20);
        JTextField playersField = new JTextField(20);
        JTextField descriptionField = new JTextField(20);
        JTextField remarksField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JFileChooser fileChooser = new JFileChooser();

        JPanel inputPanel = new JPanel(new GridLayout(9, 2));
        inputPanel.add(new JLabel("Nazwa:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Kategoria:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Czas gry:"));
        inputPanel.add(playTimeField);
        inputPanel.add(new JLabel("Wiek:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Liczba graczy:"));
        inputPanel.add(playersField);
        inputPanel.add(new JLabel("Opis:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Uwagi:"));
        inputPanel.add(remarksField);
        inputPanel.add(new JLabel("Ilość:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Obraz (PNG):"));
        JButton chooseImageButton = new JButton("Wybierz obraz");
        inputPanel.add(chooseImageButton);

        chooseImageButton.addActionListener(e -> {
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                chooseImageButton.setText(selectedFile.getName());
                chooseImageButton.putClientProperty("imageFile", selectedFile);
            }
        });

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Dodaj nową grę", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String name = nameField.getText();
                String category = categoryField.getText();
                int playTime = Integer.parseInt(playTimeField.getText());
                int age = Integer.parseInt(ageField.getText());
                String players = playersField.getText();
                String description = descriptionField.getText();
                String remarks = remarksField.getText();
                int quantity = Integer.parseInt(quantityField.getText());
                File imageFile = (File) chooseImageButton.getClientProperty("imageFile");

                if (name.isEmpty() || category.isEmpty() || players.isEmpty() || description.isEmpty() || imageFile == null) {
                    throw new IllegalArgumentException("Wszystkie pola są wymagane!");
                }

                Object[] row = {imageFile != null ? imageFile.getName() : "<PNG>", name, category, playTime, age, players, description, remarks, quantity};
                tableModel.addRow(row);

                DatabaseOperations.insertBoardGame(name, category, playTime, age, players, description, remarks, quantity, imageFile);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Czas gry, wiek i ilość muszą być liczbami!", "Błąd", JOptionPane.ERROR_MESSAGE);
                addNewGameWithPrefilledData(nameField, categoryField, playTimeField, ageField, playersField, descriptionField, remarksField, quantityField, chooseImageButton);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                addNewGameWithPrefilledData(nameField, categoryField, playTimeField, ageField, playersField, descriptionField, remarksField, quantityField, chooseImageButton);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas dodawania gry: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void addNewGameWithPrefilledData(JTextField nameField, JTextField categoryField, JTextField playTimeField, JTextField ageField, JTextField playersField, JTextField descriptionField, JTextField remarksField, JTextField quantityField, JButton chooseImageButton) {
        JFileChooser fileChooser = new JFileChooser();

        JPanel inputPanel = new JPanel(new GridLayout(9, 2));
        inputPanel.add(new JLabel("Nazwa:"));
        inputPanel.add(nameField);
        inputPanel.add(new JLabel("Kategoria:"));
        inputPanel.add(categoryField);
        inputPanel.add(new JLabel("Czas gry:"));
        inputPanel.add(playTimeField);
        inputPanel.add(new JLabel("Wiek:"));
        inputPanel.add(ageField);
        inputPanel.add(new JLabel("Liczba graczy:"));
        inputPanel.add(playersField);
        inputPanel.add(new JLabel("Opis:"));
        inputPanel.add(descriptionField);
        inputPanel.add(new JLabel("Uwagi:"));
        inputPanel.add(remarksField);
        inputPanel.add(new JLabel("Ilość:"));
        inputPanel.add(quantityField);
        inputPanel.add(new JLabel("Obraz (PNG):"));
        inputPanel.add(chooseImageButton);

        chooseImageButton.addActionListener(e -> {
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                chooseImageButton.setText(selectedFile.getName());
                chooseImageButton.putClientProperty("imageFile", selectedFile);
            }
        });

        JOptionPane.showConfirmDialog(null, inputPanel, "Dodaj nową grę", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
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
}
