package it.polimi.ingsw.network;

import java.rmi.RemoteException;

public interface NetworkClient {

    void requestAvailableGames() throws RemoteException;

    void createGame(String nickname, int numPlayers) throws RemoteException;

    void joinGame(String nickname, int gameID) throws RemoteException;

    void askToDrawCard(boolean row, int index);

    void askToPlaceTotem(int pos);

    void askToSkipBonus();

    void sendChatMessage(String token, String message) throws RemoteException;

    void askToEndTurn();

    void disconnect();

}
