package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.EventCard;

public interface GameObserver {
    void onEventResolution(EventCard event);
}
