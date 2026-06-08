package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.messages.*;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public class VirtualSocketView implements VirtualView {

    private final String nickname;
    private final ObjectOutputStream out;

    public VirtualSocketView(String nickname, ObjectOutputStream out) {
        this.nickname = nickname;
        this.out = out;
    }

    @Override
    public void eventResolution(EventCard card) throws RemoteException {
        sendMessage(new EventResolutionMessage(card));
    }

    @Override
    public void updateBoard(BoardSnapshot board, List<PlayerSnapshot> players) throws RemoteException {
        sendMessage(new BoardUpdateMessage(board, players));
    }

    @Override
    public void showError(String errorMessage) throws RemoteException {
        sendMessage(new ErrorMessage(errorMessage));
    }

    @Override
    public void notifyTurn(String currentPlayerNickname, String gamePhase, int round, int era) throws RemoteException {
        sendMessage(new TurnNotificationMessage(currentPlayerNickname, gamePhase, round, era));
    }

    @Override
    public void notifyGameEnd(List<String> rankings, List<LeaderboardEntryBean> globalRanks)throws RemoteException {
        sendMessage(new EndGameMessage(rankings, globalRanks));
    }

    @Override
    public void showMessage(String message) throws RemoteException {
        sendMessage(new RegularMessage(message));
    }

    @Override
    public void showChatMessage(String sender, String message) throws RemoteException {
        sendMessage(new BroadcastChatMessage(sender, message));
    }

    @Override
    public void askBonusExtraPick() throws RemoteException {
        sendMessage(new BonusPickMessage());
    }

    @Override
    public void ping() throws RemoteException {

    }

    @Override
    public void notifyTotemSelected() throws RemoteException {
        sendMessage(new TotemSelectedMessage());
    }

    @Override
    public void notifyGameParticipation() throws RemoteException {
        sendMessage(new GameParticipationMessage());
    }

    @Override
    public void updateAvailableTotems(Set<Totem> totems) throws RemoteException {
        sendMessage(new AvailableTotemsMessage(totems));
    }

    private void sendMessage(Serializable message) throws RemoteException {
        synchronized (out) {
            try {
                out.writeObject(message);
                out.flush();
                out.reset();
            } catch (IOException e) {
                throw new RemoteException("Socket network error", e);
            }
        }
    }
}
