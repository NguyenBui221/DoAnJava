package View;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class ChatView extends JFrame {
    private JTextArea chatArea;
    private JTextField inputField;
    private JButton sendButton;

    public ChatView() {
        setTitle("Chat Application");
        setSize(400, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        add(new JScrollPane(chatArea), BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        inputField = new JTextField();
        panel.add(inputField, BorderLayout.CENTER);

        sendButton = new JButton("Send");
        panel.add(sendButton, BorderLayout.EAST);

        add(panel, BorderLayout.SOUTH);
    }

    public void addSendListener(ActionListener listener) {
        sendButton.addActionListener(listener);
        inputField.addActionListener(listener);
    }

    public String getInputText() {
        return inputField.getText();
    }

    public void appendChat(String message) {
        chatArea.append(message + "\n");
    }

    public void clearInputField() {
        inputField.setText("");
    }
}
