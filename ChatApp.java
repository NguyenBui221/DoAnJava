import javax.swing.*;
import java.io.IOException;

import javax.swing.*;
import java.io.IOException;

import Model.ChatModel;
import View.LoginView;
import Controller.LoginController;

public class ChatApp {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                ChatModel model = new ChatModel("localhost", 12345);
                LoginView loginView = new LoginView();
                new LoginController(model, loginView);
                loginView.setVisible(true);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
