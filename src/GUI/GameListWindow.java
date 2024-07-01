package src.GUI;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.regex.PatternSyntaxException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableRowSorter;
import src.DatabaseOperations;

public class GameListWindow extends JFrame {
    private Controllers controllers;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> sorter;

    public GameListWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
        loadGamesFromDatabase();
    }

    private void createAndShowGUI() {
        setTitle("Game List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navbar = controllers.createNavbar(this);
        mainPanel.add(navbar, BorderLayout.NORTH);

        // Content panel to hold all other components
        JPanel contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        placeComponents(contentPanel);

        setContentPane(mainPanel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
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
        sorter = new TableRowSorter<>(tableModel);
        gameTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(gameTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Sort and Filter Panel
        JPanel sortFilterPanel = new JPanel(new GridLayout(1, 2));

        JPanel sortPanel = new JPanel(new GridLayout(2, 1));
        sortPanel.setBorder(BorderFactory.createTitledBorder("SORTUJ"));
        String[] sortOptions = {"NAZWA", "DATA WYDANIA", "CZAS GRY", "OCENA", "LICZBA GRACZY"};
        JComboBox<String> sortComboBox = new JComboBox<>(sortOptions);
        sortPanel.add(new JLabel("Sortuj według:"));
        sortPanel.add(sortComboBox);
        sortFilterPanel.add(sortPanel);

        JPanel filterPanel = new JPanel(new GridLayout(2, 1));
        filterPanel.setBorder(BorderFactory.createTitledBorder("FILTRUJ"));
        String[] filterOptions = {"LICZBA GRACZY", "KATEGORIA WIEKOWA", "TYP GRY"};
        JComboBox<String> filterComboBox = new JComboBox<>(filterOptions);
        filterPanel.add(new JLabel("Filtruj według:"));
        filterPanel.add(filterComboBox);
        sortFilterPanel.add(filterPanel);

        panel.add(sortFilterPanel, BorderLayout.EAST);

        // Add Game Button
        JPanel addGamePanel = new JPanel();
        JButton addGameButton = new JButton("DODAJ GRĘ");
        addGameButton.setPreferredSize(new Dimension(150, 30));
        addGamePanel.add(addGameButton);
        panel.add(addGamePanel, BorderLayout.SOUTH);

        // Add functionality to add game button
        addGameButton.addActionListener(e -> addNewGame());

        // Add functionality to sort and filter
        sortComboBox.addActionListener(e -> {
            String selectedOption = (String) sortComboBox.getSelectedItem();
            sortTable(selectedOption);
        });

        filterComboBox.addActionListener(e -> {
            String selectedOption = (String) filterComboBox.getSelectedItem();
            filterTable(selectedOption);
        });

        // Search bar functionality
        searchBar.addActionListener(e -> {
            String text = searchBar.getText();
            if (text.trim().length() == 0) {
                sorter.setRowFilter(null);
            } else {
                sorter.setRowFilter(RowFilter.regexFilter("(?i)" + text));
            }
        });
    }

    private void loadGamesFromDatabase() {
        List<Object[]> games = DatabaseOperations.getAllBoardGames();

        for (Object[] game : games) {
            // Convert image bytes to ImageIcon
            ImageIcon imageIcon = null;
            if (game[10] instanceof byte[]) {
                byte[] imageBytes = (byte[]) game[10];
                if (imageBytes != null) {
                    try {
                        ByteArrayInputStream bais = new ByteArrayInputStream(imageBytes);
                        BufferedImage bufferedImage = ImageIO.read(bais);
                        imageIcon = new ImageIcon(bufferedImage);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            Object[] row = {imageIcon, game[1], game[2], game[3], game[4], game[5], game[6], game[7], game[8]};
            tableModel.addRow(row);
            System.out.println("Added game to table: " + game[1]); // Debugging
        }

        System.out.println("All games loaded into table.");
    }

    private void sortTable(String sortBy) {
        int columnIndex = -1;
        switch (sortBy) {
            case "NAZWA":
                columnIndex = 1;
                break;
            case "DATA WYDANIA":
                columnIndex = 2;
                break;
            case "CZAS GRY":
                columnIndex = 3;
                break;
            case "OCENA":
                columnIndex = 4;
                break;
            case "LICZBA GRACZY":
                columnIndex = 5;
                break;
        }
        if (columnIndex != -1) {
            sorter.setSortKeys(List.of(new RowSorter.SortKey(columnIndex, SortOrder.ASCENDING)));
        }
    }

    private void filterTable(String filterBy) {
        RowFilter<DefaultTableModel, Object> rf = null;
        try {
            switch (filterBy) {
                case "LICZBA GRACZY":
                    rf = RowFilter.regexFilter(".*", 5); // Adjust the column index as necessary
                    break;
                case "KATEGORIA WIEKOWA":
                    rf = RowFilter.regexFilter(".*", 4); // Adjust the column index as necessary
                    break;
                case "TYP GRY":
                    rf = RowFilter.regexFilter(".*", 2); // Adjust the column index as necessary
                    break;
            }
            sorter.setRowFilter(rf);
        } catch (PatternSyntaxException e) {
            e.printStackTrace();
        }
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
                String playTime = playTimeField.getText();
                String age = ageField.getText();
                String players = playersField.getText();
                String description = descriptionField.getText();
                String remarks = remarksField.getText();
                String quantity = quantityField.getText();
                File imageFile = (File) chooseImageButton.getClientProperty("imageFile");

                if (name.isEmpty() || category.isEmpty() || players.isEmpty() || description.isEmpty() || imageFile == null) {
                    throw new IllegalArgumentException("Wszystkie pola są wymagane!");
                }

                Object[] row = {imageFile != null ? new ImageIcon(imageFile.getPath()) : "<PNG>", name, category, playTime, age, players, description, remarks, quantity};
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
}
