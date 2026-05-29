package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;
import it.polimi.ingsw.view.ui;
import java.util.List;

public class BoardUpdateMessage extends ServerToClientMessage {

    private final BoardSnapshot board;
    private final List<PlayerSnapshot> players;

    public BoardUpdateMessage(BoardSnapshot board, List<PlayerSnapshot> players) {
        this.board = board;
        this.players = players;
    }
    public BoardSnapshot getBoard() {
        return board;
    }

    @Override
    public void process(ui userInterface) {
        userInterface.updateBoard(board, players);
    }
}
