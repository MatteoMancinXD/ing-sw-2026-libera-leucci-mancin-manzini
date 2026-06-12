package it.polimi.ingsw.network.db;

import java.io.Serializable;
import java.sql.Date;

/**
 * Immutable Data Transfer Object (DTO) representing a single entry in the game's leaderboard.
 * This bean encapsulates a player's nickname and their total accumulated prestige points
 * for a specific game mode.
 * Being immutable, it ensures thread safety when passed across different layers of the application
 * or sent over the network.
 */
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