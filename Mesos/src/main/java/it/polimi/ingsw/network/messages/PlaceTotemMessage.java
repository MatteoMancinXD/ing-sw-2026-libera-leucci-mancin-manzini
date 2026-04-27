package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.socket.SocketClientHandler;

public abstract class PlaceTotemMessage extends ClientToServerMessage {

    private final int pos;

    public PlaceTotemMessage(String token, int pos) {
        super(token);
        this.pos = pos;
    }
    public int getPos() {
        return pos;
    }


    @Override
    public void process(SocketClientHandler handler) {
        handler.handlePlaceTotem(this);
    }

}

