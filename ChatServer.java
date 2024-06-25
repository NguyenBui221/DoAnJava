import java.awt.BorderLayout;
import java.io.*;
import java.net.*;
import java.util.*;
import javax.swing.*;

public class ChatServer {
    private static Set<PrintWriter> clientWriters = new HashSet<>();
    private static JTextArea serverLog;
    private static Map<String, String> userCredentials = new HashMap<>();
    private static final String USERS_FILE = "users.txt";

    public static void main(String[] args) throws IOException {
        JFrame serverFrame = new JFrame("Chat Server");
        serverLog = new JTextArea();
        serverLog.setEditable(false);
        serverFrame.add(new JScrollPane(serverLog), BorderLayout.CENTER);
        serverFrame.setSize(400, 400);
        serverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        serverFrame.setVisible(true);

        loadUserCredentials();

        ServerSocket serverSocket = new ServerSocket(12345);
        serverLog.append("Server is running...\n");

        while (true) {
            new ClientHandler(serverSocket.accept()).start();
        }
    }

    private static void loadUserCredentials() {
        try (BufferedReader reader = new BufferedReader(new FileReader(USERS_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] tokens = line.split(" ");
                if (tokens.length == 2) {
                    userCredentials.put(tokens[0], tokens[1]);
                }
            }
        } catch (IOException e) {
            serverLog.append("Error loading user credentials: " + e.getMessage() + "\n");
        }
    }

    private static void saveUserCredentials() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(USERS_FILE))) {
            for (Map.Entry<String, String> entry : userCredentials.entrySet()) {
                writer.println(entry.getKey() + " " + entry.getValue());
            }
        } catch (IOException e) {
            serverLog.append("Error saving user credentials: " + e.getMessage() + "\n");
        }
    }

    private static class ClientHandler extends Thread {
        private Socket socket;
        private PrintWriter out;
        private BufferedReader in;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        public void run() {
            try {
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                out = new PrintWriter(socket.getOutputStream(), true);
                synchronized (clientWriters) {
                    clientWriters.add(out);
                }

                String message;
                while ((message = in.readLine()) != null) {
                    serverLog.append("Received: " + message + "\n");
                    String[] tokens = message.split(" ");
                    String command = tokens[0];

                    if ("REGISTER".equals(command)) {
                        handleRegister(tokens[1], tokens[2]);
                    } else if ("LOGIN".equals(command)) {
                        handleLogin(tokens[1], tokens[2]);
                    } else {
                        synchronized (clientWriters) {
                            for (PrintWriter writer : clientWriters) {
                                writer.println(message);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                synchronized (clientWriters) {
                    clientWriters.remove(out);
                }
            }
        }

        private void handleRegister(String username, String password) {
            if (userCredentials.containsKey(username)) {
                out.println("REGISTER_FAILED");
            } else {
                userCredentials.put(username, password);
                saveUserCredentials();
                out.println("REGISTER_SUCCESS");
                serverLog.append("User registered: " + username + "\n");
            }
        }

        private void handleLogin(String username, String password) {
            if (userCredentials.containsKey(username) && userCredentials.get(username).equals(password)) {
                out.println("LOGIN_SUCCESS");
                serverLog.append("User logged in: " + username + "\n");
            } else {
                out.println("LOGIN_FAILED");
            }
        }
    }
}