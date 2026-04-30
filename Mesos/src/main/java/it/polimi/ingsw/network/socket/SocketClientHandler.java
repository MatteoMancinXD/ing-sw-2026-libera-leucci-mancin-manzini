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
                    sendMessage(new ErrorMessage("Token not valid"));
                    continue;
                }
                message.process(this);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Player " + nickname + " disconnected.");
        } finally {
            if (nickname != null) {
                // cerca in startedGames prima, poi in availableGames
                GameController ctrl = getController();
                if (ctrl == null && token != null) {
                    GameSession session = mngr.getSessions().get(token);
                    if (session != null) {
                        ctrl = mngr.getAvailableGames().get(session.getGameID());
                    }
                }
                if (ctrl != null) {
                    ctrl.handleDisconnection(nickname);
                }
            }
            closeConnection();
        }
    }

//    public void handleLogin(LoginMessage msg) {
//        this.nickname = msg.getNickname();
//        int gameID = msg.getGameId();
//        int numPlayers = msg.getNumPlayers();
//
//        Map<Integer, GameController> availableGames = mngr.getAvailableGames();
//
//        VirtualSocketView view = new VirtualSocketView(nickname, out);
//        boolean success;
//
//        if (mngr.getAvailableGames().containsKey(gameID)) {
//            mngr.getAvailableGames().get(gameID).addPlayer(view, nickname);
//            System.out.println("Player " + nickname + " added to lobby " + gameID + " through socket.");
//        } else {
//            mngr.getAvailableGames().put(gameID, new GameController(gameID, nickname, numPlayers, mngr));
//            mngr.getAvailableGames().get(gameID).addPlayer(view, nickname);
//            System.out.println("Player " + nickname + " created lobby " + gameID + " through socket.");
//        }
//
//        this.token = UUID.randomUUID().toString();
//        mngr.getSessions().put(token, new GameSession(gameID, nickname));
//        sendMessage(new LoginResponseMessage(token));
//    }

    public void handleCreateGame(CreateGameMessage msg) {
        VirtualSocketView view = new VirtualSocketView(nickname, out);

        System.out.println("CreateGameMessage received");

        this.nickname = msg.getNickname();
        int numPlayers =  msg.getNumPlayers();

        Map<Integer, GameController> availableGames = mngr.getAvailableGames();
        int gameID = mngr.getIdCounter();

        GameController ctrl = new GameController(gameID, nickname, numPlayers, mngr);
        availableGames.put(gameID, ctrl);
        ctrl.addPlayer(view, nickname);

        this.token = UUID.randomUUID().toString();
        mngr.getSessions().put(this.token, new GameSession(gameID, nickname));
        sendMessage(new TokenResponseMessage(this.token));
    }

    public void handleJoinGame(JoinGameMessage msg) {
        this.nickname = msg.getNickname();
        int gameID = msg.getGameID();
        VirtualSocketView view = new VirtualSocketView(nickname, out);

        System.out.println("JoinGameMessage received");

        // controlla se è una riconnessione a una partita avviata

        GameController startedCtrl = mngr.getStartedGames().get(gameID);
        if (startedCtrl != null && startedCtrl.isPlayerDisconnected(nickname)) {
            boolean reconnected = startedCtrl.reconnect(nickname, view);
            if (reconnected) {
                this.token = UUID.randomUUID().toString();
                mngr.getSessions().put(this.token, new GameSession(gameID, nickname));
                sendMessage(new TokenResponseMessage(this.token));
                System.out.println(nickname + " reconnected to game #" + gameID);
                return;
            }
    }
        // join normale a partita in attesa

        GameController ctrl = mngr.getAvailableGames().get(gameID);
        if (ctrl == null) {
            // fallback: cerca in startedGames per riconnessione anche se non nel set disconnessi
            ctrl = mngr.getStartedGames().get(gameID);
            if (ctrl != null) {
                boolean reconnected = ctrl.reconnect(nickname, view);
                if (reconnected) {
                    this.token = UUID.randomUUID().toString();
                    mngr.getSessions().put(this.token, new GameSession(gameID, nickname));
                    sendMessage(new TokenResponseMessage(this.token));
                    return;
                }
            }
            sendMessage(new ErrorMessage("Game " + gameID + " not found"));
            return;
        }

        ctrl.addPlayer(view, nickname);

        this.token = UUID.randomUUID().toString();
        mngr.getSessions().put(this.token, new GameSession(gameID, nickname));
        sendMessage(new TokenResponseMessage(this.token));
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
        System.out.println("PlaceTotemMessage received");
        if (ctrl != null) {
            ctrl.placeTotem(nickname, msg.getPos());
            System.out.println("Totem placed.");
        }
    }

    public void handleRequestGames(RequestGamesMessage msg) {
        Map<Integer, String> games = mngr.getGamesIDAndMaster();
        sendMessage(new AvailableGamesMessage(games));
    }

    public void handleChatMessage(ChatMessage msg) {
        GameController ctrl = getController();
        if (ctrl != null) {
            ctrl.broadcastChatMessage(nickname, msg.getMessage());
        }
    }

    private GameController getController() {
        GameSession session = mngr.getSessions().get(token);
        if (session == null) return null;
        return mngr.getStartedGames().get(session.getGameID());
    }

    private boolean validateToken(String receivedToken) {
        return this.token != null && this.token.equals(receivedToken);
    }

    private void sendMessage(ServerToClientMessage message) {
        synchronized (out) {
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            System.err.println("Error sending a message to " + nickname);
        }
    }}



    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
