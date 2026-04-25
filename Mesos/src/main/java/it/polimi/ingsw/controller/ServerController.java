package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.rmi.VirtualRMIView;

import java.util.List;

public class ServerController {

    private Game game;
    List<VirtualRMIView> connectedClients;

    public ServerController(List<VirtualRMIView> clients) {
        this.connectedClients = clients;
        this.game = new Game(clients.size());
    }

}
