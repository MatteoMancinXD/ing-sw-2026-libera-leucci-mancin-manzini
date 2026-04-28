package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.view.ui;
public class TurnNotificationMessage extends ServerToClientMessage{

    private final String currentPlayerNickname;

    public TurnNotificationMessage(String currentPlayerNickname){
        this.currentPlayerNickname=currentPlayerNickname;
    }

    public String getCurrentPlayerNickname(){
        return this.currentPlayerNickname;
    }

    public void process(ui userInterface) {
        //userInterface....
    }

}
