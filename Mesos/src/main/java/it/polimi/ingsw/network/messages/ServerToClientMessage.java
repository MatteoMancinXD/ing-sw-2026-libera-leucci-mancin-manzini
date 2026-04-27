package it.polimi.ingsw.network.messages;

import java.io.Serializable;
import it.polimi.ingsw.network.socket.SocketClient;


public abstract class ServerToClientMessage implements Serializable {
    public void onReceive(SocketClient client) {}

    public abstract void process();

}
