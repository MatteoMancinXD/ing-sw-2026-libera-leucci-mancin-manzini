package it.polimi.ingsw.network.messages;
import it.polimi.ingsw.view.ui;
public class RegularMessage extends ServerToClientMessage{

    private final String message;

    public RegularMessage(String message){
        this.message = message;
    }
    public String getMessage() {
        return message;
    }

    public void process(ui userInterface) {
        System.out.println(message);
    }

}
