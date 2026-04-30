package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.network.socket.SocketClientHandler;
import it.polimi.ingsw.view.ui;
public class TokenResponseMessage extends ServerToClientMessage{

    private final String token;

    public TokenResponseMessage(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }

    @Override
    public void onReceive(SocketClient client) {
        client.setToken(this.token);
    }

    @Override
    public void process(ui userInterface) {
        //nada
    }

}
