package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClientHandler;

public class RequestTotemsMessage extends ClientToServerMessage{
    public RequestTotemsMessage(String token) {
        super(token);
    }

    @Override
    public void process(SocketClientHandler handler) {
        handler.handleRequestTotems(this);
    }
}
