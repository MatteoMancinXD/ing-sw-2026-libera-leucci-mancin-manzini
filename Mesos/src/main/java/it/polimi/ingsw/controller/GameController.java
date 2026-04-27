package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController {
    private int gameID;
    private Game game;
    private Map<String, VirtualView> clients;

    public GameController(int gameID, int numPlayers) {
        this.gameID = gameID;
        game = new Game(numPlayers);
        clients = new HashMap<>();
    }

    public boolean addPlayer(VirtualView view, String nickname) {
        synchronized(this) {
            if (game.getPlayers().size() == game.getNumPlayers()) {
                return false;
            } else if (game.getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickname))) {
                return false;
            } else {
                game.addPlayer(new Player(nickname));
                clients.put(nickname, view);

                if(game.getPlayers().size() == game.getNumPlayers()) {
                    game.startGame();
                    System.out.println("Game " + gameID + " is starting.");
                }
                return true;
            }
        }
    }

    public boolean drawCard(String nickname, boolean row, int idx) {
        if(idx < 0)
            return false;

        if(!checkPlayer(nickname)) return false;

        Player p = game.getCurrentPlayer();
        Board board = game.getBoard();

        Card card = row ? board.getUpperRow().get(idx) : board.getLowerRow().get(idx);
        int foodCost = card.getFoodCost();

        if(foodCost > p.getFood())
            return false;

        game.resolveAction(row, idx);
        new Thread(() -> { broadcastUpdateBoard(); }).start();

        return true;
    }

    public boolean placeTotem(String nickname, int tileIndex) {
        if(!checkPlayer(nickname)) return false;

        Board board = game.getBoard();
        if(tileIndex < 0 || tileIndex > board.getTrack().size() - 1) return false;

        Tile tile = board.getTrack().get(tileIndex);
        if(tile.getStatus()) return false;

        game.placeTotem(tileIndex);

        new Thread(() -> { broadcastUpdateBoard(); }).start();
        return true;
    }

    public void broadcastUpdateBoard() {
        synchronized(this) {
            for(VirtualView view : clients.values()) {
                try {
                    view.updateBoard(game.getBoard());
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean checkPlayer(String nickname) {
        return nickname.equals(game.getCurrentPlayer().getNickname());
    }
}
