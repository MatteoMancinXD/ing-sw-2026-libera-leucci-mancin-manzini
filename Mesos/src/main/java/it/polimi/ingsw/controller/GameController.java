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
    private String gameMaster;
    private Game game;
    private Map<String, VirtualView> clients;

    public GameController(int gameID, String gameMaster, int numPlayers) {
        this.gameID = gameID;
        this.gameMaster = gameMaster;
        game = new Game(numPlayers);
        clients = new HashMap<>();
    }

    public String getGameMaster() {
        return gameMaster;
    }

    public boolean addPlayer(VirtualView view, String nickname) {
        synchronized (game) {
            if (game.getPlayers().size() == game.getNumPlayers()) {
                return false;
            } else if (game.getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickname))) {
                return false;
            } else {
                game.addPlayer(new Player(nickname));
                clients.put(nickname, view);

                int curr = game.getPlayers().size();
                int max = game.getNumPlayers();

                System.out.println(nickname + " joined game #" + gameID);
                String msg = "Current players: " + curr + "/" + max + "...";
                broadcastMessage(msg);

                if (game.getPlayers().size() == game.getNumPlayers()) {
                    game.startGame();
                    System.out.println("Game #" + gameID + " is starting.");

                    broadcastMessage("Game #" + gameID + " starting.");

                    Board board = game.getBoard();

                    new Thread(() -> {
                        broadcastUpdateBoard(board);
                        notifyCurrentPlayer();
                    }).start();
                }
            }
            return true;
        }
    }

    public boolean drawCard(String nickname, boolean row, int idx) {
        synchronized (game) {
            if (idx < 0)
                return false;

            if (!checkPlayer(nickname)) return false;

            Player p = game.getCurrentPlayer();
            Board board = game.getBoard();

            Card card = row ? board.getUpperRow().get(idx) : board.getLowerRow().get(idx);
            int foodCost = card.getFoodCost();

            if (foodCost > p.getFood())
                return false;
            try {
                game.resolveAction(row, idx);
                new Thread(() -> {
                    broadcastUpdateBoard(board);
                    notifyCurrentPlayer();
                }).start();
            } catch (IllegalStateException e) {
                VirtualView view = clients.get(nickname);
                try {
                    view.showError(e.getMessage());
                } catch (RemoteException re) {
                    //giocatore disconnesso
                }

            }
            return true;
        }
    }

    public boolean placeTotem(String nickname, int tileIndex) {
        synchronized (game) {
            if (!checkPlayer(nickname)) return false;

            Board board = game.getBoard();
            if (tileIndex < 0 || tileIndex > board.getTrack().size() - 1) return false;

            Tile tile = board.getTrack().get(tileIndex);
            if (tile.getStatus()) return false;

            game.placeTotem(tileIndex);
            new Thread(() -> {
                broadcastUpdateBoard(board);
                notifyCurrentPlayer();
            }).start();

            return true;
        }
    }

    public void broadcastUpdateBoard(Board board) {
        synchronized (clients) {
            for (VirtualView view : clients.values()) {
                new Thread(() -> {
                    try {
                        view.updateBoard(board);
                    } catch (RemoteException e) {
                        System.out.println("Client unreachable.");
                    }
                }).start();
            }
        }
    }

    public void broadcastMessage(String message) {
        synchronized (clients) {
            for (VirtualView view : clients.values()) {
                new Thread(() -> {
                    try {
                        view.showMessage(message);
                    } catch (RemoteException e) {
                        System.out.println("Client unreachable.");
                    }
                }).start();
            }
        }
    }

    public boolean checkPlayer(String nickname) {
        return nickname.equals(game.getCurrentPlayer().getNickname());
    }

    public void notifyCurrentPlayer() {
        synchronized (clients) {
            if (game.getRound() > 10) return;
            String current = game.getCurrentPlayer().getNickname();
            for (VirtualView view : clients.values()) {
                new Thread(() -> {
                    try {
                        view.notifyTurn(current);
                    } catch (RemoteException e) {
                        System.out.println("Client unreachable.");
                    }
                }).start();
            }
            if (game.getCurrentPhase() == GamePhase.EXTRA_PICK) {
                new Thread(() -> {
                    try {
                        clients.get(current).askBonusExtraPick();
                    } catch (RemoteException e) {
                        System.out.println("Client unreachable.");
                    }
                }).start();
            }
        }
    }

    public void skipExtraPick(String nickname) {
        synchronized (game) {
            if (!checkPlayer(nickname)) return;
            game.skipExtraPick();
            Board board = game.getBoard();

            new Thread(() -> {
                broadcastUpdateBoard(board);
                notifyCurrentPlayer();
            }).start();
        }
    }
}
