package Controller;

import Model.ChatModel;
import View.RegisterView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import javax.swing.JOptionPane;

public class RegisterController {
    private ChatModel model;
    private RegisterView registerView;

    public RegisterController(ChatModel model, RegisterView registerView) {
        this.model = model;
        this.registerView = registerView;

        registerView.addRegisterListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = registerView.getName();
                String password = hashPassword(registerView.getPassword());
                model.sendMessage("REGISTER " + username + " " + password);
                try {
                    String response = model.receiveMessage();
                    if ("REGISTER_SUCCESS".equals(response)) {
                        registerView.dispose();
                        JOptionPane.showMessageDialog(null, "Register successful", "Info", JOptionPane.INFORMATION_MESSAGE);
                    } else {
                        JOptionPane.showMessageDialog(registerView, "Register failed", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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