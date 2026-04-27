package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Board;

public class BoardUpdateMessage extends ServerToClientMessage {

    private final Board board;

    public BoardUpdateMessage(Board board) {
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
