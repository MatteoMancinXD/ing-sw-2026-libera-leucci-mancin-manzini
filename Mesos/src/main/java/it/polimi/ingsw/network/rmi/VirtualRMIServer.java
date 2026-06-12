package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.GameSession;
import it.polimi.ingsw.network.ServerInterface;
import it.polimi.ingsw.network.messages.ErrorMessage;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

/**
 * Server-side RMI endpoint that implements {@link ServerInterface}.
 * Receives remote method calls from {@link RmiClient} instances and
 * delegates them to the appropriate {@link GameController} via {@link GameManager}.
 * Handles game creation, joining, totem selection, card drawing, totem placement,
 * chat messages, and session management through UUID-based tokens.
 *
 * @see ServerInterface
 * @see GameManager
 * @see RmiClient
 */
public class VirtualRMIServer extends UnicastRemoteObject implements ServerInterface {

    private GameManager mngr;

    public VirtualRMIServer(GameManager mngr) throws RemoteException {
        super();
        this.mngr = mngr;
    }

//    @Override
//    public String login(String nickname, int gameID, int numPlayers, ClientRemote clientStub) throws RemoteException, IllegalArgumentException {
//        VirtualRMIView view = new VirtualRMIView(nickname, clientStub);
//        Map<Integer, GameController> availableGames = mngr.getAvailableGames();
//
//        if(availableGames.containsKey(gameID)){
//            availableGames.get(gameID).addPlayer(view, nickname);
//            //System.out.println(nickname + " participates to game " + gameID + " through RMI.");
//        } else {
//            if (numPlayers < 2) {throw new IllegalArgumentException("Players cannot be less than 2");}
//            availableGames.put(gameID, new GameController(gameID, numPlayers));
//            availableGames.get(gameID).addPlayer(view, nickname);
//            //System.out.println(nickname + " creates game " + gameID +  " through RMI.");
//        }
//
//        String token = UUID.randomUUID().toString();
//        GameSession session = new GameSession(gameID, nickname);
//
//        mngr.getSessions().put(token, session);
//
//        return token;
//    }

    /**
     * Processes a card draw request from an RMI client.
     * @param token the client's session token
     * @param row   true for upper row, false for lower row
     * @param idx   index of the card to draw
     */
    @Override
    public void drawCard(String token, boolean row, int idx) throws RemoteException {
        GameSession session = mngr.getSessions().get(token);
        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController ctrl =  mngr.getStartedGames().get(gameID);

        ctrl.drawCard(nickname, row, idx);
    }

    /**
     * Processes a totem placement request from an RMI client.
     * @param token     the client's session token
     * @param tileIndex index of the tile to place the totem on
     */
    @Override
    public void  placeTotem(String token, int tileIndex) throws RemoteException {
        GameSession session = mngr.getSessions().get(token);
        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController ctrl =  mngr.getStartedGames().get(gameID);

        ctrl.placeTotem(nickname, tileIndex);
    }

    @Override
    public void skipBonusPick(String token) throws RemoteException {
        int id = mngr.getSessions().get(token).getGameID();
        GameSession session = mngr.getSessions().get(token);

        GameController  controller = mngr.getAvailableGames().get(id);
        if (controller == null) {
            controller = mngr.getStartedGames().get(id);
        }

        if (controller != null) {
            controller.skipExtraPick(session.getNickname());
        }
    }

    @Override
    public void logout(String token) throws RemoteException{
        GameSession session = mngr.getSessions().remove(token);
    }

    @Override
    public void serverEndTurn(String token) throws RemoteException {  //checks if gameController controller exists if yes controllerEndTurn
        GameSession session = mngr.getSessions().get(token);

        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController controller = mngr.getStartedGames().get(gameID);
        if(controller == null){
            controller = mngr.getAvailableGames().get(gameID);
        }

        if(controller != null) controller.controllerEndTurn(nickname); //controllerEndTurn manually ends the turn asking the Model
    }

    /**
     * Returns the set of totem colors not yet chosen in the specified game.
     * @param token the client's session token
     * @return set of available totem colors
     */
    @Override
    public Set<Totem> getAvailableTotems(String token) {
        GameSession session = mngr.getSessions().get(token);

        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController controller = mngr.getAvailableGames().get(gameID);

        return controller.getAvailableTotems();
    }

    /**
     * Processes a totem color selection from an RMI client.
     * @param token the client's session token
     * @param totem the chosen totem color
     */
    @Override
    public void selectTotem(String token, Totem totem) throws RemoteException {
        GameSession session = mngr.getSessions().get(token);

        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController controller = mngr.getAvailableGames().get(gameID);
        if(controller != null) {
            controller.selectTotem(nickname, totem);
        }
    }

    @Override
    public void sendChatMessage(String token,String message) throws RemoteException{
        GameSession session = mngr.getSessions().get(token);

        if(session == null) {
            throw new RemoteException("Chat rejected: invalid token");
        }

        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController controller = mngr.getStartedGames().get(gameID);
        if (controller == null) {
            controller = mngr.getAvailableGames().get(gameID);
        }

        if (controller != null) {
            controller.broadcastChatMessage(nickname, message);  //logic here
        } else {
            throw new RemoteException("Chat error: Game " + gameID + " not found.");
        }
    }

    @Override
    public Map<Integer, String> getAvailableGames() {
        return mngr.getGamesIDAndMaster();
    }

    /**
     * Creates a new game lobby with the specified player count.
     * Generates a unique game ID, creates the controller, adds the creator as
     * the first player, and sends a session token to the client.
     * @param gameMaster the nickname of the player creating the game
     * @param numPlayers the number of players for the game (2-5)
     * @param clientStub the remote client stub for callbacks
     * @return the unique game ID
     */
    @Override
    public int createGame(String gameMaster, int numPlayers, ClientRemote clientStub) throws RemoteException{
        VirtualRMIView view = new VirtualRMIView(gameMaster, clientStub);

        int gameID = mngr.getIdCounter();

        GameController ctrl = new GameController(gameID, gameMaster, numPlayers);
        ctrl.addStarter(mngr);

        mngr.getAvailableGames().put(gameID, ctrl);
        mngr.getAvailableGames().get(gameID).addPlayer(view, gameMaster);

        String token = UUID.randomUUID().toString();
        GameSession session = new GameSession(gameID, gameMaster);

        mngr.getSessions().put(token, session);
        clientStub.receiveToken(token);

        return gameID;
    }


    /**
     * Joins an existing game lobby. Validates that the game exists and is not full,
     * then adds the player and sends a session token.
     * @param nickname   the joining player's nickname
     * @param gameID     the ID of the game to join
     * @param clientStub the remote client stub for callbacks
     * @throws IllegalArgumentException if the game does not exist
     */
    @Override
    public void joinGame(String nickname, int gameID, ClientRemote clientStub) throws RemoteException, IllegalArgumentException {
        VirtualRMIView view = new VirtualRMIView(nickname, clientStub);
        if(!mngr.getAvailableGames().containsKey(gameID)){
            throw new IllegalArgumentException("Game with ID " + gameID + " does not exist!");
        }

        GameController ctrl = mngr.getAvailableGames().get(gameID);
        boolean success = ctrl.addPlayer(view, nickname);
        if (!success) {
            view.showError("Nickname already in use or match already filled");
            return;
        }

        String token = UUID.randomUUID().toString();
        GameSession session =  new GameSession(gameID, nickname);
        mngr.getSessions().put(token, session);

        clientStub.receiveToken(token);
    }
}
