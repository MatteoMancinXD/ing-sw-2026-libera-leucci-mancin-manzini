package it.polimi.ingsw.network.socket;

import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.network.GameManager;
import it.polimi.ingsw.network.messages.*;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Map;
import java.util.UUID;

public class SocketClientHandler implements Runnable{

    private final Socket socket;
    //private final GameManager mngr;
    private final GameController gameController;

    private ObjectInputStream in;
    private ObjectOutputStream out;
    private String nickname;
    private String token;

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
                //message.process(this);
                if (message.requiresToken() && !validateToken(message.getToken())) {
                    sendMessage(new ErrorMessage("Token non valid"));
                    continue;
                }
                message.process(this);
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Player " + nickname + " disconnected.");
        } finally {
            closeConnection();
        }
    }

    public String handleLogin(LoginMessage msg) {
        this.nickname = msg.getNickname();
        int gameId = msg.getGameId();
        int numPlayers = msg.getNumPlayers();



        //Map<Integer, GameController> lobbies = mngr.getLobbies();

        VirtualSocketView view = new VirtualSocketView(nickname, out);
        boolean success= gameController.addPlayer(view, nickname);
        if (success) {
            this.token = UUID.randomUUID().toString();
            sendMessage(new LoginResponseMessage(token));
        } else {
            sendMessage(new LoginResponseMessage("Nickname already taken", false));
        }

       /* if(lobbies.containsKey(gameId)){
            success = lobbies.get(gameId).addPlayer(view, nickname);
            System.out.println("Player " + nickname + " added to lobby " + gameId + " through socket.");
        } else {
            lobbies.put(gameId, new GameController(numPlayers));
            success = lobbies.get(gameId).addPlayer(view, nickname);
            System.out.println("Player " + nickname + " created lobby " + gameId + " through socket.");
        }

        if (!success) {
           //messaggio errore
        }

        return UUID.randomUUID().toString();
    } */

    public void handleDrawCard(DrawCardMessage msg) {
      //  String token = msg.getToken();

        gameController.drawCard(msg.getUpperRow(), msg.getIndex());
    }

    public void handlePlaceTotem(PlaceTotemMessage msg) {
            gameController.placeTotem(msg.getPos());
        }

    public void handleSkip(SkipBonusMessage msg) {}


    private boolean validateToken(String receivedToken) {
            return this.token != null && this.token.equals(receivedToken);
        }

    private void sendMessage(ServerToClientMessage message) {
            try {
                out.writeObject(message);
                out.flush();
                out.reset();
            } catch (IOException e) {
                System.err.println("Error sending a message to " + nickname);
            }
        }


    private void closeConnection() {
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
