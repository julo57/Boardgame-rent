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
        loadUsersFromDatabase();
    }

    private void createAndShowGUI() {
        setTitle("User List");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(245, 245, 245));

        JPanel navbar = controllers.createNavbar(this);
        mainPanel.add(navbar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(245, 245, 245));
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        placeComponents(contentPanel);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(new BorderLayout());

        String[] columnNames = {"ID", "Nazwa Użytkownika"};
        tableModel = new DefaultTableModel(columnNames, 0);
        JTable userTable = new JTable(tableModel);
        userTable.setFont(new Font("Arial", Font.PLAIN, 14));
        userTable.setRowHeight(30);
        JScrollPane scrollPane = new JScrollPane(userTable);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel addUserPanel = new JPanel();
        addUserPanel.setBackground(new Color(245, 245, 245));
        JButton addUserButton = new JButton("Dodaj Użytkownika");
        addUserButton.setPreferredSize(new Dimension(200, 30));
        addUserButton.setBackground(new Color(100, 149, 237));
        addUserButton.setForeground(Color.WHITE);
        addUserButton.setFocusPainted(false);
        addUserPanel.add(addUserButton);
        panel.add(addUserPanel, BorderLayout.SOUTH);

        addUserButton.addActionListener(e -> addNewUser());

        System.out.println("Table components placed.");
    }

    public void loadUsersFromDatabase() {
        List<Object[]> users = DatabaseOperations.getAllUsers();
        tableModel.setRowCount(0); // Clear the table before loading new data

        for (Object[] user : users) {
            tableModel.addRow(user);
            System.out.println("Added user to table: " + user[1]);
        }

        System.out.println("All users loaded into table.");
    }

    private void addNewUser() {
        JTextField userNameField = new JTextField(20);

        JPanel inputPanel = new JPanel(new GridLayout(2, 2));
        inputPanel.add(new JLabel("Nazwa Użytkownika:"));
        inputPanel.add(userNameField);

        int result = JOptionPane.showConfirmDialog(null, inputPanel, "Dodaj nowego użytkownika", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result == JOptionPane.OK_OPTION) {
            try {
                String userName = userNameField.getText();

                if (userName.isEmpty()) {
                    throw new IllegalArgumentException("Nazwa użytkownika jest wymagana!");
                }

                DatabaseOperations.insertUser(userName);
                int userId = DatabaseOperations.getUserIdByName(userName); // Ensure this method exists in DatabaseOperations
                Object[] row = {userId, userName};
                tableModel.addRow(row);
                System.out.println("Dodano użytkownika: " + userName);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
                addNewUser();
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Wystąpił błąd podczas dodawania użytkownika: " + ex.getMessage(), "Błąd", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}
