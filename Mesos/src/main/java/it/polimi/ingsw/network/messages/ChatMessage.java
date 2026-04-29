package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClientHandler;
import it.polimi.ingsw.view.ui;

public class ChatMessage extends ClientToServerMessage{

    private final String message;

    public ChatMessage(String token, String message){
        super(token);
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void process(SocketClientHandler handler) {

    }

}
