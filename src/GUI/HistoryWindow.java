package src.GUI;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import src.DatabaseOperations;

public class HistoryWindow extends JFrame {
    private Controllers controllers;
    private DefaultTableModel tableModel;

    public HistoryWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
        loadHistoryFromDatabase();
    }

    private void createAndShowGUI() {
        setTitle("History");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navbar = controllers.createNavbar(this);
        mainPanel.add(navbar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        placeComponents(contentPanel);

        setContentPane(mainPanel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());

        // Table
        String[] columnNames = {"ID", "Nazwa Użytkownika", "Nazwa Gry", "Data Wypożyczenia", "Data Zwrotu", "Ilość"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable historyTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(historyTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        System.out.println("Table components placed."); // Debugging
    }

    private void loadHistoryFromDatabase() {
        List<Object[]> history = DatabaseOperations.getAllRentalHistory();

        for (Object[] record : history) {
            tableModel.addRow(record);
            System.out.println("Added history record to table: " + record[1] + " - " + record[2]); // Debugging
        }

        System.out.println("All history records loaded into table.");
    }

    public void refreshHistory() {
        tableModel.setRowCount(0); // Clear the table
        loadHistoryFromDatabase(); // Reload the data
    }
}
