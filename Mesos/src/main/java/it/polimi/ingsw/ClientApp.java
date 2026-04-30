package it.polimi.ingsw;

import it.polimi.ingsw.controller.ClientController;
import it.polimi.ingsw.network.messages.CreateGameMessage;
import it.polimi.ingsw.network.messages.LoginMessage;
import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.cli;


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
        kb.nextLine();

        cli cliView = new cli(nickname);



        if(protocol == 1){
            RmiClient client = new RmiClient(cliView, nickname);
            cliView.setNetworkClient(client);
            client.startConnection("127.0.0.1", 1099);
        } else if (protocol == 2) {
            SocketClient client = new SocketClient(nickname);
            client.setUserInterface(cliView);
            cliView.setNetworkClient(client);
            client.startConnection("127.0.0.1", 5000);
        }

        //new Thread(cliView::startInputStream).start();
        cliView.startInputStream();
    }
}
