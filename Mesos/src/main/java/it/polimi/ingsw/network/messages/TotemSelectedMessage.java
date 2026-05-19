package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.ui;

public class TotemSelectedMessage extends ServerToClientMessage {
    @Override
    public void process(ui userInterface) {
        userInterface.onTotemSelected();
    }
}
