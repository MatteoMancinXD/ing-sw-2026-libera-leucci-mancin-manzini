package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.ui;

import java.io.Serializable;

public abstract class ServerToClientMessage implements Serializable {

    public abstract void process(ui userInterface);
    public void onReceive(SocketClient client) {}

}
