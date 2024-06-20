package src.GUI;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainWindow extends JFrame {
    private Controllers controllers;

    public MainWindow(Controllers controllers) {
        this.controllers = controllers;
        controllers.setMainWindow(this);
        createAndShowGUI();
    }

    public void createAndShowGUI() {
        setTitle("Moja Aplikacja");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenu editMenu = new JMenu("Edit");
        JMenu helpMenu = new JMenu("Help");

        JMenuItem exitMenuItem = new JMenuItem("Exit");
        JMenuItem aboutMenuItem = new JMenuItem("About");

        fileMenu.add(exitMenuItem);
        helpMenu.add(aboutMenuItem);

        menuBar.add(fileMenu);
        menuBar.add(editMenu);
        menuBar.add(helpMenu);

        setJMenuBar(menuBar);

        JPanel panel = new JPanel();
        add(panel);
        placeComponents(panel);

        exitMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        aboutMenuItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(MainWindow.this, "Planszowki.");
            }
        });

        setVisible(true);
    }

    private void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel userLabel = new JLabel("Witaj w aplikacji!");
        userLabel.setBounds(150, 50, 200, 25);
        panel.add(userLabel);

        JButton loginButton = new JButton("Zaloguj");
        loginButton.setBounds(150, 100, 100, 25);
        panel.add(loginButton);

        JButton closeButton = new JButton("Zamknij");
        closeButton.setBounds(150, 150, 100, 25);
        panel.add(closeButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                controllers.openLoginWindow();
            }
        });

        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
    }
}
