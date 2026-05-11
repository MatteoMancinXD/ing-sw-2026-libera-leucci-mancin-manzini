package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.characters.BuilderCard;
import it.polimi.ingsw.network.GameStarter;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameController implements GameObserver {
    private int gameID;
    private String gameMaster;
    private Game game;
    private GameStarter starter;
    private Map<String, VirtualView> clients;

    //private final Object gameLock = new Object(); //only used in controllerEndTurn (see the method below)

    public GameController(int gameID, String gameMaster, int numPlayers, GameStarter starter) {
        this.gameID = gameID;
        this.gameMaster = gameMaster;
        this.game = new Game(numPlayers, this);
        this.starter = starter;

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
                    starter.onGameStart(gameID);

                    System.out.println("Game #" + gameID + " is starting.");

                    new Thread(() -> {
                        broadcastMessage("Game #" + gameID + " starting.");
                    }).start();

                    Board board = game.getBoard();
                    String current = game.getCurrentPlayer().getNickname();


                    new Thread(() -> {
                        broadcastUpdateBoard(board, game.getPlayers(), current, game.getCurrentPhase());
                    }).start();
                }
            }
            return true;
        }
    }

    public boolean drawCard(String nickname, boolean row, int idx) {
        synchronized (game) {
            Card card = null;
            if (idx < 0)
                return false;

            if (!checkPlayer(nickname)) return false;

            Player p = game.getCurrentPlayer();
            Board board = game.getBoard();

            try {
                card = row ? board.getUpperRow().get(idx) : board.getLowerRow().get(idx);
            } catch (IndexOutOfBoundsException e) {
                VirtualView view = clients.get(p.getNickname());
                try {
                    view.showError("Index out of bounds! ");
                    return false;
                } catch (RemoteException e1) {
                    e1.printStackTrace();
                }

            }

            int foodCost = card.getFoodCost();
            int discount = p.getTotDiscount();

            if (foodCost > (p.getFood() + discount)){
                VirtualView view = clients.get(p.getNickname());
                try {
                    view.showError("You don't have enough food to buy that card!");
                    return false;
                } catch (RemoteException e) {
                    //giocatore disconnesso
                }
            }


            try {
                game.resolveAction(row, idx);
                new Thread(() -> {
                    broadcastUpdateBoard(board, game.getPlayers(), game.getCurrentPlayer().getNickname(), game.getCurrentPhase());
                    //notifyCurrentPlayer();
                }).start();
            } catch (IllegalStateException | IllegalArgumentException e) {
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
            if (tileIndex < 0 || tileIndex > board.getTrack().size() - 1) {
                VirtualView view = clients.get(nickname);
                try {
                    view.showError("Index out of bound");
                    return false;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            Tile tile = board.getTrack().get(tileIndex);
            if (tile.getStatus()) {
                VirtualView view = clients.get(nickname);
                try {
                    view.showError("Tile already occupied by player " + tile.getPlayer().getNickname());
                    return false;
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }

            game.placeTotem(tileIndex);
            new Thread(() -> {
                broadcastUpdateBoard(board, game.getPlayers(), game.getCurrentPlayer().getNickname(), game.getCurrentPhase());
            }).start();

            return true;
        }
    }

    public void broadcastUpdateBoard(Board board, List<Player> players, String current, String phase) {
        synchronized (clients) {
            for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                new Thread(() -> {
                    try {
                        entry.getValue().updateBoard(board, players);
                        entry.getValue().notifyTurn(current, phase);
                    } catch (RemoteException e) {
                        System.out.println("Client "+ entry.getKey() + " unreachable");
                    }
                }).start();
            }
        }
    }

    public void broadcastMessage(String message) {
        synchronized (clients) {
            for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                new Thread(() -> {
                    try {
                        entry.getValue().showMessage(message);
                    } catch (RemoteException e) {
                        System.out.println("Client " + entry.getKey() + " unreachable");
                    }
                }).start();
            }
        }
    }

    public void broadcastChatMessage(String nickname, String message) {
        synchronized (clients) {
            for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                new Thread(() -> {
                    try {
                        entry.getValue().showChatMessage(nickname, message);
                    } catch (RemoteException e) {
                        System.out.println("Client " + entry.getKey() + " unreachable");
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
                        view.notifyTurn(current, game.getCurrentPhase());
                    } catch (RemoteException e) {
                        System.out.println("Client unreachable.");
                    }
                }).start();
            }
            if (game.getCurrentPhase().equals("EXTRA_PICK")) {
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

    //invoked if the player decides not to use the "extra pick" bonus
    public void skipExtraPick(String nickname) {
        synchronized (game) {
            if (!checkPlayer(nickname)) return;
            game.skipExtraPick();
            Board board = game.getBoard();

            new Thread(() -> {
                broadcastUpdateBoard(board, game.getPlayers(), game.getCurrentPlayer().getNickname(), game.getCurrentPhase());
                //notifyCurrentPlayer();
            }).start();
        }
    }

    //invoked only if currentPlayer refuses to draw all the cards he is able to
    public void controllerEndTurn(String nickname) {

        synchronized (game) {
            if (!checkPlayer(nickname)) {
                System.err.println("It's not " + nickname + "'s turn, so you can't end it");
                return;
            }

            game.nextTurn();

            //update board
            Board board = game.getBoard();
            new Thread(() -> {
                broadcastUpdateBoard(board, game.getPlayers(), game.getCurrentPlayer().getNickname(), game.getCurrentPhase());
                //notifyCurrentPlayer();
            }).start();
        }
    }

    @Override
    public void onEventResolution(EventCard event) {
        String desc = event.getShortString();
        System.out.println("Solving: " + desc);

        synchronized (clients) {
            for(VirtualView view : clients.values()) {
                try {
                    view.eventResolution(event);
                } catch (RemoteException e) {
                    System.out.println("Client unreachable");
                    throw new RuntimeException(e);
                }
            }
        }
    }

    @Override
    public void onGameEnd(ArrayList<Player> winners) {
        List<String> rankings = new ArrayList<>();
        for (Player p : winners) rankings.add(p.getNickname());

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < winners.size(); i++) {
            message.append((i + 1) + ". :" + winners.get(i).getNickname() + "\n");
        }
        new Thread(() -> {
            broadcastMessage(message.toString());
        }).start();

        synchronized (clients) {
            for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                new Thread(() -> {
                    try {
                        entry.getValue().notifyGameEnd(rankings);
                    } catch (RemoteException e) {
                        System.out.println("Client " + entry.getKey() + " unreachable");
                    }
                }).start();
            }
        }
    }
}
