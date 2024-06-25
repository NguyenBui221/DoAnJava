package Controller;

import Model.ChatModel;
import View.ChatView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class ChatController {
    private ChatModel model;
    private ChatView view;
    private String username;

    public ChatController(ChatModel model, ChatView view, String username) {
        this.model = model;
        this.view = view;
        this.username = username;

        model.sendMessage(username + " has joined the chat");

        view.addSendListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String message = view.getInputText();
                model.sendMessage(username + ": " + message);
                view.appendChat("Me: " + message);
                view.clearInputField();
            }
        });

        new Thread(() -> {
            try {
                String message;
                while ((message = model.receiveMessage()) != null) {
                    view.appendChat(message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}