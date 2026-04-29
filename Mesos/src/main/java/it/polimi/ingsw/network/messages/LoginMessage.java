package it.polimi.ingsw.network.messages;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.socket.SocketClientHandler;

import java.util.Map;

public class LoginMessage extends ClientToServerMessage {
    private String nickname;
    private int gameId;
    private int numPlayers;

    public int getGameId() {
        return gameId;
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public String getNickname() {
        return nickname;
    }

    public LoginMessage(String nickname, int gameId, int numPlayers) {
        super(null);
        this.nickname = nickname;
        this.gameId = gameId;
        this.numPlayers = numPlayers;
    }

    @Override
    public void process(SocketClientHandler handler) {
        //handler.handleLogin(this);
    }

    @Override
    public boolean requiresToken() {
        return false;
    }
}