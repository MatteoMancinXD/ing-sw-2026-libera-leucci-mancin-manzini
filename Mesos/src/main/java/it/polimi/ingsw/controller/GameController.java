package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.network.VirtualView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {

    private Game game;
    private Map<String, VirtualView> clients;

    public GameController(int numPlayers) {
        game = new Game(numPlayers);
        clients = new HashMap<>();
    }

    public boolean addPlayer(VirtualView view, String nickname) {
        synchronized(this) {
            if (game.getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickname))) {
                return false;
            } else {
                game.addPlayer(new Player(nickname));
                clients.put(nickname, view);
                return true;
            }
        }
    }

    public boolean drawCard(boolean row, int idx) {
        if(idx < 0)
            return false;

        Player p = game.getCurrentPlayer();
        Board board = game.getBoard();

        Card card = row ? board.getUpperRow().get(idx) : board.getLowerRow().get(idx);
        int foodCost = card.getFoodCost();

        if(foodCost > p.getFood())
            return false;

        p.drawCard(card);
        return true;
    }
}
