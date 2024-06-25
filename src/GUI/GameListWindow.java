package src.GUI;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GameListWindow extends JFrame {
    private Controllers controllers;

    public GameListWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Game List");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel navbar = controllers.createNavbar(this);
        mainPanel.add(navbar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        placeComponents(contentPanel);

        add(mainPanel);
        setLocationRelativeTo(null);
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
        String[] columnNames = {"PNG", "NAZWA", "KATEGORIA", "CZAS GRY", "WIEK", "L. graczy", "OPIS", "UWAGI"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable gameTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(gameTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        // Sort and Filter Panel
        JPanel sortFilterPanel = new JPanel(new GridLayout(1, 2));

        JPanel sortPanel = new JPanel(new GridLayout(5, 1));
        sortPanel.setBorder(BorderFactory.createTitledBorder("SORTUJ"));
        sortPanel.add(new JCheckBox("NAZWA"));
        sortPanel.add(new JCheckBox("DATA WYDANIA"));
        sortPanel.add(new JCheckBox("CZAS GRY"));
        sortPanel.add(new JCheckBox("OCENA"));
        sortPanel.add(new JCheckBox("LICZBA GRACZY"));
        sortFilterPanel.add(sortPanel);

        JPanel filterPanel = new JPanel(new GridLayout(3, 1));
        filterPanel.setBorder(BorderFactory.createTitledBorder("FILTRUJ"));
        filterPanel.add(new JCheckBox("LICZBA GRACZY"));
        filterPanel.add(new JCheckBox("KATEGORIA WIEKOWA"));
        filterPanel.add(new JCheckBox("TYP GRY"));
        sortFilterPanel.add(filterPanel);

        panel.add(sortFilterPanel, BorderLayout.EAST);

        // Add Game Button
        JPanel addGamePanel = new JPanel();
        JButton addGameButton = new JButton("DODAJ GRĘ");
        addGameButton.setPreferredSize(new Dimension(150, 30));
        addGamePanel.add(addGameButton);
        panel.add(addGamePanel, BorderLayout.SOUTH);

        // Add functionality to add game button
        addGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Logic to add a new game
                Object[] row = {"<PNG>", "New Game", "Strategy", 60, 12, "2-4", "Description", "Remarks"};
                tableModel.addRow(row);
            }
        });
    }
}
