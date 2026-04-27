package it.polimi.ingsw.network.messages;

public class ErrorMessage extends ServerToClientMessage {

    private final String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void process() {
        //userInterface....
    }

}
