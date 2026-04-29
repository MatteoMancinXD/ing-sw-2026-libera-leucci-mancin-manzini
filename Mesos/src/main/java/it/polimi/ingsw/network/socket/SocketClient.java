package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.view.ui;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.rmi.RemoteException;


public class SocketClient implements NetworkClient {

    private final String nickname;
    private String token;
    private final ui userInterface;

    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    public SocketClient(String nickname, ui userInterface)
    {
        this.nickname = nickname;
        this.userInterface = userInterface;
    }


    public void startConnection(String ip, int port) {
        try {
            userInterface.showMessage("Connecting to server:  " + ip + ":" + port + "...");
            socket = new Socket(ip, port);

            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            new Thread(this::listenToServer).start();

            userInterface.showMessage("Connected to server");

        } catch (IOException e) {
            userInterface.showError("Connection error: " + e.getMessage());
        }
    }

    public void sendMessageToServer(ClientToServerMessage message) {
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            userInterface.showError("Network error, impossible to send message");
        }
    }


    private void listenToServer() {
        try {
            while (true) {
                ServerToClientMessage message = (ServerToClientMessage) in.readObject();
                message.onReceive(this);
                message.process(userInterface);
            }
        } catch (IOException | ClassNotFoundException e) {
            userInterface.showError("Connection lost");
        } finally {
            disconnect();
        }
    }


    public String getToken() { return token; }

    public void setToken(String token) { this.token = token; }

    public void login(int gameId, int numPlayers) {
        sendMessageToServer(new LoginMessage(nickname, gameId, numPlayers));
    }

    @Override
    public void requestAvailableGames() throws RemoteException {

    }

    @Override
    public void createGame(String nickname, int numPlayers) {
        sendMessageToServer(new CreateGameMessage(token, this.nickname, numPlayers));
    }

    @Override
    public void joinGame(String nickname, int gameID) {
        sendMessageToServer(new JoinGameMessage(token, this.nickname, gameID));
    }

    @Override
    public void askToDrawCard(boolean row, int index) {
        sendMessageToServer(new DrawCardMessage(token, row, index));
    }

    @Override
    public void askToPlaceTotem(int pos) {
        sendMessageToServer(new PlaceTotemMessage(token, pos));
    }

    @Override
    public void askToSkipBonus() {
        sendMessageToServer(new SkipBonusMessage(token));
    }

    @Override
    public void sendChatMessage(String message) {
        sendMessageToServer(new ChatMessage(token, message));
    }

    @Override
    public void askToEndTurn() {
        //inutile perchè i player DEVONO prendere tutte le carte che possono
    }

    @Override
    public void disconnect() {
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
