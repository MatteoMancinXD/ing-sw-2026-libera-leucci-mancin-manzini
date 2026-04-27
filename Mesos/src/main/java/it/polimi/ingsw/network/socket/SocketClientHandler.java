package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.DrawCardMessage;
import it.polimi.ingsw.network.messages.LoginMessage;
import it.polimi.ingsw.network.messages.SkipBonusMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

public class SocketClientHandler implements Runnable{

    private final Socket socket;
    private final GameManager mngr;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nickname;

    public SocketClientHandler(Socket socket, GameManager mngr) {
        this.socket = socket;
        this.mngr = mngr;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                ClientToServerMessage message = (ClientToServerMessage) in.readObject();
                message.process(this);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Player " + nickname + " disconnected.");
        } finally {
            closeConnection();
        }
    }

    public String handleLogin(LoginMessage msg) {
        this.nickname = msg.getNickname();
        int gameId = msg.getGameId();
        int numPlayers = msg.getNumPlayers();

        Map<Integer, GameController> lobbies = mngr.getLobbies();

        VirtualSocketView view = new VirtualSocketView(nickname, out);
        boolean success;

        if(lobbies.containsKey(gameId)){
            success = lobbies.get(gameId).addPlayer(view, nickname);
            System.out.println("Player " + nickname + " added to lobby " + gameId + " through socket.");
        } else {
            lobbies.put(gameId, new GameController(numPlayers));
            success = lobbies.get(gameId).addPlayer(view, nickname);
            System.out.println("Player " + nickname + " created lobby " + gameId + " through socket.");
        }

        if (!success) {
           //messaggio errore
        }

        return UUID.randomUUID().toString();
    }

    public void handleDrawCard(DrawCardMessage msg) {
        String token = msg.getToken();

        //gameController.drawCard(msg.getNickname(), msg.getUpperRow(), msg.getIndex());
    }

    public void handleSkip(SkipBonusMessage msg) {}

    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
