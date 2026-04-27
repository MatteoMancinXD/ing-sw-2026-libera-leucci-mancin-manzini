package it.polimi.ingsw.network;

public class GameSession {
    int gameID;
    String nickname;

    public GameSession(int gameID, String nickname) {
        this.gameID = gameID;
        this.nickname = nickname;
    }

    public int getGameID() {
        return gameID;
    }

    public String getNickname() {
        return nickname;
    }
}
