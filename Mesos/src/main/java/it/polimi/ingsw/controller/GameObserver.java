package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.model.Player;

import java.util.ArrayList;

/**
 * Observer interface for game events. Implemented by {@link GameController}
 * to receive notifications from the {@link it.polimi.ingsw.model.Game} model
 * when significant events occur during gameplay.
 *
 * @see GameController
 */
public interface GameObserver {
    /**
     * Called when an event card is resolved during the event resolution phase.
     * @param event the event card being resolved
     */
    void onEventResolution(EventCard event);

    /**
     * Called when the game ends, providing the final player rankings.
     * @param winners list of players sorted by final score (highest first)
     */
    void onGameEnd(ArrayList<Player> winners);
}
