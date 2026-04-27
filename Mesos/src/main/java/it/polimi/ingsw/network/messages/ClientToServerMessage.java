package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.socket.SocketClientHandler;

import java.io.Serializable;

public abstract class ClientToServerMessage implements Serializable {

    private final String nickname;
    public ClientToServerMessage(String nickname) {
        this.nickname = nickname;
    }
    public String getNickname() {
        return nickname;
    }

    public abstract void process(GameController gameController, SocketClientHandler handler);

}
