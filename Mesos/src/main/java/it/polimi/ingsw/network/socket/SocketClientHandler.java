package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.GameSession;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.Set;
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

    /**
     * Creates a game with the properties expressed in the message, adds the sender
     * to the list of players for the game, sends him his token and registers the session.
     * @param msg: Socket message used to express the intention to create a game
     */
    public void handleCreateGame(CreateGameMessage msg) {
        VirtualSocketView view = new VirtualSocketView(nickname, out);

        System.out.println("CreateGameMessage received");

        this.nickname = msg.getNickname();
        int numPlayers =  msg.getNumPlayers();

        if(numPlayers < 2 || numPlayers > 5) {
            sendMessage(new ErrorMessage("Invalid number of players"));
            return;
        }

        Map<Integer, GameController> availableGames = mngr.getAvailableGames();
        int gameID = mngr.getIdCounter();

        GameController ctrl = new GameController(gameID, nickname, numPlayers);
        ctrl.addStarter(mngr);

        availableGames.put(gameID, ctrl);
        ctrl.addPlayer(view, nickname);

        this.token = UUID.randomUUID().toString();
        mngr.getSessions().put(this.token, new GameSession(gameID, nickname));
        sendMessage(new TokenResponseMessage(this.token));
    }

    /**
     * Adds the sender to the list of players of the game specified in the message,
     * sends him his token and registers his session.
     * @param msg: Socket message used to declare which game is to be joined
     */
    public void handleJoinGame(JoinGameMessage msg) {
        VirtualSocketView view = new VirtualSocketView(nickname, out);

        System.out.println("JoinGameMessage received");

        this.nickname = msg.getNickname();
        int gameID = msg.getGameID();

        GameController ctrl = mngr.getAvailableGames().get(gameID);
        boolean success = ctrl.addPlayer(view, nickname);
        if (!success) {
            sendMessage(new ErrorMessage("Nickname already in use or match already filled"));
            return;
        }

        this.token = UUID.randomUUID().toString();
        mngr.getSessions().put(this.token, new GameSession(gameID, nickname));
        sendMessage(new TokenResponseMessage(this.token));
    }

    /**
     * Gets the game's controller to handle the card drawing
     * @param msg: contains info about the card to be drawn
     */
    public void handleDrawCard(DrawCardMessage msg) {
        GameController ctrl = getController();
        if (ctrl != null) ctrl.drawCard(nickname, msg.getUpperRow(), msg.getIndex());
    }

    public void handleSkip(SkipBonusMessage msg) {
        GameController ctrl = getController();
        if (ctrl != null) ctrl.skipExtraPick(nickname);
    }

    /**
     * Gets the game's controller to handle the totem placement.
     * @param msg: contains info about where to place the totem
     */
    public void handlePlaceTotem(PlaceTotemMessage msg) {
        GameController ctrl = getController();
        System.out.println("PlaceTotemMessage received");
        if (ctrl != null) {
            ctrl.placeTotem(nickname, msg.getPos());
            System.out.println("Totem placed.");
        }
    }

    /**
     * Gets the list of games available for joining and sends it to the requester
     * @param msg: contains the list of available games
     */
    public void handleRequestGames(RequestGamesMessage msg) {
        Map<Integer, String> games = mngr.getGamesIDAndMaster();
        sendMessage(new AvailableGamesMessage(games));
    }

    /**
     * Gets the game's controller to handle the delivery of the message
     * @param msg: contains the message to be sent
     */
    public void handleChatMessage(ChatMessage msg) {
        GameController ctrl = getController();
        if (ctrl != null) {
            ctrl.broadcastChatMessage(nickname, msg.getMessage());
        }
    }

    /**
     * Gets the game controller corresponding to the session the token is linked to.
     * @return the aforementioned game controller
     */
    private GameController getController() {
        GameSession session = mngr.getSessions().get(token);
        if (session == null) return null;
        return mngr.getStartedGames().get(session.getGameID());
    }

    /**
     * checks the validity of the received token (if it is requested)
     * @param receivedToken: token passed from the message
     * @return true if token is valid, false otherwise
     */
    private boolean validateToken(String receivedToken) {
        return this.token != null && this.token.equals(receivedToken);
    }

    /**
     * sends a ServerToClientMessage to the client
     * @param message: message to be sent
     */
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
            System.out.println("Failed to close connection");
            e.printStackTrace();
        }
    }

    /**
     * Gets the set of available totems and sends it to the requester
     * @param msg: message that demands the set of available totems
     */
    public void handleRequestTotems(RequestTotemsMessage msg) {
        String token = msg.getToken();

        GameSession session = mngr.getSessions().get(token);
        int gameID = session.getGameID();
        GameController ctrl = mngr.getAvailableGames().get(gameID);

        Set<Totem> totems = ctrl.getAvailableTotems();

        sendMessage(new AvailableTotemsMessage(totems));
    }

    /**
     * Gets the game's controller to handle the selection of the specified totem by the player
     * @param msg: contains info about the totem to select
     */
    public void handleSelectTotems(SelectTotemMessage msg) {
        String token = msg.getToken();
        Totem totem = msg.getTotem();

        GameSession session = mngr.getSessions().get(token);
        String nickname = session.getNickname();
        int gameID = session.getGameID();
        GameController ctrl = mngr.getAvailableGames().get(gameID);

        ctrl.selectTotem(nickname, totem);
    }
}
