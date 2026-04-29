package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.rmi.RemoteException;
import java.util.List;

public interface VirtualView {

    //Interfaccia che rappresenta un generico client

    void updateBoard(Board board, List<Player> players) throws RemoteException;

    void notifyTurn(String currentPlayerNickname, String gamePhase) throws RemoteException;

    void showError(String errorMessage) throws RemoteException;

    void askBonusExtraPick() throws RemoteException;

    void showMessage(String message) throws RemoteException;

    void notifyGameEnd(List<String> rankings) throws RemoteException;

    void ping() throws RemoteException;
}
