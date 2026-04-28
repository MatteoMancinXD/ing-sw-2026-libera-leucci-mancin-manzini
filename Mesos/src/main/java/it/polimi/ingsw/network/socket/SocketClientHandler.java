package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.GameSession;
import it.polimi.ingsw.network.messages.*;

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
    private String token;

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
                if (message.requiresToken() && !validateToken(message.getToken())) {
                    sendMessage(new ErrorMessage("Token non valido"));
                    continue;
                }
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

        if (mngr.getLobbies().containsKey(gameID)) {
            mngr.getLobbies().get(gameID).addPlayer(view, nickname);
            System.out.println("Player " + nickname + " added to lobby " + gameID + " through socket.");
        } else {
            mngr.getLobbies().put(gameID, new GameController(gameID, numPlayers));
            mngr.getLobbies().get(gameID).addPlayer(view, nickname);
            System.out.println("Player " + nickname + " created lobby " + gameID + " through socket.");
        }

        this.token = UUID.randomUUID().toString();
        mngr.getSessions().put(token, new GameSession(gameID, nickname));
        sendMessage(new LoginResponseMessage(token));
    }

    public void handleDrawCard(DrawCardMessage msg) {
        GameController ctrl = getController();
        if (ctrl != null) ctrl.drawCard(nickname, msg.getUpperRow(), msg.getIndex());
    }

    public void handleSkip(SkipBonusMessage msg) {
        GameController ctrl = getController();
        if (ctrl != null) ctrl.skipExtraPick(nickname);
    }

    public void handlePlaceTotem(PlaceTotemMessage msg) {
        GameController ctrl = getController();
        if (ctrl != null) ctrl.placeTotem(nickname, msg.getPos());
    }

    private GameController getController() {
        GameSession session = mngr.getSessions().get(token);
        if (session == null) return null;
        return mngr.getLobbies().get(session.getGameID());
    }

    private boolean validateToken(String receivedToken) {
        return this.token != null && this.token.equals(receivedToken);
    }

    private void sendMessage(ServerToClientMessage message) {
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.err.println("Error sending a message to " + nickname);
        }
    }


    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
