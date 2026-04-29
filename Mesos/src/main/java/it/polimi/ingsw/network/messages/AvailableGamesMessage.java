package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.ui;

import java.util.Map;

public class AvailableGamesMessage extends ServerToClientMessage {

    private final Map<Integer, String> games; // gameID -> gameMaster

    public AvailableGamesMessage(Map<Integer, String> games) {
        this.games = games;
    }

    public Map<Integer, String> getGames() { return games; }

    @Override
    public void process(ui userInterface) {
        if (games.isEmpty()) {
            userInterface.showMessage("No available games.");
        } else {
            StringBuilder sb = new StringBuilder("Available games:\n");
            for (Map.Entry<Integer, String> entry : games.entrySet()) {
                sb.append("  Game ID: ").append(entry.getKey())
                        .append(" | Created by: ").append(entry.getValue()).append("\n");
            }
            userInterface.showMessage(sb.toString());
        }
    }
}