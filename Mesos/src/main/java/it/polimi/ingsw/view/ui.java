package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;

import java.rmi.RemoteException;
import java.util.List;
import java.util.Set;

public interface ui {
    //Tutti i metodi che hanno a che fare con modifiche della grafica, potrebbero mancarne alcuni
    void updateBoard(Board board, List<Player> players);

    void showError(String errorMessage);

    void notifyTurn(String currentPlayerNickname, String gamePhase);

    void notifyEndGame(List<String> rankings);

    void showMessage(String message);

    void showChatMessage(String sender, String message);

    void showAvailableTotems(Set<Totem> totems);
}
