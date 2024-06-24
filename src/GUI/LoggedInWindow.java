package src.GUI;

import java.awt.*;
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

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        JPanel navbar = controllers.createNavbar(this);
        mainPanel.add(navbar, BorderLayout.NORTH);

        JPanel contentPanel = new JPanel();
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        placeComponents(contentPanel);

        add(mainPanel);
        setLocationRelativeTo(null); // Center the window
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel welcomeLabel = new JLabel("Welcome to the Application!");
        welcomeLabel.setBounds(100, 50, 200, 25);
        panel.add(welcomeLabel);
    }
}
