package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.network.rmi.VirtualRMIView;

import java.util.List;

public class GameController {

    private Game game;
    List<VirtualRMIView> connectedClients;

    public GameController(List<VirtualRMIView> clients) {
        this.connectedClients = clients;
        this.game = new Game(clients.size());
    }

}
