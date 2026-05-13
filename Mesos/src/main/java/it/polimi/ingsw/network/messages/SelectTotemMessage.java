package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.network.socket.SocketClientHandler;

public class SelectTotemMessage extends ClientToServerMessage {
    Totem totem;

    public SelectTotemMessage(String token, Totem totem) {
        super(token);
        this.totem = totem;
    }

    @Override
    public void process(SocketClientHandler handler) {
        handler.handleSelectTotems(this);
    }

    public Totem getTotem() { return totem; }
}
