package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;


public interface ClientRemote extends Remote{
    void receiveEventResolution(EventCard card) throws RemoteException;

    void receiveBoardUpdate(BoardSnapshot board, List<PlayerSnapshot> players) throws RemoteException;

    void receiveError(String errorMessage) throws RemoteException;

    void receiveTurnNotification(String currentPlayerNickname, String gamePhase, int round, int era) throws RemoteException;

    void receiveAskBonusExtraPick() throws RemoteException;

    void receiveMessage(String message) throws RemoteException;

    void receiveGameEnd(List<String> rankings, List<LeaderboardEntryBean> globalRanks) throws RemoteException;

    void receiveToken(String token) throws RemoteException;

    void receiveChatMessage(String sender, String message) throws RemoteException;

    void ping() throws RemoteException;

    void onTotemSelected() throws RemoteException;

    void onGameParticipation() throws RemoteException;
}
