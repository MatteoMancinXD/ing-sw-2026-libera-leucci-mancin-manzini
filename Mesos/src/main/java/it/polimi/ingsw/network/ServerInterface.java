package it.polimi.ingsw.network;

import it.polimi.ingsw.network.rmi.ClientRemote;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Remote{

    void login(String nickname, ClientRemote clientStub) throws RemoteException;

    void drawCard(String nickname, boolean row, int idx) throws RemoteException;

    void placeTotem(String nickname, int tileIndex) throws RemoteException;

    void skipBonusPick() throws RemoteException;


}
