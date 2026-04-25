package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;
import java.util.List;

public class VirtualRMIView implements VirtualView {

    private final String nickname;
    private final ClientRemote clientStub;

    public VirtualRMIView(String nickname, ClientRemote clientStub) {
        this.nickname = nickname;
        this.clientStub = clientStub;
    }

    public String getNickname() {
        return nickname;
    }

    @Override
    public void updateBoard(Board board) throws RemoteException {
        clientStub.receiveBoardUpdate(board);
    }
    @Override
    public void notifyTurn(String currentPlayerNickname) throws RemoteException {
        clientStub.receiveTurnNotification(currentPlayerNickname);
    }

    @Override
    public void showError(String errorMessage) throws RemoteException {
        clientStub.receiveError(errorMessage);
    }

    @Override
    public void askBonusExtraPick() throws RemoteException {
        clientStub.receiveAskBonusExtraPick();
    }

    @Override
    public void showMessage(String message) throws RemoteException {
        clientStub.receiveMessage(message);
    }

    @Override
    public void notifyGameEnd(List<String> rankings) throws RemoteException {
        clientStub.receiveGameEnd(rankings);
    }

    @Override
    public void ping() throws RemoteException {
        clientStub.ping();
    }
}
