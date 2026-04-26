package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

public class VirtualRMIServer extends UnicastRemoteObject implements ServerInterface {

    private Map<Integer, GameController> lobbies;

    public VirtualRMIServer(Map<Integer, GameController> lobbies) throws RemoteException {
        super();
        this.lobbies = lobbies;
    }

    @Override
    public void login(String nickname, int gameId, int numPlayers, ClientRemote clientStub) throws RemoteException {
        VirtualRMIView view = new VirtualRMIView(nickname, clientStub);
        if(lobbies.containsKey(gameId)){
            lobbies.get(gameId).addPlayer(view, nickname);
            System.out.println(nickname + " participates to game " + gameId);
        } else {
            lobbies.put(gameId, new GameController(numPlayers));
            lobbies.get(gameId).addPlayer(view, nickname);
            System.out.println(nickname + " creates game " + gameId);
        }
    }

    @Override
    public void drawCard(String nickname, int gameId, boolean row, int idx) throws RemoteException {
        lobbies.get(gameId).drawCard(nickname, row, idx);
    }

    @Override
    public void  placeTotem(String nickname, int gameId, int tileIndex) throws RemoteException {
        lobbies.get(gameId).placeTotem(nickname, tileIndex);
    }

    @Override
    public void skipBonusPick() throws RemoteException {

    }

}
