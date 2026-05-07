package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

public interface GameObserver {
    void onEventResolution(EventCard event);

    void onGameEnd(ArrayList<Player> winners);
}
