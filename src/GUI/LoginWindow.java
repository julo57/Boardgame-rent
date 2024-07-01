package src.GUI;

import java.awt.*;
import javax.swing.*;

public class LoginWindow extends JFrame {
    private Controllers controllers;

    public LoginWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        panel.setBackground(Color.LIGHT_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        add(panel);
        placeComponents(panel, gbc);

        setVisible(true);
    }

    private void placeComponents(JPanel panel, GridBagConstraints gbc) {
        JLabel userLabel = new JLabel("Username:");
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(userLabel, gbc);

        JTextField userText = new JTextField(20);
        gbc.gridx = 1;
        panel.add(userText, gbc);

        JLabel passwordLabel = new JLabel("Password:");
        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(passwordLabel, gbc);

        JPasswordField passwordText = new JPasswordField(20);
        gbc.gridx = 1;
        panel.add(passwordText, gbc);

        JButton loginButton = new JButton("Login");
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        panel.add(loginButton, gbc);

        loginButton.addActionListener(e -> {
            String username = userText.getText();
            String password = new String(passwordText.getPassword());

            if (controllers.verifyLogin(username, password)) {
                controllers.openLoggedInWindow();
                dispose();
            } else {
                JOptionPane.showMessageDialog(LoginWindow.this, "Invalid username or password.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
