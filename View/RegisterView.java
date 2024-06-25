package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class RegisterView extends JFrame {
    private JTextField nameField;
    private JPasswordField passwordField;
    private JButton registerButton;

    public RegisterView() {
        setTitle("Register");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(3, 2));

        panel.add(new JLabel("Name: "));
        nameField = new JTextField();
        panel.add(nameField);

        panel.add(new JLabel("Password: "));
        passwordField = new JPasswordField();
        panel.add(passwordField);

        registerButton = new JButton("Register");
        panel.add(registerButton);

        add(panel, BorderLayout.CENTER);
    }

    public void addRegisterListener(ActionListener listener) {
        registerButton.addActionListener(listener);
    }

    public String getName() {
        return nameField.getText();
    }

    public String getPassword() {
        return new String(passwordField.getPassword());
    }
}
