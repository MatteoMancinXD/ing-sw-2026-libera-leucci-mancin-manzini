package it.polimi.ingsw.network;

import it.polimi.ingsw.controller.GameController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class GameManager implements GameStarter {
    Map<Integer, GameController> availableGames;
    Map<Integer, GameController> startedGames;
    Map<String, GameSession> sessions;
    AtomicInteger idCounter;

    public GameManager(Map<Integer, GameController> availableGames, Map<Integer, GameController> startedGames, Map<String, GameSession> sessions) {
        this.availableGames = availableGames;
        this.sessions = sessions;
        this.startedGames = startedGames;

        idCounter = new AtomicInteger(0);
    }

    public Map<Integer, GameController> getAvailableGames() { return availableGames; }

    public Map<String, GameSession> getSessions() {
        return sessions;
    }

    public Map<Integer, GameController> getStartedGames() {
        return startedGames;
    }

    public int getIdCounter() { return idCounter.incrementAndGet(); }

    public synchronized Map<Integer, String> getGamesIDAndMaster() {
        Map<Integer, String> games = new HashMap<>();
        for(Map.Entry<Integer, GameController> game : availableGames.entrySet()) {
            games.put(game.getKey(), game.getValue().getGameMaster());
        }

        return games;
    }

    @Override
    public synchronized void onGameStart(int gameID) {
        if(availableGames.containsKey(gameID)) {
            GameController ctrl = availableGames.remove(gameID);
            startedGames.put(gameID, ctrl);
        }
    }
}
