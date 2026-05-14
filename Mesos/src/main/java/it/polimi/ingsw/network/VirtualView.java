package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;

import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.util.List;

public interface VirtualView {

    //Interfaccia che rappresenta un generico client

    void eventResolution(EventCard card) throws RemoteException;

    void updateBoard(Board board, List<Player> players) throws RemoteException;

    void notifyTurn(String currentPlayerNickname, String gamePhase) throws RemoteException;

    void showError(String errorMessage) throws RemoteException;

    void askBonusExtraPick() throws RemoteException;

    void showMessage(String message) throws RemoteException;

    void showChatMessage(String sender, String message) throws RemoteException;

    void notifyGameEnd(List<String> rankings, List<LeaderboardEntryBean> globalRanks) throws RemoteException;

    void ping() throws RemoteException;
}
