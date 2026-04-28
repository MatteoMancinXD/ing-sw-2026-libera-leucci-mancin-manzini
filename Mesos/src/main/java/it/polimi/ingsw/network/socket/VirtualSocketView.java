package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public class VirtualSocketView  implements VirtualView {

    private final String nickname;
    private final ObjectOutputStream out;

    public VirtualSocketView(String nickname, ObjectOutputStream out) {
        this.nickname = nickname;
        this.out = out;
    }

    @Override
    public void updateBoard(Board board, List<Player> players) throws RemoteException {
            sendMessage(new BoardUpdateMessage(board, players));
    }

    @Override
    public void  showError(String errorMessage) throws RemoteException {
        sendMessage(new ErrorMessage(errorMessage));
    }

    @Override
    public void notifyTurn(String currentPlayerNickname) throws RemoteException {
        sendMessage(new TurnNotificationMessage(currentPlayerNickname));
    }

    @Override
    public void notifyGameEnd(List<String> rankings)throws RemoteException {
        sendMessage(new EndGameMessage(rankings));
    }

    @Override
    public void showMessage(String message) throws RemoteException {
        sendMessage(new RegularMessage(message));
    }

    @Override
    public void askBonusExtraPick() throws RemoteException {
        sendMessage(new BonusPickMessage());
    }

    @Override
    public void ping() throws RemoteException {

    }

    private void sendMessage(Serializable message) throws RemoteException {
        try{
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            throw new RemoteException("Socket network error", e);
        }
    }

}
