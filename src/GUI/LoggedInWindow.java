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

        // Button to mark rentals as returned
        JPanel buttonPanel = new JPanel();
        JButton returnButton = new JButton("ODDAJ");
        buttonPanel.add(returnButton);
        panel.add(buttonPanel, BorderLayout.SOUTH);

        returnButton.addActionListener(e -> markRentalAsReturned(rentalTable));

        System.out.println("Table components placed."); // Debugging
    }

    private void loadRentalsFromDatabase() {
        List<Object[]> rentals = DatabaseOperations.getAllRentals();

        for (Object[] rental : rentals) {
            rental[5] = "Oddaj"; // Add "Oddaj" button text to the "Akcje" column
            tableModel.addRow(rental);
            System.out.println("Added rental to table: " + rental[1] + " - " + rental[2]); // Debugging
        }

        System.out.println("All rentals loaded into table.");
    }

    private void markRentalAsReturned(JTable rentalTable) {
        int selectedRow = rentalTable.getSelectedRow();
        if (selectedRow != -1) {
            String rentalId = tableModel.getValueAt(selectedRow, 0).toString();
            String returnDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

            // Update the database
            DatabaseOperations.returnRental(rentalId, returnDate);

            // Remove the rental from the table
            tableModel.removeRow(selectedRow);

            System.out.println("Rental marked as returned: " + rentalId);
        } else {
            JOptionPane.showMessageDialog(this, "Wybierz wypożyczenie do zwrotu!", "Błąd", JOptionPane.ERROR_MESSAGE);
        }
    }
}
