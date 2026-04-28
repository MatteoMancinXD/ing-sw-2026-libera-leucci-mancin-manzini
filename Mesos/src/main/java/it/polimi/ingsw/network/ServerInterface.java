package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.rmi.ClientRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Map;

public interface ServerInterface extends Remote{

    //String login(String nickname, int gameId, int numPlayers, ClientRemote clientStub) throws RemoteException;

    void drawCard(String token, boolean row, int idx) throws RemoteException;

    void placeTotem(String token, int tileIndex) throws RemoteException;

    void skipBonusPick(String token) throws RemoteException;

    Map<Integer, String> getAvailableGames() throws RemoteException;
    //Map<Integer, GameController> getStartedGames() throws RemoteException;

    int createGame(String gameMaster, int numPlayers, ClientRemote clientStub) throws RemoteException;
    void joinGame(String nickname, int gameID, ClientRemote clientStub) throws RemoteException, IllegalArgumentException;
}
