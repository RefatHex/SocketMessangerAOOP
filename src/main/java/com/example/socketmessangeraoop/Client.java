package com.example.socketmessangeraoop;

import java.io.*;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.Scanner;

public class Client {
    private Socket socket;
    private BufferedReader bufferedReader;
    private BufferedWriter bufferedWriter;
    private String username;
    private String targetUsername;
    ChatBox chatBox;

    public Client(Socket socket, String username, String targetUsername,ChatBox chatBox) {
        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;
            this.targetUsername = targetUsername;
            this.chatBox=chatBox;
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }



    public void sendMessage(String message) {
        try {
            bufferedWriter.write(message);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            if (executeInsert(username, targetUsername, message)) {
                System.out.println("Message sent and saved to the database successfully!");
            } else {
                System.out.println("Failed to save the message to the database.");
            }
        } catch (IOException e) {
            closeAll(socket, bufferedReader, bufferedWriter);
        }
    }

    private void getUserInput() {
        try {
            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                System.out.print("Enter your message: ");
                String messageToSend = scanner.nextLine();
                sendMessage(targetUsername + ": " + username + ": " + messageToSend);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean executeInsert(String senderUsername, String receiverUsername, String message) {
        String insertSql = "INSERT INTO messages (senderUsername, receiverUsername, message) VALUES (?, ?, ?)";

        try (Connection connection = database.connectDB();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSql)) {

            preparedStatement.setString(1, senderUsername);
            preparedStatement.setString(2, receiverUsername);
            preparedStatement.setString(3, message);

            int affectedRows = preparedStatement.executeUpdate();
            return affectedRows > 0;
        } catch (Exception e) {
            closeAll(socket, bufferedReader, bufferedWriter);
            return false;
        }
    }

    public void listenForMessage() {
        new Thread(() -> {
            String msgFromGChat;
            while (socket.isConnected()) {
                try {
                    msgFromGChat = bufferedReader.readLine();
                    if (msgFromGChat == null) {
                        System.out.println("Disconnected from the server.");
                        break;
                    }
//                     Check if the message is from the target username
                    if (msgFromGChat.startsWith(targetUsername + ":")) {
                        System.out.println(msgFromGChat);
                    this.chatBox.receiveMessageFromServer(msgFromGChat);
                    }
                } catch (IOException e) {
                    closeAll(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    public void closeAll(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public static void main(String[] args) throws IOException {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Define your username: ");
//        String targetUsername = "nabil";
//        String username = scanner.nextLine();
//        Socket socket = new Socket("localhost", 5555);
//        Client client = new Client(socket, username, targetUsername);
//        client.listenForMessage();
//        new Thread(client::getUserInput).start();
//    }
}
