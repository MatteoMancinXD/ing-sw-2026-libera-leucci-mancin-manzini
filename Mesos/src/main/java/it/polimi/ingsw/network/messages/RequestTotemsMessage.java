package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClientHandler;

public class RequestTotemsMessage extends ClientToServerMessage{
    public RequestTotemsMessage(String token) {
        super(token);
    }

    @Override
    public boolean requiresToken() { return true; }

    @Override
    public void process(SocketClientHandler handler) {
        handler.handleRequestTotems(getToken());
    }
}
