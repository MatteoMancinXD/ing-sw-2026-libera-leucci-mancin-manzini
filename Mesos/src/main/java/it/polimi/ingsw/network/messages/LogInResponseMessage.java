package it.polimi.ingsw.network.messages;

import it.polimi.ingsw.network.socket.SocketClient;

public class LoginResponseMessage extends ServerToClientMessage {

    private final String token;
    private final boolean success;
    private final String errorMessage;

    public LoginResponseMessage(String token) {
        this.token = token;
        this.success = true;
        this.errorMessage = null;
    }

    public LoginResponseMessage(String errorMessage, boolean success) {
        this.token = null;
        this.success = false;
        this.errorMessage = errorMessage;
    }

    public String getToken() { return token; }
    public boolean isSuccess() { return success; }
    public String getErrorMessage() { return errorMessage; }

    @Override
    public void onReceive(SocketClient client) {
        if (success) {
            client.setToken(token);
        }
    }

    @Override
    public void process() {
        // vedi tui
    }
}