package src.GUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginWindow extends JFrame {

    private JTextField userTextField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JButton cancelButton;

    public LoginWindow() {
        // Ustawienia okna
        setTitle("Login Window");
        setSize(300, 150);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel główny
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(3, 2));

        // Etykieta użytkownika
        JLabel userLabel = new JLabel("User:");
        panel.add(userLabel);

        // Pole tekstowe dla użytkownika
        userTextField = new JTextField(20);
        panel.add(userTextField);

        // Etykieta hasła
        JLabel passwordLabel = new JLabel("Password:");
        panel.add(passwordLabel);

        // Pole hasła
        passwordField = new JPasswordField(20);
        panel.add(passwordField);

        // Przycisk logowania
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String user = userTextField.getText();
                char[] password = passwordField.getPassword();
                
                // Logika logowania (do zaimplementowania)
                JOptionPane.showMessageDialog(LoginWindow.this,
                        "User: " + user + "\nPassword: " + new String(password));
            }
        });
        panel.add(loginButton);

        // Przycisk anulowania
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        panel.add(cancelButton);

        // Dodanie panelu do okna
        add(panel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginWindow().setVisible(true);
            }
        });
    }
}
