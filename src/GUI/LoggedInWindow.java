package src.GUI;

import javax.swing.*;

public class LoggedInWindow extends JFrame {
    private Controllers controllers;

    public LoggedInWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Welcome");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        setLocationRelativeTo(null); // Center the window

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome to the Application!");
        welcomeLabel.setBounds(100, 50, 200, 25);
        panel.add(welcomeLabel);

        JButton userListButton = new JButton("User List");
        userListButton.setBounds(150, 100, 100, 25);
        panel.add(userListButton);

        JButton gameListButton = new JButton("Game List");
        gameListButton.setBounds(150, 130, 100, 25);
        panel.add(gameListButton);

        JButton historyButton = new JButton("History");
        historyButton.setBounds(150, 160, 100, 25);
        panel.add(historyButton);

        JButton logoutButton = new JButton("Logout");
        logoutButton.setBounds(150, 190, 100, 25);
        panel.add(logoutButton);

        userListButton.addActionListener(e -> {
            // Show the UserListWindow
            controllers.getUserListWindow().setVisible(true);
            this.setVisible(false);
        });

        gameListButton.addActionListener(e -> {
            // Show the GameListWindow
            controllers.getGameListWindow().setVisible(true);
            this.setVisible(false);
        });

        historyButton.addActionListener(e -> {
            // Show the HistoryWindow
            controllers.getHistoryWindow().setVisible(true);
            this.setVisible(false);
        });

        logoutButton.addActionListener(e -> {
            // Close the LoggedInWindow and show the MainWindow again
            this.setVisible(false);
            controllers.getMainWindow().setVisible(true);
        });
    }
}
