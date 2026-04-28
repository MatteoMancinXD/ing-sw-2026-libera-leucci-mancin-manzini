package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.socket.SocketClientHandler;

import java.io.Serializable;
import java.util.Map;

public abstract class ClientToServerMessage implements Serializable {

    private final String token;
    public ClientToServerMessage(String token) {
        this.token = token;
    }
    public String getToken() {
        return token;
    }
    public boolean requiresToken() { return true; }

    public abstract void process(SocketClientHandler handler);
}
