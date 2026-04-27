package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.GameSession;
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

    public void handleLogin(LoginMessage msg) {
        this.nickname = msg.getNickname();
        int gameID = msg.getGameId();
        int numPlayers = msg.getNumPlayers();

        Map<Integer, GameController> lobbies = mngr.getLobbies();

        VirtualSocketView view = new VirtualSocketView(nickname, out);
        boolean success;

        if(lobbies.containsKey(gameID)){
            success = lobbies.get(gameID).addPlayer(view, nickname);
            //System.out.println("Player " + nickname + " added to lobby " + gameID + " through socket.");
        } else {
            lobbies.put(gameID, new GameController(gameID, numPlayers));
            success = lobbies.get(gameID).addPlayer(view, nickname);
            //System.out.println("Player " + nickname + " created lobby " + gameID + " through socket.");
        }

        if (!success) {
           //messaggio errore
        }

        String token = UUID.randomUUID().toString();
        GameSession session = new GameSession(gameID, nickname);

        mngr.getSessions().put(token, session);
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
