package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    Map<Integer, GameController> lobbies;
    Map<Integer, GameController> startedGames;
    Map<String, GameSession> sessions;

    public GameManager(Map<Integer, GameController> lobbies, Map<Integer, GameController> startedGames, Map<String, GameSession> sessions) {
        this.lobbies = lobbies;
        this.sessions = sessions;
        this.startedGames = startedGames;
    }

    public Map<Integer, GameController> getLobbies() {
        return lobbies;
    }

    public Map<String, GameSession> getSessions() {
        return sessions;
    }

    public Map<Integer, GameController> getStartedGames() {
        return startedGames;
    }
}
