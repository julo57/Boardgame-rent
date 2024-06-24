package src.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainFrame extends JFrame {
    private Controllers controllers;
    private CardLayout cardLayout;
    private JPanel cardPanel;

    public MainFrame(Controllers controllers) {
        this.controllers = controllers;
        controllers.setMainFrame(this);
        createAndShowGUI();
    }

    private void createAndShowGUI() {
        setTitle("Main Application");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);
        setLocationRelativeTo(null); // Center the window

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel navbar = controllers.createNavbar();
        mainPanel.add(navbar, BorderLayout.NORTH);

        cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        cardPanel.add(new LoggedInPanel(controllers), "LoggedIn");
        cardPanel.add(new UserListPanel(), "UserList");
        cardPanel.add(new GameListPanel(), "GameList");
        cardPanel.add(new HistoryPanel(), "History");

        mainPanel.add(cardPanel, BorderLayout.CENTER);

        add(mainPanel);
        setVisible(true);
    }

    public void showCard(String cardName) {
        cardLayout.show(cardPanel, cardName);
    }

    private class LoggedInPanel extends JPanel {
        public LoggedInPanel(Controllers controllers) {
            setLayout(new BorderLayout());
            JLabel label = new JLabel("Welcome to the Application!", SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);
        }
    }

    private class UserListPanel extends JPanel {
        public UserListPanel() {
            setLayout(new BorderLayout());
            JLabel label = new JLabel("User List", SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);
        }
    }

    private class GameListPanel extends JPanel {
        public GameListPanel() {
            setLayout(new BorderLayout());
            JLabel label = new JLabel("Game List", SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);
        }
    }

    private class HistoryPanel extends JPanel {
        public HistoryPanel() {
            setLayout(new BorderLayout());
            JLabel label = new JLabel("History", SwingConstants.CENTER);
            add(label, BorderLayout.CENTER);
        }
    }
}
