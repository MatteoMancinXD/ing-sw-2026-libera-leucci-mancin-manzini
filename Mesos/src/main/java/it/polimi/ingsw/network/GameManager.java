package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameManager {
    Map<Integer, GameController> availableGames;
    Map<Integer, GameController> startedGames;
    Map<String, GameSession> sessions;

    public GameManager(Map<Integer, GameController> availableGames, Map<Integer, GameController> startedGames, Map<String, GameSession> sessions) {
        this.availableGames = availableGames;
        this.sessions = sessions;
        this.startedGames = startedGames;
    }

    public Map<Integer, GameController> getAvailableGames() { return availableGames; }

    public Map<String, GameSession> getSessions() {
        return sessions;
    }

    public Map<Integer, GameController> getStartedGames() {
        return startedGames;
    }
}
