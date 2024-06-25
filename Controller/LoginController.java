package Controller;

import Model.ChatModel;
import View.ChatView;
import View.LoginView;
import View.RegisterView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.swing.JOptionPane;

public class LoginController {
    private ChatModel model;
    private LoginView loginView;
    private RegisterView registerView;

    public LoginController(ChatModel model, LoginView loginView) {
        this.model = model;
        this.loginView = loginView;

        loginView.addLoginListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = loginView.getName();
                String password = hashPassword(loginView.getPassword());
                model.sendMessage("LOGIN " + username + " " + password);
                try {
                    String response = model.receiveMessage();
                    if ("LOGIN_SUCCESS".equals(response)) {
                        loginView.dispose();
                        ChatView chatView = new ChatView();
                        new ChatController(model, chatView, username);
                        chatView.setVisible(true);
                    } else {
                        JOptionPane.showMessageDialog(loginView, "Login failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        loginView.addRegisterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                loginView.dispose();
                registerView = new RegisterView();
                new RegisterController(model, registerView);
                registerView.setVisible(true);
            }
        });
    }

    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
