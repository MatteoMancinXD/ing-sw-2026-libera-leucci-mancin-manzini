package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.view.ui;

import java.util.List;

public class TestUI implements ui{
    private String lastMessage = "";
    private String lastErrorMessage = "";
    private Board lastBoard = null;

    @Override
    public void showError(String msg) {
        this.lastErrorMessage = msg;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void updateBoard(Board board, List<Player> players){
        this.lastBoard = board;
    }

    public Board getLastBoard() {
        return lastBoard;
    }

    public void notifyTurn(String currentPlayerNickname, String gamePhase){}

    public void notifyEndGame(List<String> rankings){}

    public void showMessage(String message){
        this.lastMessage = message;
    }
    public String getLastMessage() {
        return lastMessage;
    }

}

