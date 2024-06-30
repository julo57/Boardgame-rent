package src.GUI;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableRowSorter;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import src.DatabaseOperations;

public class LoggedInWindow extends JFrame {
    private Controllers controllers;
    private DefaultTableModel tableModel;
    private JTable rentalTable; // Make rentalTable a class member

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
        rentalTable = new JTable(tableModel) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 5; // Only the "Akcje" column is editable
            }

            @Override
            public TableCellRenderer getCellRenderer(int row, int column) {
                if (column == 4) { // Column for quantity (squares)
                    return new SquareCellRenderer();
                }
                return super.getCellRenderer(row, column);
            }
        };
        rentalTable.getColumnModel().getColumn(5).setCellRenderer(new ButtonRenderer());
        rentalTable.getColumnModel().getColumn(5).setCellEditor(new ButtonEditor(new JCheckBox(), rentalTable, controllers));

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
            gameComboBox.addItem((String) game[2]);  // Adding game names to the combo box
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
                    if (gameName.equals(game[2])) {
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

    // Custom cell renderer to draw green squares for quantity
    private static class SquareCellRenderer extends JPanel implements TableCellRenderer {
        private int quantity;

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            quantity = Integer.parseInt(value.toString());
            return this;
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            int squareSize = 10;
            int padding = 2;
            int x = padding;
            int y = (getHeight() - squareSize) / 2;
            for (int i = 0; i < quantity; i++) {
                g.setColor(Color.GREEN); // Only green squares
                g.fillRect(x, y, squareSize, squareSize);
                x += squareSize + padding;
            }
        }
    }

    // Custom button renderer
    private class ButtonRenderer extends JButton implements TableCellRenderer {
        public ButtonRenderer() {
            setOpaque(true);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            setText((value == null) ? "" : value.toString());
            return this;
        }
    }

    // Custom button editor
    private class ButtonEditor extends DefaultCellEditor {
        private String label;
        private boolean clicked;
        private JTable rentalTable;
        private Controllers controllers;

        public ButtonEditor(JCheckBox checkBox, JTable rentalTable, Controllers controllers) {
            super(checkBox);
            this.rentalTable = rentalTable;
            this.controllers = controllers;
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
            label = (value == null) ? "" : value.toString();
            JButton button = new JButton(label);
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    clicked = true;
                    fireEditingStopped();
                }
            });
            return button;
        }

        @Override
        public Object getCellEditorValue() {
            if (clicked) {
                markRentalAsReturned(rentalTable, rentalTable.getSelectedRow());
                new HistoryWindow(controllers).refreshHistory(); // Refresh history window after returning rental
            }
            clicked = false;
            return label;
        }

        @Override
        public boolean stopCellEditing() {
            clicked = false;
            return super.stopCellEditing();
        }
    }
}
