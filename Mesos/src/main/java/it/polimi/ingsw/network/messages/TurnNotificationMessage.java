package it.polimi.ingsw.network.messages;

public class TurnNotificationMessage extends ServerToClientMessage{

    private final String currentPlayerNickname;

    public TurnNotificationMessage(String currentPlayerNickname){
        this.currentPlayerNickname=currentPlayerNickname;
    }

    public String getCurrentPlayerNickname(){
        return this.currentPlayerNickname;
    }

    public void process() {
        //userInterface....
    }

}
