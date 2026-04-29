package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClientHandler;

public class RequestGamesMessage extends ClientToServerMessage{
    public RequestGamesMessage(String token) {
        super(token);
    }

    @Override
    public boolean requiresToken() { return false; }

    @Override
    public void process(SocketClientHandler handler) {
        handler.handleRequestGames(this);
    }
}
