package it.polimi.ingsw.network.messages;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.socket.SocketClientHandler;

public class LoginMessage extends ClientToServerMessage {

    public LoginMessage(String nickname) {
        super(nickname);
    }

    @Override
    public void process(GameController gameController, SocketClientHandler handler) {
        handler.handleLogin(this);
    }
}
