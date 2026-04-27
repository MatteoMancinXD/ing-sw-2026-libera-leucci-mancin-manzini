package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.messages.ClientToServerMessage;
import it.polimi.ingsw.network.messages.DrawCardMessage;
import it.polimi.ingsw.network.messages.LoginMessage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SocketClientHandler implements Runnable{

    private final Socket socket;
    private final GameController gameController;
    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nickname;

    public SocketClientHandler(Socket socket, GameController gameController) {
        this.socket = socket;
        this.gameController = gameController;
    }

    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                ClientToServerMessage message = (ClientToServerMessage) in.readObject();
                message.process(gameController, this);
            }

        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Player " + nickname + " disconnected.");
        } finally {
            closeConnection();
        }
    }

    public void handleLogin(LoginMessage msg) {
        this.nickname = msg.getNickname();

        VirtualSocketView view = new VirtualSocketView(nickname, out);

        boolean success = gameController.addPlayer(view, nickname);

        if (!success) {
           //messaggio errore
        }
    }

    private void handleDrawCard(DrawCardMessage msg) {
        gameController.drawCard(msg.getNickname(), msg.getUpperRow(), msg.getIndex());
    }

    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
