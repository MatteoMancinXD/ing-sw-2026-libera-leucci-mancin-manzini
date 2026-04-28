package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.GameSession;
import it.polimi.ingsw.network.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;
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
    public void skipBonusPick(String token) throws RemoteException {  //pls check
        int id = mngr.getSessions().get(token).getGameID();

        GameSession session = mngr.getSessions().get(token);
        GameController  controller = mngr.getAvailableGames().get(id);
        controller.skipExtraPick(session.getNickname());
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

    @Override
    public void sendChatMessage(String token,String message) throws RemoteException{
        GameSession session = mngr.getSessions().get(token);

        if(session == null) {
            System.err.println("Chat rejected: Invalid token.");
            return;
        }

        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController controller = mngr.getStartedGames().get(gameID);
        if (controller == null) {
            controller = mngr.getAvailableGames().get(gameID);
        }

        if (controller != null) {
            controller.broadcastMessage(nickname + ": " + message);  //logic here
        } else {
            System.err.println("Chat error: Game " + gameID + " not found.");
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

        GameController ctrl = new GameController(gameID, gameMaster, numPlayers);

        mngr.getAvailableGames().put(gameID, ctrl);
        mngr.getAvailableGames().get(gameID).addPlayer(view, gameMaster);

        String token = UUID.randomUUID().toString();
        GameSession session = new GameSession(gameID, gameMaster);

        mngr.getSessions().put(token, session);
        clientStub.receiveToken(token);

        return gameID;
    }

    @Override
    public void joinGame(String nickname, int gameID, ClientRemote clientStub) throws RemoteException, IllegalArgumentException {
        VirtualRMIView view = new VirtualRMIView(nickname, clientStub);
        if(!mngr.getAvailableGames().containsKey(gameID)){
            throw new IllegalArgumentException("Game with ID " + gameID + " does not exist!");
        }

        GameController ctrl = mngr.getAvailableGames().get(gameID);
        ctrl.addPlayer(view, nickname);

        String token = UUID.randomUUID().toString();
        GameSession session =  new GameSession(gameID, nickname);
        mngr.getSessions().put(token, session);

        clientStub.receiveToken(token);
    }
}
