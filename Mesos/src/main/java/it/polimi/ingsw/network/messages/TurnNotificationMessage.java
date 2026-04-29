package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.view.ui;
public class TurnNotificationMessage extends ServerToClientMessage{

    private final String currentPlayerNickname;
    private final String gamePhase;

    public TurnNotificationMessage(String currentPlayerNickname,  String gamePhase) {
        this.gamePhase = gamePhase;
        this.currentPlayerNickname=currentPlayerNickname;
    }

    public String getCurrentPlayerNickname(){
        return this.currentPlayerNickname;
    }
    public String getGamePhase(){ return this.gamePhase; }

    public void process(ui userInterface) {
        userInterface.notifyTurn(currentPlayerNickname, gamePhase);
    }

}
