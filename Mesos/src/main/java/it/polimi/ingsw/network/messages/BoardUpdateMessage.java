package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;

import java.util.List;

public class BoardUpdateMessage extends ServerToClientMessage {

    private final Board board;
    List<Player> players;

    public BoardUpdateMessage(Board board, List<Player> players) {
        this.board = board;
    }
    public Board getBoard() {
        return board;
    }

    @Override
    public void process() {
        //userInterface.updateBoard(board);
    }

}
