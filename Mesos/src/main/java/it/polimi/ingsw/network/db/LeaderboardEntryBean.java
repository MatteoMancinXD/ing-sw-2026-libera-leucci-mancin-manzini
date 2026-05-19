package it.polimi.ingsw.network.db;

import java.io.Serializable;
import java.sql.Date;

public class LeaderboardEntryBean implements Serializable {

    private final String nickname;
    private final int score;

    public LeaderboardEntryBean(String nickname, int score) {
        this.nickname = nickname;
        this.score = score;
    }

    public String getNickname() { return nickname; }
    public int getScore() { return score; }

    @Override
    public String toString() {
        return nickname + " - Prestige Points: " + score;
    }
}