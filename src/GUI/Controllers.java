package src.GUI;

import javax.swing.JButton;
import javax.swing.JPanel;
import src.Utils.PasswordUtils;
import src.Utils.User;

public class Controllers {

    private MainFrame mainFrame;
    private User user;

    public Controllers() {
        // Create a user with username "user" and password "password"
        user = new User("user", "password");
    }

    public void setMainFrame(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
    }

    public MainFrame getMainFrame() {
        return mainFrame;
    }

    public boolean verifyLogin(String username, String password) {
        return username.equals(user.getUsername()) && PasswordUtils.verifyPassword(password, user.getPasswordHash());
    }

    public JPanel createNavbar() {
        JPanel navbar = new JPanel();

        JButton userListButton = new JButton("User List");
        JButton gameListButton = new JButton("Game List");
        JButton historyButton = new JButton("History");
        JButton generalButton = new JButton("General");
        JButton logoutButton = new JButton("Logout");

        userListButton.addActionListener(e -> mainFrame.showCard("UserList"));
        gameListButton.addActionListener(e -> mainFrame.showCard("GameList"));
        historyButton.addActionListener(e -> mainFrame.showCard("History"));
        generalButton.addActionListener(e -> mainFrame.showCard("LoggedIn"));

        logoutButton.addActionListener(e -> {
            // Logout and show the login window
            mainFrame.dispose();
            openLoginWindow();
        });

        navbar.add(userListButton);
        navbar.add(gameListButton);
        navbar.add(historyButton);
        navbar.add(generalButton);
        navbar.add(logoutButton);

        return navbar;
    }

    public void openLoginWindow() {
        if (mainFrame != null) {
            mainFrame.setVisible(false);
        }
        LoginWindow loginWindow = new LoginWindow(this);
        loginWindow.setVisible(true);
    }

    public void openMainFrame() {
        if (mainFrame == null) {
            mainFrame = new MainFrame(this);
        }
        mainFrame.setVisible(true);
    }
}

