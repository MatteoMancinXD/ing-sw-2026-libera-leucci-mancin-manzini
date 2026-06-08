package it.polimi.ingsw.network.rmi;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;
import it.polimi.ingsw.view.ui;

import java.util.List;
import java.util.Set;

public class TestUI implements ui{
    private String lastMessage = "";
    private String lastErrorMessage = "";
    private BoardSnapshot lastBoard = null;

    @Override
    public void showError(String msg) {
        this.lastErrorMessage = msg;
    }

    public String getLastErrorMessage() {
        return lastErrorMessage;
    }

    public void updateBoard(BoardSnapshot board, List<PlayerSnapshot> players){
        this.lastBoard = board;
    }

    public BoardSnapshot getLastBoard() {
        return lastBoard;
    }

    public void notifyTurn(String currentPlayerNickname, String gamePhase, int round, int era){}

    @Override
    public void notifyEndGame(List<String> rankings, List<LeaderboardEntryBean> globalRanks) {

    }

    public void notifyEndGame(List<String> rankings){}

    public void showMessage(String message){
        this.lastMessage = message;
    }

    @Override
    public void showChatMessage(String sender, String message) {

    }

    @Override
    public void showAvailableTotems(Set<Totem> totems) {

    }

    @Override
    public void onTotemSelected() {

    }

    @Override
    public void onGameParticipation(Set<Totem> totems) {

    }

    public String getLastMessage() {
        return lastMessage;
    }

}

