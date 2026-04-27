package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.messages.BoardUpdateMessage;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.PlaceTotemMessage;
import it.polimi.ingsw.network.messages.ServerToClientMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClient implements NetworkClient {

    private final String nickname;
    private String token;
    //ci sarà anche la TUI/GUI

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SocketClient(String nickname) {
        this.nickname = nickname;
    }


    public void startConnection(String ip, int port) {
        try {
            System.out.println("Connecting to server:  " + ip + ":" + port + "...");
            socket = new Socket(ip, port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(this::listenToServer).start();

            System.out.println("Connected to server");

        } catch (IOException e) {
            System.err.println("Connection error: " + e.getMessage());
        }
    }

    public void sendMessageToServer(ClientToServerMessage message) {
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.err.println("Network error, impossible to send message");
        }
    }


    private void listenToServer() {
        try {
            while (true) {
                ServerToClientMessage message = (ServerToClientMessage) in.readObject();
                message.process();
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Connection lost");
        } finally {
            try { if (socket != null) socket.close(); } catch (IOException ex) { }
        }
    }

    public void askToPlaceTotem(int index) {
        sendMessageToServer(new PlaceTotemMessage(this.nickname, index));
    }

}
