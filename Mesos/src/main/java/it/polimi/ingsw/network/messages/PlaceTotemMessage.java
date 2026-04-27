package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.socket.SocketClientHandler;

public class PlaceTotemMessage extends ClientToServerMessage {

    private final int pos;

    public PlaceTotemMessage(String nickname, int pos) {
        super(nickname);
        this.pos = pos;
    }
    public int getPos() {
        return pos;
    }


    @Override
    public void process(SocketClientHandler handler) {
        //controller.placeTotem(getNickname(), pos);
    }

}

