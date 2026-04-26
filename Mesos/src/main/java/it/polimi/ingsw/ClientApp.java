package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.network.rmi.RmiClient;

import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        ClientController ui = new ClientController();

        Scanner kb = new Scanner(System.in);

        System.out.println("Type your nickname: ");
        String nickname = kb.nextLine();

        System.out.println("Type the gameID: ");
        int gameID = kb.nextInt();

        RmiClient client = new RmiClient(ui, nickname);

        client.startConnection("127.0.0.1", 1099, gameID, 2);
    }
}
