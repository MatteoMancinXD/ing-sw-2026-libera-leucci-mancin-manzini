package it.polimi.ingsw.controller;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.GameStarter;
import it.polimi.ingsw.network.VirtualView;

import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;


public class GameController {
    private int gameID;
    private String gameMaster;
    private Game game;
    private GameStarter starter;
    private Map<String, VirtualView> clients;
    private Set<String> disconnectedPlayers;
    private ScheduledExecutorService pingScheduler;


    private static final int TIMEOUT_SECONDS = 60;
    private ScheduledExecutorService scheduler;
    private ScheduledFuture<?> timeoutTask;

    //private final Object gameLock = new Object(); //only used in controllerEndTurn (see the method below)

    public GameController(int gameID, String gameMaster, int numPlayers, GameStarter starter) {
        this.gameID = gameID;
        this.gameMaster = gameMaster;
        this.game = new Game(numPlayers);
        this.starter = starter;
        this.clients = new HashMap<>();
        this.disconnectedPlayers = new HashSet<>();
        this.scheduler = Executors.newSingleThreadScheduledExecutor();
        this.pingScheduler = Executors.newSingleThreadScheduledExecutor();

    }

    public String getGameMaster() {
        return gameMaster;
    }

    public boolean addPlayer(VirtualView view, String nickname) {
           synchronized (game) {
            if (game.getPlayers().size() == game.getNumPlayers()) return false;
            if (game.getPlayers().stream().anyMatch(p -> p.getNickname().equals(nickname))) return false;

            game.addPlayer(new Player(nickname));
            clients.put(nickname, view);

            int curr = game.getPlayers().size();
            int max = game.getNumPlayers();

            System.out.println(nickname + " joined game #" + gameID);
            broadcastMessage("Current players: " + curr + "/" + max + "...");

            if (game.getPlayers().size() == game.getNumPlayers()) {
                game.startGame();
                starter.onGameStart(gameID);
                startPingThread();
                System.out.println("Game #" + gameID + " is starting.");

                new Thread(() -> broadcastMessage("Game #" + gameID + " starting.")).start();

                Board board = game.getBoard();
                String current = game.getCurrentPlayer().getNickname();
                new Thread(() -> broadcastUpdateBoard(board, game.getPlayers(), current, game.getCurrentPhase())).start();
            }
            return true; }

    }

    public boolean drawCard(String nickname, boolean row, int idx) {
            synchronized (game) {
                if (idx < 0) return false;
                if (!checkPlayer(nickname)) return false;

                Player p = game.getCurrentPlayer();
                Board board = game.getBoard();
                Card card = row ? board.getUpperRow().get(idx) : board.getLowerRow().get(idx);
                if (card.getFoodCost() > p.getFood()) return false;

                try {
                    game.resolveAction(row, idx);
                    System.out.println("DEBUG drawCard - after resolveAction, currentPlayer: " + game.getCurrentPlayer().getNickname() + ", phase: " + game.getCurrentPhase());
                    new Thread(() -> broadcastUpdateBoard(board, game.getPlayers(), game.getCurrentPlayer().getNickname(), game.getCurrentPhase())).start();
                } catch (IllegalStateException e) {
                    VirtualView view = clients.get(nickname);
                    try {
                        view.showError(e.getMessage());
                    } catch (RemoteException re) {
                        handleDisconnection(nickname);
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
                new Thread(() -> broadcastUpdateBoard(board, game.getPlayers(), game.getCurrentPlayer().getNickname(), game.getCurrentPhase())).start();
    return true; }}

    public void broadcastUpdateBoard(Board board, List<Player> players, String current, String phase)
        {
            List<String> toDisconnect = new ArrayList<>();
            synchronized (clients) {
                for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                    if (disconnectedPlayers.contains(entry.getKey())) continue;
                    try {
                        entry.getValue().updateBoard(board, players);
                        entry.getValue().notifyTurn(current, phase);
                    } catch (RemoteException e) {
                        System.out.println("Client " + entry.getKey() + " unreachable");
                        toDisconnect.add(entry.getKey());
                    }
                }
            }
            for (String n : toDisconnect) handleDisconnection(n);
    }

    public void broadcastMessage(String message) {
        System.out.println("DEBUG broadcastMessage: " + message + " to " + clients.size() + " clients, disconnected: " + disconnectedPlayers);
        List<String> toDisconnect = new ArrayList<>();
                synchronized (clients) {
                    for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                        if (disconnectedPlayers.contains(entry.getKey())) continue;
                        try {
                            entry.getValue().showMessage(message);
                        } catch (RemoteException e) {
                            System.out.println("Client " + entry.getKey() + " unreachable");
                            toDisconnect.add(entry.getKey());
                        }
                    }
                }
                for (String n : toDisconnect) handleDisconnection(n);
        }

    public void broadcastChatMessage(String nickname, String message) {
                synchronized (clients) {
                    for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                        try {
                            entry.getValue().showMessage("[CHAT]" + nickname + ": " + message);
                        } catch (RemoteException e) {
                            System.out.println("Client " + entry.getKey() + " unreachable");
                        }
                    }
                }
    }

    public boolean checkPlayer(String nickname) {
        return nickname.equals(game.getCurrentPlayer().getNickname());
    }

    private void broadcastGameEnd(List<String> rankings) {
                synchronized (clients) {
                    for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                        if (disconnectedPlayers.contains(entry.getKey())) continue;
                        try {
                            entry.getValue().notifyGameEnd(rankings);
                        } catch (RemoteException e) {
                            System.out.println("Client " + entry.getKey() + " unreachable");
                        }
                    }
                }
    }

    public void notifyCurrentPlayer() {
                synchronized (clients) {
                    if (game.getRound() > 10) return;
                    String current = game.getCurrentPlayer().getNickname();
                    List<String> toDisconnect = new ArrayList<>();
                    for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                        if (disconnectedPlayers.contains(entry.getKey())) continue;
                        try {
                            entry.getValue().notifyTurn(current, game.getCurrentPhase());
                        } catch (RemoteException e) {
                            toDisconnect.add(entry.getKey());
                        }
                    }
                    for (String n : toDisconnect) handleDisconnection(n);

                    if (game.getCurrentPhase().equals("EXTRA_PICK")) {
                        try {
                            clients.get(current).askBonusExtraPick();
                        } catch (RemoteException e) {
                            handleDisconnection(current);
                        }
                    }
                }
    }

    //invoked if the player decides not to use the "extra pick" bonus
    public void skipExtraPick(String nickname) {
                synchronized (game) {
                    if (!checkPlayer(nickname)) return;
                    game.skipExtraPick();
                    Board board = game.getBoard();
                    new Thread(() -> broadcastUpdateBoard(board, game.getPlayers(), game.getCurrentPlayer().getNickname(), game.getCurrentPhase())).start();
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
                    Board board = game.getBoard();
                    new Thread(() -> broadcastUpdateBoard(board, game.getPlayers(), game.getCurrentPlayer().getNickname(), game.getCurrentPhase())).start();
                }
    }
    public boolean reconnect(String nickname, VirtualView view) {
                synchronized (game) {
                    if (!disconnectedPlayers.contains(nickname)) return false;
                    game.addReconnectingPlayer(nickname);
                    disconnectedPlayers.remove(nickname); // rimuovi dai disconnessi subito per annullare il timeout
                    clients.put(nickname, view);
                    System.out.println(nickname + " reconnected to game #" + gameID);
                    broadcastMessage(nickname + " has reconnected.");
                    cancelTimeout();
                    Board board = game.getBoard();
                    String current = game.getCurrentPlayer().getNickname();
                    new Thread(() -> broadcastUpdateBoard(board, game.getPlayers(), current, game.getCurrentPhase())).start();
                    return true;
        }
    }

    public boolean isPlayerDisconnected(String nickname) {
        return disconnectedPlayers.contains(nickname);
    }

    public boolean isGameInProgress() {
        return game.getPlayers().size() == game.getNumPlayers();
    }

    public void handleDisconnection(String nickname) {
                synchronized (game) {
                    if (!clients.containsKey(nickname)) return;
                    if (disconnectedPlayers.contains(nickname)) return; // già disconnesso

                    disconnectedPlayers.add(nickname);
                    System.out.println(nickname + " disconnected from game #" + gameID);
                    broadcastMessage(nickname + " has disconnected. Their turns will be skipped.");

                    long connectedCount = clients.keySet().stream()
                            .filter(n -> !disconnectedPlayers.contains(n))
                            .count();

                    if (connectedCount <= 1) startTimeout();

                    game.setDisconnectedPlayers(disconnectedPlayers);
                    if (game.getCurrentPlayer().getNickname().equals(nickname)) {
                        game.nextPlayer();
                        Board board = game.getBoard();
                        String current = game.getCurrentPlayer().getNickname();
                        new Thread(() -> broadcastUpdateBoard(board, game.getPlayers(), current, game.getCurrentPhase())).start();
                    }
                }
    }

    private void startTimeout() {
                cancelTimeout();
                broadcastMessage("Only one player connected. Game will end in " + TIMEOUT_SECONDS + " seconds if no one reconnects.");
                timeoutTask = scheduler.schedule(() -> {
                    synchronized (game) {
                        String winner = clients.keySet().stream()
                                .filter(n -> !disconnectedPlayers.contains(n))
                                .findFirst()
                                .orElse(null);
                        if (winner != null) {
                            broadcastMessage("Timeout! " + winner + " wins by default.");
                            List<String> rankings = new ArrayList<>();
                            rankings.add(winner);
                            rankings.addAll(disconnectedPlayers);
                            broadcastGameEnd(rankings);
                        }
                    }
                }, TIMEOUT_SECONDS, TimeUnit.SECONDS);
    }

    private void cancelTimeout() {
        if (timeoutTask != null && !timeoutTask.isDone()) {
            timeoutTask.cancel(false);
            timeoutTask = null;
        }
    }
    public Set<String> getDisconnectedPlayers() { return disconnectedPlayers; }
    private void startPingThread() {
        pingScheduler.scheduleAtFixedRate(() -> {
            synchronized (clients) {
                List<String> toDisconnect = new ArrayList<>();
                for (Map.Entry<String, VirtualView> entry : clients.entrySet()) {
                    if (disconnectedPlayers.contains(entry.getKey())) continue;
                    try {
                        entry.getValue().ping();
                    } catch (RemoteException e) {
                        toDisconnect.add(entry.getKey());
                    }
                }
                for (String n : toDisconnect) handleDisconnection(n);
            }
        }, 5, 5, TimeUnit.SECONDS);
    }
}
