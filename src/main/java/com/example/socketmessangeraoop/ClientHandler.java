package com.example.socketmessangeraoop;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    private static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedReader reader;
    private BufferedWriter writer;
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {
            this.socket = socket;
            this.writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.clientUsername = reader.readLine();

            // Notify everyone about the new client
            broadcastMessage("Server: " + clientUsername + " has entered the chat room.");

            // Add the new client handler to the list
            clientHandlers.add(this);

        } catch (IOException e) {
            closeAll();
        }
    }

    @Override
    public void run() {
        String messageFromClient;
        while (socket.isConnected()) {
            try {
                messageFromClient = reader.readLine();

                // Broadcast the received message to all clients
                broadcastMessage(messageFromClient);

            } catch (IOException e) {
                closeAll();
                break;
            }
        }
    }

    public void broadcastMessage(String messageToSend) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                // Exclude the sender when broadcasting the message
                if (!clientHandler.clientUsername.equals(clientUsername)) {
                    clientHandler.writer.write(messageToSend);
                    clientHandler.writer.newLine();
                    clientHandler.writer.flush();
                }
            } catch (IOException e) {
                // Handle any issues with a specific client, remove them from the list
                clientHandler.closeAll();
            }
        }
    }

    public void removeClientHandler() {
        clientHandlers.remove(this);

        // Notify everyone about the departure of the client
        broadcastMessage("Server: " + clientUsername + " left the room.");
    }

    public void closeAll() {
        removeClientHandler();
        try {
            if (reader != null) {
                reader.close();
            }
            if (writer != null) {
                writer.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
