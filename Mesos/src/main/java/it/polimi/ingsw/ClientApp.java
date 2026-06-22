package it.polimi.ingsw;

import it.polimi.ingsw.network.rmi.RmiClient;
import it.polimi.ingsw.network.socket.SocketClient;
import it.polimi.ingsw.view.gui.GuiManager;
import it.polimi.ingsw.view.gui.LobbyView;
import it.polimi.ingsw.view.cli;
import javafx.application.Application;


import java.util.Scanner;

public class ClientApp {
    public static void main(String[] args) throws Exception {
        Scanner kb = new Scanner(System.in);

        System.out.println("Select interface:");
        System.out.println("1. CLI");
        System.out.println("2. GUI");
        int interfaceChoice = Integer.parseInt(kb.nextLine().trim());

        if (interfaceChoice == 2) {
            // JavaFX prende il controllo del thread principale
            Application.launch(GuiManager.class, args);
            return;
        }

        System.out.println("Type your nickname: ");
        String nickname = kb.nextLine();

        System.out.println("Select communication protocol:");
        System.out.println("1. RMI");
        System.out.println("2. Socket");
        int protocol = kb.nextInt();
        kb.nextLine();

        System.out.println("Insert IPv4 address of server:");
        String address = kb.nextLine();

        cli cliView = new cli(nickname);
        //System.setProperty("java.rmi.server.hostname", "192.168.1.236");


        if(protocol == 1){
            RmiClient client = new RmiClient(cliView, nickname);
            cliView.setNetworkClient(client);
            client.startConnection(address, 1099);
        } else if (protocol == 2) {
            SocketClient client = new SocketClient(nickname);
            client.setUserInterface(cliView);
            cliView.setNetworkClient(client);
            client.startConnection(address, 5000);
        }

        //new Thread(cliView::startInputStream).start();
        cliView.startInputStream();
    }
}
