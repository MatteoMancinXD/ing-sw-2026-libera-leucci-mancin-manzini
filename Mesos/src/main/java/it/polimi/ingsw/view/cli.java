package it.polimi.ingsw.view;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.network.NetworkClient;
import it.polimi.ingsw.network.db.LeaderboardEntryBean;
import it.polimi.ingsw.network.snapshots.BoardSnapshot;
import it.polimi.ingsw.network.snapshots.CardSnapshot;
import it.polimi.ingsw.network.snapshots.PlayerSnapshot;
import it.polimi.ingsw.network.snapshots.TileSnapshot;

import java.rmi.RemoteException;
import java.sql.SQLOutput;
import java.util.*;
import java.util.stream.Collectors;

public class cli implements ui {

    private final Scanner scanner;
    private NetworkClient client;

    private final String nickname;
    private CliState currentState;

    private List<LeaderboardEntryBean> globalRanks;
    private int numPlayers;

    public cli(String nickname) {
        this.scanner = new Scanner(System.in);
        this.nickname = nickname;
        this.currentState = CliState.LOBBY;
    }

    public void setNetworkClient(NetworkClient client) {
        this.client = client;
    }

    public void startInputStream() {
        System.out.println("Welcome in MESOS! -help for commands list");

        while (true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.isEmpty()) continue;

            String[] parameters = input.split(" ");
            String command = parameters[0];

            switch (currentState) {
                case LOBBY:
                    handleLobbyCommands(command, parameters);
                    break;
                case TOTEM:
                    handleTotemCommands(command, parameters);
                    break;
                case STARTING:
                    System.out.println("You can't do anything.\nWaiting for game to start");
                    break;
                case WAITING:
                    handleWaitingCommands(command, parameters);
                    break;
                case DRAWING:
                    handleDrawingCommands(command, parameters);
                    break;
                case PLACING:
                    handlePlacingCommands(command, parameters);
                    break;
                case END_GAME:
                    handleEndGameCommands(command, parameters);
            }
        }
    }

    private void handleTotemCommands(String command, String[] parameters) {
        switch (command) {
            case "totems":
                client.requestAvailableTotems();
                break;
            case "select":
                String selected = parameters[1].toUpperCase();
                List<String> stringTotems = Arrays.stream(Totem.values()).map(Enum::name).toList();
                if (stringTotems.contains(selected)) {
                    Totem selectedTotem = Totem.valueOf(selected);
                    client.askToSelectTotem(selectedTotem);
                } else {
                    showError("Totem selected not valid.\nChoose either red, yellow, cyan, purple or white");
                }
                break;
            case "help":
                System.out.println("Totem selection commands: totems  |  select *color*  ");
                break;
            default:
                System.out.println("Invalid command");

        }
    }

    public void handleEndGameCommands(String command, String[] parameters) {

        switch (command) {
            case "show_ranks":
                showGlobalRanks();
                break;
            case "help":
                System.out.println("End game commands: show_ranks");
                break;
        }

    }

    public void handleLobbyCommands(String command, String[] parameters) {
        switch (command) {
            case "create":
                try {
                    try {
                        System.out.println("Sending game creation request...");
                        client.createGame(nickname, Integer.parseInt(parameters[1]));
                    } catch (NumberFormatException e) {
                        showError(e.getMessage());
                    }

                } catch (RemoteException e) {
                    System.out.println("Connection error creating game");
                }
                break;
            case "join":
                try {
                    try {
                        System.out.println("Sending join request...");
                        client.joinGame(nickname, Integer.parseInt(parameters[1]));
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid gameID");
                    }

                } catch (RemoteException e) {
                    System.out.println("Connection error joining game");
                }
                break;
            case "list":
                try {
                    client.requestAvailableGames();
                } catch (RemoteException e) {
                    showError(e.getMessage());
                }
                break;
            case "help":
                System.out.println("Lobby commands: create *num_players*  |  join *game_id*  | list ");
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    public void handleWaitingCommands(String command, String[] parameters) {
        switch (command) {
            case "chat":
                StringBuilder message = new StringBuilder();
                //message.append("[CHAT] ");
                for (int i = 1; i < parameters.length; i++) {
                    if (i >= 2) {
                        message.append(" ");
                    }
                    message.append(parameters[i]);
                }
                try {
                    client.sendChatMessage(message.toString());
                } catch (RemoteException e) {
                    showError(e.getMessage());
                }

                System.out.println("Chat message sent");
                break;
            case "help":
                System.out.println("Waiting commands: chat *message*");
                break;
            default:
                System.out.println("Invalid command");
        }
    }

    public void handleDrawingCommands(String command, String[] parameters) {
        switch (command) {
            case "help":
                System.out.println("Drawing commands: draw *row(1:UpperRow, 0:LowerRow)*  *card_index*  |  chat *message*");
                break;
            case "draw":
                boolean row = false;
                if (parameters[1].equals("1")) {
                    row = true;
                }
                try {
                    if (parameters.length == 3) {
                        System.out.println("Sending draw request...");
                        client.askToDrawCard(row, Integer.parseInt(parameters[2]));
                    } else {
                        System.out.println("Use draw <row> <index> command.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid index format");
                }

                break;
            case "chat":
                String message = "";
                for (String word : parameters) {
                    if (!word.equals(parameters[0])) {
                        message += word;
                    }
                }
                try {
                    client.sendChatMessage(message);
                } catch (RemoteException e) {
                    showError(e.getMessage());
                }

                System.out.println("Chat message sent");
                break;
            default:
                System.out.println("Invalid command");

        }
    }

    public void handlePlacingCommands(String command, String[] parameters) {

        switch (command) {
            case "help":
                System.out.println("Placing commands:  totem *tile_index*  |  chat *message*");
                break;
            case "totem":
                try {
                    System.out.println("Sending totem placing request...");
                    client.askToPlaceTotem(Integer.parseInt(parameters[1]));
                    //this.currentState = CliState.WAITING;
                } catch (NumberFormatException e) {
                    System.out.println("Invalid number");
                }
                break;
            case "chat":
                String message = "";
                for (String word : parameters) {
                    if (!word.equals(parameters[0])) {
                        message += word;
                    }
                }
                try {
                    client.sendChatMessage(message);
                } catch (RemoteException e) {
                    showError(e.getMessage());
                }

                System.out.println("Chat message sent");
                break;
            default:
                System.out.println("Invalid command");

        }

    }

    @Override
    public void notifyTurn(String currentPlayerNickname, String currentGamePhase) {
        System.out.println("\n---------------------------------");

        if (currentPlayerNickname.equals(this.nickname)) {
            if (currentGamePhase.equals("PLACEMENT")) {
                this.currentState = CliState.PLACING;
            } else if (currentGamePhase.equals("RESOLUTION") || currentGamePhase.equals("EXTRA_PICK")) {
                this.currentState = CliState.DRAWING;
            }
            System.out.println("It's your turn!");
        } else {
            this.currentState = CliState.WAITING;
            System.out.println("It's " + currentPlayerNickname.toUpperCase() + "'s turn");
        }

        System.out.println("---------------------------------");
        System.out.print("> ");
    }

    @Override
    public void updateBoard(BoardSnapshot board, List<PlayerSnapshot> players) {
        System.out.println("\n======BOARD UPDATE!======\n");

        //TILE TRACK
        for (TileSnapshot t : board.track()) {
            String status = "";
            if (t.status()) {
                String player = t.player().nickname();
                status = "Occupied by " + player;
            } else {
                status = "is free";
            }
            System.out.println("Tile " + t.letter() + ": " + status + ", " + t.upperRow() + " upper and " + t.lowerRow() + " lower");
        }

        System.out.println("\n");

        //UPPER ROW / LOWER ROW
        System.out.println("------UPPER ROW------ \n");
        for (CardSnapshot c : board.upperRow()) {
            System.out.println(c.desc()); //provo a stamparle tutte su una riga (crazy)
        }
        System.out.println("\n------LOWER ROW------\n");
        for (CardSnapshot c : board.lowerRow()) {
            System.out.println(c.desc());
        }

        //System.out.println();

        //STATUS PERSONALE + ALTRI PLAYER
        for (PlayerSnapshot p : players) {
            if (p.nickname().equals(this.nickname)) {
                System.out.println("\n------YOUR STATS AND CARDS------\n");
                System.out.println("Food: " + p.food() + ", Prestige: " + p.prestige() + ", Your cards: \n");
                List<CardSnapshot> everyCard = new ArrayList<>();
                everyCard.addAll(p.artists());
                everyCard.addAll(p.builders());
                everyCard.addAll(p.harvesters());
                everyCard.addAll(p.hunters());
                everyCard.addAll(p.shamans());
                everyCard.addAll(p.inventors());
                everyCard.addAll(p.buildings());
                for (CardSnapshot c : everyCard) {
                    System.out.print(c.desc() + ",  ");
                }
                System.out.println("\n\n");
            }
        }
        System.out.println("------OPPONENTS STATS AND CARDS------\n");
        for (PlayerSnapshot p : players) {      //due cicli diversi per carte personali e stats di altri per printare prima le proprie carte sempre
            if (!p.nickname().equals(this.nickname)) {
                System.out.println("Nickname: " + p.nickname() + ", Food: " + p.food() + ", Prestige: " + p.prestige());
                System.out.println("Artists: " + p.artists().size() + ", Builder: " + p.builders().size() + ", Harvesters: " + p.harvesters().size() + ", Hunters: " + p.hunters().size() + ", Inventors: " + p.inventors().size() + ", Shamans: " + p.shamans().size() + "\n");
            }
        }
        //System.out.println("---------------------------------");
        //System.out.print("> ");

    }

    @Override
    public void showError(String errorMsg) {
        final String ANSI_RED = "\u001B[31m";       //stampa l'errore in rosso, poi resetta a bianco
        final String ANSI_RESET = "\u001B[0m";
        System.out.println("\n" + ANSI_RED + errorMsg + ANSI_RESET);
        System.out.println("---------------------------------");
        System.out.print("> ");
    }

    @Override
    public void notifyEndGame(List<String> rankings, List<LeaderboardEntryBean> globalRanks) {
        this.currentState = CliState.END_GAME;
        this.globalRanks = globalRanks;
        this.numPlayers = rankings.size();
        System.out.println("\n\n======GAME OVER======");
        System.out.println("Standings: ");
        for (int i = 0; i < rankings.size(); i++) {
            System.out.println((i + 1) + ")" + rankings.get(i));
        }
        System.out.println("\nYour position in global ranking: ");
        //Mostra la posizione nella classica totale salvata sul DB del player
        for (int i = 0; i < globalRanks.size(); i++) {
            if (globalRanks.get(i).getNickname().equals(this.nickname)) {
                System.out.println((i + 1) + ")YOU, total prestige points: " + globalRanks.get(i).getScore());
                break;
            }
        }

    }

    @Override
    public void showMessage(String message) {
        System.out.println(message);

        System.out.println("---------------------------------");
        System.out.print("> ");
    }

    @Override
    public void showChatMessage(String sender, String message) {
        System.out.println("[CHAT] " + sender + ": " + message);


    }

    @Override
    public void showAvailableTotems(Set<Totem> totems) {
        System.out.println("Available Totems: ");
        for (Totem totem : totems) {
            System.out.println("  " + totem.toString());
        }

        System.out.println("---------------------------------");
        System.out.print("> ");
    }

    @Override
    public void onTotemSelected() {
        System.out.println("Totem selected succesfully");
        currentState = CliState.STARTING;
    }

    @Override
    public void onGameParticipation() {
        currentState = CliState.TOTEM;

    }

    private void showGlobalRanks() {
        System.out.println("\n\n======GLOBAL RANKINGS FOR " + this.numPlayers + " PLAYERS GAMES======");
        for (int i = 0; i < this.globalRanks.size(); i++) {
            System.out.println((i + 1) + ")" + globalRanks.get(i).getNickname() + " - Total Prestige Points: " + globalRanks.get(i).getScore());
        }

    }
}
