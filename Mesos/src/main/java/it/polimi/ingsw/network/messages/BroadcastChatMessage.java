package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.view.ui;

public class BroadcastChatMessage extends ServerToClientMessage {
    private String sender;
    private String message;

    public BroadcastChatMessage(String sender, String message) {
        this.sender = sender;
        this.message = message;
    }

    @Override
    public void process(ui userInterface) {
        userInterface.showChatMessage(sender, message);
    }
}
