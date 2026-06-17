package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.GameStarter;
import it.polimi.ingsw.network.VirtualView;
import it.polimi.ingsw.network.db.DatabaseManagerDAO;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.GameSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;

import java.rmi.RemoteException;
import java.util.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * Server-side controller that manages a single game session.
 * Acts as the intermediary between the {@link it.polimi.ingsw.model.Game} model
 * and the connected clients via {@link VirtualView}.
 * Handles player actions (totem placement, card drawing), enforces turn order,
 * broadcasts game state updates, and manages totem selection before game start.
 * Implements {@link GameObserver} to receive callbacks for event resolution
 * and game end from the model.
 *
 * @see Game
 * @see VirtualView
 * @see GameObserver
 */
public class GameController implements GameObserver {
    private int gameID;
    private String gameMaster;
    private Game game;
    private GameStarter starter;
    private Map<String, VirtualView> clients;
    private Set<Totem> availableTotems;

    //private final Object gameLock = new Object(); //only used in controllerEndTurn (see the method below)

    public GameController(int gameID, String gameMaster, int numPlayers) {
        this.gameID = gameID;
        this.gameMaster = gameMaster;
        this.game = new Game(numPlayers);
        game.addObserver(this);

        availableTotems = new HashSet<>(Arrays.asList(Totem.values()));
        clients = new HashMap<>();
    }

    /**
     * Registers a {@link GameStarter} callback to notify the network layer when
     * the game starts and should be moved from available to started games.
     * @param starter the game starter callback
     */
    public void addStarter(GameStarter starter) {
        this.starter = starter;
    }

    /** @return the set of totem colors not yet chosen by any player */
    public Set<Totem> getAvailableTotems() { return new HashSet<>(availableTotems); }

    /**
     * Handles a player's totem color selection. If all players have selected
     * their totems and the lobby is full, the game starts automatically.
     * @param nickname the nickname of the player selecting the totem
     * @param totem    the totem color to assign
     */
    public void selectTotem(String nickname, Totem totem) {
        synchronized(game) {
            if(!availableTotems.contains(totem)) {
                VirtualView view = clients.get(nickname);
                try {
                    view.showError("Totem not available");
                    return;
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }
            }

            availableTotems.remove(totem);
            game.assignTotem(nickname, totem);

            VirtualView view = clients.get(nickname);
            try {
                view.notifyTotemSelected();
            } catch (RemoteException e) {
                throw new RuntimeException(e);
            }

            boolean everyoneReady = true;
            for(Player p : game.getPlayers()) {
                if (p.getTotem() == null) {
                    everyoneReady = false;
                    break;
                }
            }

            if (game.getPlayers().size() == game.getNumPlayers() && everyoneReady) {
                game.startGame();
                starter.onGameStart(gameID);

                System.out.println("Game #" + gameID + " is starting.");

                new Thread(() -> {
                    broadcastMessage("Game #" + gameID + " starting.");
                }).start();

                Board board = game.getBoard();
                String current = game.getCurrentPlayer().getNickname();

                GameSnapshot snap = game.toSnapshot();
                new Thread(() -> {
                    broadcastUpdate(snap);
                }).start();
            } else {
                broadcastUpdateAvailableTotems(availableTotems);
            }
        }
    }

    /** @return the nickname of the player who created this game */
    public String getGameMaster() {
        return gameMaster;
    }

    /**
     * Adds a player to the game lobby. Notifies all connected clients
     * of the updated player count and sends the available totems to the new player.
     * @param view     the virtual view representing the player's connection
     * @param nickname the player's unique nickname
     * @return true if the player was successfully added, false if the lobby is full
     *         or the nickname is already taken
     */
    public boolean addPlayer(VirtualView view, String nickname) {
        synchronized (game) {
            if (game.getPlayers().size() == game.getNumPlayers()) {
                return false;
            } else if (game.getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickname))) {
                return false;
            } else {
                game.addPlayer(new Player(nickname));
                clients.put(nickname, view);

                Set<Totem> totems = availableTotems;
                try {
                    view.notifyGameParticipation(totems);
                } catch (RemoteException e) {
                    throw new RuntimeException(e);
                }

                int curr = game.getPlayers().size();
                int max = game.getNumPlayers();

                System.out.println(nickname + " joined game #" + gameID);
                String msg = "Current players: " + curr + "/" + max + "...";
                broadcastMessage(msg);
            }
            return true;
        }
    }

    /**
     * Processes a card draw request from a player during the resolution phase.
     * Validates turn order, card index, and food cost before delegating to the model.
     * @param nickname the nickname of the requesting player
     * @param row      true to draw from the upper row, false for the lower row
     * @param idx      the index of the card to draw within the row
     * @return true if the draw was processed, false if rejected
     */
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

            String rowStr = row ? "upper row" : "lower row";
            System.out.println("Player " + nickname + " draws from " + rowStr + "at index " + idx);

            try {
                game.resolveAction(row, idx);

                GameSnapshot snap = game.toSnapshot();
                new Thread(() -> {
                    broadcastUpdate(snap);
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

    /**
     * Processes a totem placement request from a player during the placement phase.
     * Validates turn order, tile index, and tile availability before delegating to the model.
     * @param nickname  the nickname of the requesting player
     * @param tileIndex the index of the tile on the track to place the totem on
     * @return true if the placement was processed, false if rejected
     */
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
            GameSnapshot snap = game.toSnapshot();
            new Thread(() -> {
                broadcastUpdate(snap);
            }).start();

            return true;
        }
    }

    /**
     * Broadcasts the current game state snapshot to all connected clients.
     * Sends both the board update and the turn notification in the same thread
     * to ensure consistency.
     * @param game the current game state snapshot
     */
    public void broadcastUpdate(GameSnapshot game) {
        synchronized (clients) {
            BoardSnapshot board = game.board();
            List<PlayerSnapshot> players = game.players();
            String phase = game.phase().toString();

            int currentIdx = game.currentPlayerIndex();
            String currentNick = players.get(currentIdx).nickname();

            int round = game.round();
            int era = game.era();

            for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                new Thread(() -> {
                    try {
                        entry.getValue().updateBoard(board, players);
                        entry.getValue().notifyTurn(currentNick, phase, round, era);
                    } catch (RemoteException e) {
                        System.out.println("Client "+ entry.getKey() + " unreachable");
                    }
                }).start();
            }
        }
    }

    /**
     * Sends a text message to all connected clients.
     * @param message the message to broadcast
     */
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

    /**
     * Broadcasts a chat message from one player to all connected clients.
     * @param nickname the sender's nickname
     * @param message  the chat message content
     */
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

    /**
     * Sends the updated set of available totems to all connected clients.
     * @param totems the set of still available totem colors
     */
    public void broadcastUpdateAvailableTotems(Set<Totem> totems) {
        synchronized (clients) {
            for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                new Thread(() -> {
                    try {
                        entry.getValue().updateAvailableTotems(totems);
                    } catch (RemoteException e) {
                        System.out.println("Client " + entry.getKey() + " unreachable");
                    }
                }).start();
            }
        }
    }

    /**
     * Checks whether it is currently the specified player's turn.
     * @param nickname the nickname to check
     * @return true if it is this player's turn
     */
    public boolean checkPlayer(String nickname) {
        return nickname.equals(game.getCurrentPlayer().getNickname());
    }

    /*
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
    */

    /**
     * Skips the extra pick phase for a player who declines the bonus draw.
     * @param nickname the nickname of the player skipping
     */    public void skipExtraPick(String nickname) {
        synchronized (game) {
            if (!checkPlayer(nickname)) return;
            game.skipExtraPick();

            GameSnapshot snap = game.toSnapshot();
            new Thread(() -> {
                broadcastUpdate(snap);
                //notifyCurrentPlayer();
            }).start();
        }
    }

    /**
     * Manually ends the current player's turn, advancing to the next player.
     * Used when a player chooses not to draw all available cards.
     * @param nickname the nickname of the player ending their turn
     */
    public void controllerEndTurn(String nickname) {

        synchronized (game) {
            if (!checkPlayer(nickname)) {
                System.err.println("It's not " + nickname + "'s turn, so you can't end it");
                return;
            }

            game.nextTurn();

            //update board
            GameSnapshot snap = game.toSnapshot();
            new Thread(() -> {
                broadcastUpdate(snap);
                //notifyCurrentPlayer();
            }).start();
        }
    }

    /**
     * {@inheritDoc}
     * Notifies all connected clients about the event being resolved.
     */
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

    /**
     * {@inheritDoc}
     * Saves match results to the database, retrieves the global leaderboard,
     * and sends the final rankings and leaderboard to all connected clients.
     */
    @Override
    public void onGameEnd(ArrayList<Player> players) {
        List<String> rankings = new ArrayList<>();
        for (Player p : players) rankings.add(p.getNickname());

        StringBuilder message = new StringBuilder();
        for (int i = 0; i < players.size(); i++) {
            message.append((i + 1) + ". :" + players.get(i).getNickname() + "\n");
        }
        new Thread(() -> {
            broadcastMessage(message.toString());
        }).start();

        new Thread(() -> {
            try {
                DatabaseManagerDAO db = DatabaseManagerDAO.getInstance();
                try {
                    db.saveMatchResults(players, players.size());
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                List<LeaderboardEntryBean> globalLeaderboard =  db.getLeaderboardByPlayerCount(players.size());

                synchronized (clients) {
                    for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                        new Thread(() -> {
                            try {
                                entry.getValue().notifyGameEnd(rankings, globalLeaderboard);
                            } catch (RemoteException e) {
                                System.out.println("Client " + entry.getKey() + " unreachable");
                            }
                        }).start();
                    }
                }
            } catch (Exception e) {
                System.out.println("DB saving error");
                e.printStackTrace();
            }

        }).start();

    }
}
