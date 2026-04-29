package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.view.ui;

import java.util.List;

public class EndGameMessage extends ServerToClientMessage{

    private final List<String> rankings;

    public EndGameMessage(List<String> rankings){
        this.rankings = rankings;
    }

    public List<String> getRankings() {
        return rankings;
    }

    public void process(ui userInterface) {
        userInterface.notifyEndGame(rankings);
    }

}
