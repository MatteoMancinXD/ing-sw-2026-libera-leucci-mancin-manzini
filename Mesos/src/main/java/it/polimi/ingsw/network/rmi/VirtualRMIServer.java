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

    @Override
    public String login(String nickname, int gameID, int numPlayers, ClientRemote clientStub) throws RemoteException {
        VirtualRMIView view = new VirtualRMIView(nickname, clientStub);
        Map<Integer, GameController> lobbies = mngr.getLobbies();

        if(lobbies.containsKey(gameID)){
            lobbies.get(gameID).addPlayer(view, nickname);
            //System.out.println(nickname + " participates to game " + gameID + " through RMI.");
        } else {
            lobbies.put(gameID, new GameController(gameID, numPlayers));
            lobbies.get(gameID).addPlayer(view, nickname);
            //System.out.println(nickname + " creates game " + gameID +  " through RMI.");
        }

        String token = UUID.randomUUID().toString();
        GameSession session = new GameSession(gameID, nickname);

        mngr.getSessions().put(token, session);

        return token;
    }

    @Override
    public void drawCard(String token, boolean row, int idx) throws RemoteException {
        GameSession session = mngr.getSessions().get(token);
        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController ctrl =  mngr.getLobbies().get(gameID);

        ctrl.drawCard(nickname, row, idx);
    }

    @Override
    public void  placeTotem(String token, int tileIndex) throws RemoteException {
        GameSession session = mngr.getSessions().get(token);
        int gameID = session.getGameID();
        String nickname = session.getNickname();

        GameController ctrl =  mngr.getLobbies().get(gameID);

        ctrl.placeTotem(nickname, tileIndex);
    }

    @Override
    public void skipBonusPick() throws RemoteException {

    }

    @Override
    public Map<Integer, GameController> getAvailableGames() {
        return mngr.getLobbies();
    }

}
