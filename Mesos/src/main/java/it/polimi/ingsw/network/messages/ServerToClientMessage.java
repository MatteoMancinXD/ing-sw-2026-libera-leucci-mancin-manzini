package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.network.socket.SocketClient;

import java.io.Serializable;

public abstract class ServerToClientMessage implements Serializable {

    public abstract void process();
    public void onReceive(SocketClient client) {}

}
