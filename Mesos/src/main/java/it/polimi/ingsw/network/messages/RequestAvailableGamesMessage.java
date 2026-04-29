package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClientHandler;

public class RequestAvailableGamesMessage extends ClientToServerMessage {

    public RequestAvailableGamesMessage(String token) {
        super(token);
    }

    @Override
    public void process(SocketClientHandler handler) {
        handler.handleRequestAvailableGames(this);
    }

    @Override
    public boolean requiresToken() { return false; }
}
