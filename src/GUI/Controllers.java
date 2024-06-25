package src.GUI;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import src.Utils.PasswordUtils;
import src.Utils.User;

public class Controllers {

    private MainWindow mainWindow;
    private LoginWindow loginWindow;
    private LoggedInWindow loggedInWindow;
    private UserListWindow userListWindow;
    private GameListWindow gameListWindow;
    private HistoryWindow historyWindow;
    private User user;

    public Controllers() {
        user = new User("user", "password");
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    public MainWindow getMainWindow() {
        return mainWindow;
    }

    public void openLoginWindow() {
        if (loginWindow == null) {
            loginWindow = new LoginWindow(this);
        }
        loginWindow.setVisible(true);
        if (mainWindow != null) mainWindow.setVisible(false);
    }

    public void openLoggedInWindow() {
        if (loggedInWindow == null) {
            loggedInWindow = new LoggedInWindow(this);
        }
        loggedInWindow.setVisible(true);
        if (loginWindow != null) loginWindow.setVisible(false);
    }

    public void openGameListWindow() {
        if (gameListWindow == null) {
            gameListWindow = new GameListWindow(this);
        }
        gameListWindow.setVisible(true);
        if (loggedInWindow != null) loggedInWindow.setVisible(false);
    }

    public boolean verifyLogin(String username, String password) {
        return username.equals(user.getUsername()) && PasswordUtils.verifyPassword(password, user.getPasswordHash());
    }

    public JPanel createNavbar(JFrame currentFrame) {
        JPanel navbar = new JPanel();

        JButton userListButton = new JButton("User List");
        JButton gameListButton = new JButton("Game List");
        JButton historyButton = new JButton("History");
        JButton generalButton = new JButton("General");
        JButton logoutButton = new JButton("Logout");

        userListButton.addActionListener(e -> {
            if (userListWindow == null) {
                userListWindow = new UserListWindow(this);
            }
            userListWindow.setVisible(true);
            currentFrame.setVisible(false);
        });

        gameListButton.addActionListener(e -> {
            if (gameListWindow == null) {
                gameListWindow = new GameListWindow(this);
            }
            gameListWindow.setVisible(true);
            currentFrame.setVisible(false);
        });

        historyButton.addActionListener(e -> {
            if (historyWindow == null) {
                historyWindow = new HistoryWindow(this);
            }
            historyWindow.setVisible(true);
            currentFrame.setVisible(false);
        });

        generalButton.addActionListener(e -> {
            if (loggedInWindow == null) {
                loggedInWindow = new LoggedInWindow(this);
            }
            loggedInWindow.setVisible(true);
            currentFrame.setVisible(false);
        });

        logoutButton.addActionListener(e -> {
            if (mainWindow == null) {
                mainWindow = new MainWindow(this);
            }
            mainWindow.setVisible(true);
            currentFrame.setVisible(false);
        });

        navbar.add(userListButton);
        navbar.add(gameListButton);
        navbar.add(historyButton);
        navbar.add(generalButton);
        navbar.add(logoutButton);

        return navbar;
    }
}