package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.view.ui;

import java.util.List;

public class EndGameMessage extends ServerToClientMessage{

    private final List<String> rankings;
    private final List<LeaderboardEntryBean> globalRanks;

    public EndGameMessage(List<String> rankings,  List<LeaderboardEntryBean> globalRanks) {
        this.rankings = rankings;
        this.globalRanks = globalRanks;
    }

    public List<String> getRankings() {
        return rankings;
    }

    public void process(ui userInterface) {
        userInterface.notifyEndGame(rankings, globalRanks);
    }

}
