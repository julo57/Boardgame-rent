package src;

import src.GUI.Controllers;
import src.GUI.MainWindow;

public class Main {
    public static void main(String[] args) {
        // Create the database tables if they don't exist
        DatabaseManager.createTables();
        
        Controllers controllers = new Controllers();
        MainWindow mainWindow = new MainWindow(controllers);
        mainWindow.setVisible(true);
    }
}
