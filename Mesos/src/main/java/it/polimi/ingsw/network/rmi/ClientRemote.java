package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface ClientRemote extends Remote{

    void receiveBoardUpdate(Board board) throws RemoteException;

    void receiveError(String errorMessage) throws RemoteException;

    void receiveTurnNotification(String currentPlayerNickname) throws RemoteException;

    void receiveAskBonusExtraPick() throws RemoteException;

    void receiveMessage(String message) throws RemoteException;

    void receiveGameEnd(List<String> rankings) throws RemoteException;

    void receiveToken(String token) throws RemoteException;

    void ping() throws RemoteException;

}
