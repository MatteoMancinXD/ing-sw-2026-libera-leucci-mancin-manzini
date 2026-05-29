package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.view.ui;
public class TurnNotificationMessage extends ServerToClientMessage{

    private final String currentPlayerNickname;
    private final String gamePhase;
    private final int round;
    private final int era;

    public TurnNotificationMessage(String currentPlayerNickname,  String gamePhase, int round, int era) {
        this.gamePhase = gamePhase;
        this.currentPlayerNickname=currentPlayerNickname;
        this.round=round;
        this.era=era;
    }

    public String getCurrentPlayerNickname(){
        return this.currentPlayerNickname;
    }
    public String getGamePhase(){ return this.gamePhase; }

    public void process(ui userInterface) {
        userInterface.notifyTurn(currentPlayerNickname, gamePhase, round, era);
    }

}
