package src.GUI;

import java.awt.*;
import javax.swing.*;

public class MainWindow extends JFrame {
    private Controllers controllers;

    public MainWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Moja Aplikacja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JPanel mainPanel = new JPanel(new BorderLayout());

        
        JPanel contentPanel = new JPanel(new BorderLayout());
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        placeComponents(contentPanel);

        setContentPane(mainPanel);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Witaj w aplikacji!");
        userLabel.setBounds(300, 50, 200, 25);
        panel.add(userLabel);

        JButton loginButton = new JButton("Zaloguj");
        loginButton.setBounds(300, 100, 200, 25);
        panel.add(loginButton);

        JButton closeButton = new JButton("Zamknij");
        closeButton.setBounds(300, 150, 200, 25);
        panel.add(closeButton);

        loginButton.addActionListener(e -> controllers.openLoginWindow());

        closeButton.addActionListener(e -> System.exit(0));
    }
}
