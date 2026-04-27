package it.polimi.ingsw.network.messages;


import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.socket.SocketClientHandler;

public class SkipBonusMessage extends ClientToServerMessage{

    public SkipBonusMessage(String nickname) {
        super(nickname);
    }

    @Override
    public void process(GameController controller, SocketClientHandler handler) {
        controller.skipBonusPick();
    }
}
