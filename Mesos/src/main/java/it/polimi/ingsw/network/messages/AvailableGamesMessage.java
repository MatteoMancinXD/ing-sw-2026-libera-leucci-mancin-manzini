package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.ui;

import java.util.Map;

public class AvailableGamesMessage extends ServerToClientMessage{
    Map<Integer, String> games;

    public AvailableGamesMessage(Map<Integer, String> games) {
        this.games = games;
    }

    @Override
    public void process(ui userInterface) {
        for(Map.Entry<Integer, String> game : games.entrySet()){
            System.out.println(game.getKey() + ": " + game.getValue());
        };
    }
}
