package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.EventCard;
import it.polimi.ingsw.view.ui;

public class EventResolutionMessage extends ServerToClientMessage{
    EventCard card;

    public EventResolutionMessage(EventCard card) {
        this.card = card;
    }

    @Override
    public void process(ui userInterface) {
        userInterface.showMessage("Event resolved: " + card.getShortString());
    }
}
