package src.GUI;

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
        // Create a user with username "user" and password "password"
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
        mainWindow.setVisible(false);
    }

    public void openLoggedInWindow() {
        if (loggedInWindow == null) {
            loggedInWindow = new LoggedInWindow(this);
        }
        loggedInWindow.setVisible(true);
        loginWindow.setVisible(false);
    }

    public boolean verifyLogin(String username, String password) {
        return username.equals(user.getUsername()) && PasswordUtils.verifyPassword(password, user.getPasswordHash());
    }
    public UserListWindow getUserListWindow() {
        if (userListWindow == null) {
            userListWindow = new UserListWindow(this);
        }
        return userListWindow;
    }

    public GameListWindow getGameListWindow() {
        if (gameListWindow == null) {
            gameListWindow = new GameListWindow(this);
        }
        return gameListWindow;
    }

    public HistoryWindow getHistoryWindow() {
        if (historyWindow == null) {
            historyWindow = new HistoryWindow(this);
        }
        return historyWindow;
    }
}
