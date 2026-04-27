package it.polimi.ingsw.network.messages;

public class RegularMessage extends ServerToClientMessage{

    private final String message;

    public RegularMessage(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void process() {
        //userInterface....
    }

}
