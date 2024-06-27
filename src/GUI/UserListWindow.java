package src.GUI;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import src.DatabaseOperations;

public class UserListWindow extends JFrame {
    private Controllers controllers;
    private DefaultTableModel tableModel;

    public UserListWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
        loadRentalsFromDatabase();
    }

    private void createAndShowGUI() {
        setTitle("User List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navbar = controllers.createNavbar(this);
        mainPanel.add(navbar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        placeComponents(contentPanel);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nazwa Użytkownika", "Nazwa Gry", "Data"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable rentalTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(rentalTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel addUserPanel = new JPanel();
        JButton addUserButton = new JButton("Dodaj Wypożyczenie");
        addUserButton.setPreferredSize(new Dimension(200, 30));
        addUserPanel.add(addUserButton);
        panel.add(addUserPanel, BorderLayout.SOUTH);

        addUserButton.addActionListener(e -> addNewRental());

        System.out.println("Table components placed.");
    }

    private void loadRentalsFromDatabase() {
        List<Object[]> rentals = DatabaseOperations.getAllRentals();

        for (Object[] rental : rentals) {
            tableModel.addRow(rental);
            System.out.println("Added rental to table: " + rental[1] + " - " + rental[2]);
        }

        System.out.println("All rentals loaded into table.");
    }

    private void addNewRental() {
        JTextField userNameField = new JTextField(20);
        JTextField rentalDateField = new JTextField(20);
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
        inputPanel.add(new JLabel("Data Wypożyczenia:"));
        inputPanel.add(rentalDateField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Dodaj nowe wypożyczenie", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String userName = userNameField.getText();
                String gameName = (String) gameComboBox.getSelectedItem();
                String rentalDate = rentalDateField.getText();

                if (userName.isEmpty() || gameName == null || rentalDate.isEmpty()) {
                    throw new IllegalArgumentException("Wszystkie pola są wymagane!");
                }

                int gameId = -1;
                for (Object[] game : games) {
                    if (gameName.equals(game[1])) {
                        gameId = (int) game[0];
                        break;
                    }
                }

                DatabaseOperations.insertRental(userName, gameId, rentalDate);
                Object[] row = {null, userName, gameName, rentalDate};
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
