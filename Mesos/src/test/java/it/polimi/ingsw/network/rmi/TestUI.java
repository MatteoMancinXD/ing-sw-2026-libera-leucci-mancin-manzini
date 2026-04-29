package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ui;

import java.util.List;

public class TestUI implements ui{
    private String lastErrorMessage = "";

    @Override
    public void showError(String msg) {
        this.lastErrorMessage = msg;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void updateBoard(Board board, List<Player> players){}

    public void notifyTurn(String currentPlayerNickname, String gamePhase){}

    public void notifyEndGame(List<String> rankings){}

    public void showMessage(String message){}

}

