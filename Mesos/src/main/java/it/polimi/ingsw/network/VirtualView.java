package it.polimi.ingsw.network;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;

import java.rmi.RemoteException;
import java.rmi.server.RemoteRef;
import java.util.List;
import java.util.Set;

public interface VirtualView {

    //Interfaccia che rappresenta un generico client

    void eventResolution(EventCard card) throws RemoteException;

    void updateBoard(BoardSnapshot board, List<PlayerSnapshot> players) throws RemoteException;

    void notifyTurn(String currentPlayerNickname, String gamePhase, int round, int era) throws RemoteException;

    void showError(String errorMessage) throws RemoteException;

    void askBonusExtraPick() throws RemoteException;

    void showMessage(String message) throws RemoteException;

    void showChatMessage(String sender, String message) throws RemoteException;

    void notifyGameEnd(List<String> rankings, List<LeaderboardEntryBean> globalRanks) throws RemoteException;

    void ping() throws RemoteException;

    void notifyTotemSelected() throws RemoteException;

    void notifyGameParticipation(Set<Totem> totems) throws RemoteException;

    void updateAvailableTotems(Set<Totem> totems) throws RemoteException;
}
