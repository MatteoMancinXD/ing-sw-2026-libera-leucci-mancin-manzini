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

    @Override
    public void drawCard(String token, boolean row, int idx) throws RemoteException {
        GameSession session = mngr.getSessions().get(token);
        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController ctrl =  mngr.getStartedGames().get(gameID);

        ctrl.drawCard(nickname, row, idx);
    }

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
        mngr.getSessions().remove(token);
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

    @Override
    public Set<Totem> getAvailableTotems(String token) {
        GameSession session = mngr.getSessions().get(token);

        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController controller = mngr.getAvailableGames().get(gameID);

        return controller.getAvailableTotems();
    }

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

    @Override
    public int createGame(String gameMaster, int numPlayers, ClientRemote clientStub) throws RemoteException{
        VirtualRMIView view = new VirtualRMIView(gameMaster, clientStub);

        int gameID = mngr.getIdCounter();

        GameController ctrl = new GameController(gameID, gameMaster, numPlayers, mngr);

        mngr.getAvailableGames().put(gameID, ctrl);
        //mngr.getAvailableGames().get(gameID).addPlayer(view, gameMaster);
        ctrl.addPlayer(view, gameMaster);

        String token = UUID.randomUUID().toString();
        mngr.getSessions().put(token, new GameSession(gameID, gameMaster));
        clientStub.receiveToken(token);

        return gameID;
    }

    @Override
    public void joinGame(String nickname, int gameID, ClientRemote clientStub) throws RemoteException, IllegalArgumentException {
        VirtualRMIView view = new VirtualRMIView(nickname, clientStub);
        GameController startedCtrl = mngr.getStartedGames().get(gameID);
        if (startedCtrl != null && startedCtrl.isPlayerDisconnected(nickname)) {
            boolean reconnected = startedCtrl.reconnect(nickname, view);
            if (reconnected) {
                String token = UUID.randomUUID().toString();
                mngr.getSessions().put(token, new GameSession(gameID, nickname));
                clientStub.receiveToken(token);
                return;
            }
        }

        // fallback: cerca in startedGames anche se non nel set disconnessi
        if (startedCtrl != null) {
            boolean reconnected = startedCtrl.reconnect(nickname, view);
            if (reconnected) {
                String token = UUID.randomUUID().toString();
                mngr.getSessions().put(token, new GameSession(gameID, nickname));
                clientStub.receiveToken(token);
                return;
            }
        }
        if (!mngr.getAvailableGames().containsKey(gameID)) {
            throw new IllegalArgumentException("Game with ID " + gameID + " does not exist!");
        }

        GameController ctrl = mngr.getAvailableGames().get(gameID);
        boolean success = ctrl.addPlayer(view, nickname);
        if (!success) {
            view.showError("Nickname already in use or match already filled");
            return;
        }

        String token = UUID.randomUUID().toString();
        mngr.getSessions().put(token, new GameSession(gameID, nickname));
        clientStub.receiveToken(token);
    }
}
