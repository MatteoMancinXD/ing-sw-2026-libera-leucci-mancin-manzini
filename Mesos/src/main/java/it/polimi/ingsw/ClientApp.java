package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.network.messages.LoginMessage;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;

import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        ClientController ui = new ClientController();

        Scanner kb = new Scanner(System.in);

        System.out.println("Type your nickname: ");
        String nickname = kb.nextLine();

        System.out.println("Select communication protocol:");
        System.out.println("1. RMI");
        System.out.println("2. Socket");
        int protocol = kb.nextInt();

        System.out.println("1. Create a new game");
        System.out.println("2. Join a game");
        int choice = kb.nextInt();

        String token = "";

        if(protocol == 1){
            RmiClient client = new RmiClient(ui, nickname);
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
            int gameID = 0;
            client.sendMessageToServer(new LoginMessage(nickname, gameID, 2));
        }
}
}
