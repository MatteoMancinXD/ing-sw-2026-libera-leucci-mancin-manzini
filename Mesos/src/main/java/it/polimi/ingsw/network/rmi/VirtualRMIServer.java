package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class VirtualRMIServer extends UnicastRemoteObject implements ServerInterface {

    private final GameController gameController;

    public VirtualRMIServer(GameController gameController) throws RemoteException {
        super();
        this.gameController = gameController;
    }

    @Override
    public void login(String nickname, ClientRemote clientStub) throws RemoteException {
        VirtualRMIView view = new VirtualRMIView(nickname, clientStub);
        gameController.addPlayer(view, nickname);
    }

    @Override
    public void drawCard(String nickname, boolean row, int idx) throws RemoteException {
        gameController.drawCard(nickname, row, idx);
    }

    @Override
    public void  placeTotem(String nickname, int tileIndex) throws RemoteException {
        gameController.placeTotem(nickname, tileIndex);
    }

    @Override
    public void skipBonusPick() throws RemoteException {

    }

}
