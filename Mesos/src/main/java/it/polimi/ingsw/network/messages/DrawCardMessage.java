package it.polimi.ingsw.network.messages;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.socket.SocketClientHandler;

public class DrawCardMessage extends ClientToServerMessage{

    private final boolean upperRow;
    private final int index;

    public DrawCardMessage(String token, boolean upperRow, int index) {
        super(token);
        this.upperRow = upperRow;
        this.index = index;
    }

    public boolean getUpperRow() {
        return this.upperRow;
    }
    public int getIndex() {
        return this.index;
    }


    @Override
    public void process(SocketClientHandler handler) {
        handler.handleDrawCard(this);
    }

}
