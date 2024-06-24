package src.GUI;

import java.awt.*;
import javax.swing.*;

public class GameListWindow extends JFrame {
    private Controllers controllers;

    public GameListWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Game List");
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

        JLabel label = new JLabel("Game List");
        label.setBounds(150, 50, 100, 25);
        panel.add(label);
    }
}
