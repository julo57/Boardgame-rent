package src.GUI;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
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
        loadRentalsFromDatabase();
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

        System.out.println("GUI created and shown."); // Debugging
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // Search Bar
        JTextField searchBar = new JTextField("WYSZUKIWARKA (GLOBALNA NA WSZYSTKIE LISTY)");
        searchBar.setHorizontalAlignment(JTextField.CENTER);
        searchBar.setPreferredSize(new Dimension(1000, 30));
        panel.add(searchBar, BorderLayout.NORTH);

        // Table
        String[] columnNames = {"ID", "Nazwa Użytkownika", "Nazwa Gry", "Data Wypożyczenia", "Ilość", "Akcje"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable rentalTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the "Akcje" column is editable
            }
        };
        TableRowSorter<DefaultTableModel> sorter = new TableRowSorter<>(tableModel);
        rentalTable.setRowSorter(sorter);
        JScrollPane scrollPane = new JScrollPane(rentalTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Add rental button
        JPanel buttonPanel = new JPanel();
        JButton addRentalButton = new JButton("DODAJ WYPOŻYCZENIE");
        buttonPanel.add(addRentalButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        // Add functionality to add rental button
        addRentalButton.addActionListener(e -> addNewRental());

        // Add functionality for "Oddaj" button in the "Akcje" column
        rentalTable.getColumnModel().getColumn(5).setCellRenderer((table, value, isSelected, hasFocus, row, column) -> {
            JButton button = new JButton("Oddaj");
            button.addActionListener(e -> markRentalAsReturned(rentalTable, row));
            return button;
        });

        System.out.println("Table components placed."); // Debugging
    }

    private void loadRentalsFromDatabase() {
        List<Object[]> rentals = DatabaseOperations.getAllRentals();

        for (Object[] rental : rentals) {
            tableModel.addRow(rental);
            System.out.println("Added rental to table: " + rental[1] + " - " + rental[2]); // Debugging
        }

        System.out.println("All rentals loaded into table.");
    }

    private void markRentalAsReturned(JTable rentalTable, int row) {
        String rentalId = tableModel.getValueAt(row, 0).toString();
        String returnDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        // Update the database
        DatabaseOperations.returnRental(rentalId, returnDate);

        // Remove the rental from the table
        tableModel.removeRow(row);

        System.out.println("Rental marked as returned: " + rentalId);
    }

    private void addNewRental() {
        JTextField userNameField = new JTextField(20);
        JTextField quantityField = new JTextField(20);
        JComboBox<String> gameComboBox = new JComboBox<>();

        List<Object[]> games = DatabaseOperations.getAllBoardGames();
        for (Object[] game : games) {
            gameComboBox.addItem((String) game[1]);  // Adding game names to the combo box
        }

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Nazwa Użytkownika:"));
        inputPanel.add(userNameField);
        inputPanel.add(new JLabel("Nazwa Gry:"));
        inputPanel.add(gameComboBox);
        inputPanel.add(new JLabel("Ilość:"));
        inputPanel.add(quantityField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Dodaj nowe wypożyczenie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String userName = userNameField.getText();
                String gameName = (String) gameComboBox.getSelectedItem();
                String quantity = quantityField.getText();
                String rentalDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                if (userName.isEmpty() || gameName == null || quantity.isEmpty()) {
                    throw new IllegalArgumentException("Wszystkie pola są wymagane!");
                }

                int gameId = -1;
                for (Object[] game : games) {
                    if (gameName.equals(game[1])) {
                        gameId = (int) game[0];
                        break;
                    }
                }

                DatabaseOperations.insertRental(userName, String.valueOf(gameId), rentalDate, quantity);
                Object[] row = {null, userName, gameName, rentalDate, quantity, "Oddaj"};
                tableModel.addRow(row);
                System.out.println("Dodano wypożyczenie: " + userName + " - " + gameName);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                addNewRental();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas dodawania wypożyczenia: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
