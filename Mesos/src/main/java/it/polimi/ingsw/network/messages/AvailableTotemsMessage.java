package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.view.ui;

import java.util.Set;

public class AvailableTotemsMessage extends ServerToClientMessage {
    Set<Totem> totems;

    public AvailableTotemsMessage(Set<Totem> totems) {
        this.totems = totems;
    }

    @Override
    public void process(ui userInterface) {
        userInterface.showAvailableTotems(totems);
    }
}
