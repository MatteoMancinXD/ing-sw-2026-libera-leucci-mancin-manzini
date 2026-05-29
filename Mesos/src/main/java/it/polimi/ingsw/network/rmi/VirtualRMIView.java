package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;

import java.rmi.RemoteException;
import java.util.List;


//Client Handler RMI
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
    public void eventResolution(EventCard card) throws RemoteException {
        clientStub.receiveEventResolution(card);
    }

    @Override
    public void updateBoard(BoardSnapshot board, List<PlayerSnapshot> players) throws RemoteException {
        clientStub.receiveBoardUpdate(board, players);
    }
    @Override
    public void notifyTurn(String currentPlayerNickname, String gamePhase, int round, int era) throws RemoteException {
        clientStub.receiveTurnNotification(currentPlayerNickname, gamePhase, round, era);
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
    public void showChatMessage(String sender, String message) throws RemoteException {
        clientStub.receiveChatMessage(sender, message);
    }

    @Override
    public void notifyGameEnd(List<String> rankings, List<LeaderboardEntryBean> globalRanks) throws RemoteException {
        clientStub.receiveGameEnd(rankings, globalRanks);
    }

    @Override
    public void ping() throws RemoteException {
        clientStub.ping();
    }

    @Override
    public void notifyTotemSelected() throws RemoteException {
        clientStub.onTotemSelected();
    }

    @Override
    public void notifyGameParticipation() throws RemoteException {
        clientStub.onGameParticipation();
    }
}
