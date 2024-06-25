package src;

import src.GUI.Controllers;
import src.GUI.MainWindow;

public class Main {
    public static void main(String[] args) {
        // Create the database table if it doesn't exist
        DatabaseManager.createTable();
        
        Controllers controllers = new Controllers();
        MainWindow mainWindow = new MainWindow(controllers);
        mainWindow.setVisible(true);
    }
}
