package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.model.Totem;
import it.polimi.ingsw.view.ui;

import java.util.Set;

public class GameParticipationMessage extends ServerToClientMessage {
    private Set<Totem> totems;

    public GameParticipationMessage(Set<Totem> totems) {
        this.totems = totems;
    }

    @Override
    public void process(ui userInterface) {
        userInterface.onGameParticipation(totems);
    }
}
