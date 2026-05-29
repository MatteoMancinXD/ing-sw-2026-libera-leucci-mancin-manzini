package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface ui {
    //Tutti i metodi che hanno a che fare con modifiche della grafica, potrebbero mancarne alcuni
    void updateBoard(BoardSnapshot board, List<PlayerSnapshot> players);

    void showError(String errorMessage);

    void notifyTurn(String currentPlayerNickname, String gamePhase, int round, int era);

    void notifyEndGame(List<String> rankings, List<LeaderboardEntryBean> globalRanks);

    void showMessage(String message);

    void showChatMessage(String sender, String message);

    void showAvailableTotems(Set<Totem> totems);

    void onTotemSelected();

    void onGameParticipation();
}
