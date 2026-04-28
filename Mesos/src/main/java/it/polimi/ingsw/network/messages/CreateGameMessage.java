package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClientHandler;

public class CreateGameMessage extends ClientToServerMessage{
    private String nickname;
    private int numPlayers;

    public CreateGameMessage(String token, String nickname, int numPlayers) {
        super(token);
        this.nickname = nickname;
        this.numPlayers = numPlayers;
    }

    public String getNickname() {
        return nickname;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public void process(SocketClientHandler handler) {
        handler.handleCreateGame(this);
    }

    @Override
    public boolean requiresToken() { return false; }
}
