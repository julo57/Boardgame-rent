package src.GUI;

import javax.swing.*;

public class HistoryWindow extends JFrame {
    private Controllers controllers;

    public HistoryWindow(Controllers controllers) {
        this.controllers = controllers;
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("History");
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

        JLabel label = new JLabel("History");
        label.setBounds(150, 50, 100, 25);
        panel.add(label);

        JButton backButton = new JButton("Back");
        backButton.setBounds(150, 200, 100, 25);
        panel.add(backButton);

        backButton.addActionListener(e -> {
            this.setVisible(false);
            controllers.getMainWindow().setVisible(true);
        });
    }
}
