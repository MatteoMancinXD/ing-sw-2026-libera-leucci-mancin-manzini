package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.network.messages.CreateGameMessage;
import it.polimi.ingsw.network.messages.JoinGameMessage;
import it.polimi.ingsw.network.messages.LoginMessage;
import it.polimi.ingsw.network.messages.RequestGamesMessage;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;

import it.polimi.ingsw.view.*;

import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        ClientController ctrl = new ClientController();

        Scanner kb = new Scanner(System.in);

        System.out.println("Type your nickname: ");
        String nickname = kb.nextLine();

        ui ui = new cli(nickname);


        System.out.println("Select communication protocol:");
        System.out.println("1. RMI");
        System.out.println("2. Socket");
        int protocol = kb.nextInt();

        System.out.println("1. Create a new game");
        System.out.println("2. Join a game");
        int choice = kb.nextInt();

        String token = "";

        if(protocol == 1){
            RmiClient client = new RmiClient(ctrl, ui, nickname);
            client.startConnection("127.0.0.1", 1099);

            if(choice == 1) {
                client.createGame(nickname, 2);
            } else if (choice == 2) {
                client.requestAvailableGames();
                System.out.println("Select game to join: ");
                int id =  kb.nextInt();
                client.joinGame(nickname, id);
            }
        } else if(protocol == 2){
            SocketClient client = new SocketClient(nickname);
            client.startConnection("127.0.0.1", 5000);

            if(choice == 1) {
                client.sendMessageToServer(new CreateGameMessage(token, nickname, 2));
            } else if (choice == 2) {
                client.sendMessageToServer(new RequestGamesMessage(token));
                System.out.println("Select game to join: ");
                int id =  kb.nextInt();
                client.sendMessageToServer(new JoinGameMessage(token, nickname, id));
            }
        }
    }
}
