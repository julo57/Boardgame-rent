package src.GUI;

public class Controllers {

    private MainWindow mainWindow;
    private LoginWindow loginWindow;

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
}
