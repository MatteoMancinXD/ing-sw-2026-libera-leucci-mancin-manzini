package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClientHandler;

public class JoinGameMessage extends ClientToServerMessage{
    private String nickname;
    private int gameID;

    public JoinGameMessage(String token, String nickname, int gameID) {
        super(token);
        this.nickname = nickname;
        this.gameID = gameID;
    }

    public String getNickname() {
        return nickname;
    }

    public int getGameID() {
        return gameID;
    }

    @Override
    public boolean requiresToken() { return false; }

    @Override
    public void process(SocketClientHandler handler) {
        handler.handleJoinGame(this);
    }
}
