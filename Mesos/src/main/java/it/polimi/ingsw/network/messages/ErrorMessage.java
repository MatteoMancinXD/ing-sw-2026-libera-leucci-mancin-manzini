package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.view.ui;
public class ErrorMessage extends ServerToClientMessage {

    private final String error;

    public ErrorMessage(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void process(ui userInterface) {
        userInterface.showError(error);
    }

}
