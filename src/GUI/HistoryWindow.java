package src.GUI;

import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
        mainPanel.setBackground(Color.WHITE);

        JPanel navbar = controllers.createNavbar(this);
        mainPanel.add(navbar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(Color.WHITE);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        JLabel headerLabel = new JLabel("Rental History", JLabel.CENTER);
        headerLabel.setFont(new Font("Arial", Font.BOLD, 24));
        headerLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        contentPanel.add(headerLabel, BorderLayout.NORTH);

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
        
        // Set table header font and alignment
        JTableHeader tableHeader = historyTable.getTableHeader();
        tableHeader.setFont(new Font("Arial", Font.BOLD, 16));
        tableHeader.setBackground(Color.LIGHT_GRAY);
        ((DefaultTableCellRenderer) tableHeader.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        // Set table cell alignment
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        for (int i = 0; i < historyTable.getColumnCount(); i++) {
            historyTable.getColumnModel().getColumn(i).setCellRenderer(centerRenderer);
        }

        // Set row height
        historyTable.setRowHeight(25);

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
