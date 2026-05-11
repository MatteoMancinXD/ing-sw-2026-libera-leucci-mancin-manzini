package it.polimi.ingsw.view;

import it.polimi.ingsw.model.Board;
import it.polimi.ingsw.model.Card;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.Tile;
import it.polimi.ingsw.network.NetworkClient;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class cli implements ui{

    private final Scanner scanner;
    private NetworkClient client;

    private final String nickname;
    private CliState currentState;

    public cli (String nickname) {
        this.scanner = new Scanner(System.in);
        this.nickname = nickname;
        this.currentState = CliState.LOBBY;
    }

    public void setNetworkClient(NetworkClient client) {
        this.client = client;
    }

    public void startInputStream() {
        System.out.println("Welcome in MESOS! -help for commands list");

        while(true) {
            System.out.print("\n> ");
            String input = scanner.nextLine().trim().toLowerCase();
            if (input.isEmpty()) continue;

            String[] parameters = input.split(" ");
            String command = parameters[0];

            switch(currentState) {
                case LOBBY:
                    handleLobbyCommands(command, parameters);
                    break;
                case  WAITING:
                    handleWaitingCommands(command, parameters);
                    break;
                case DRAWING:
                    handleDrawingCommands(command, parameters);
                    break;
                case PLACING:
                    handlePlacingCommands(command, parameters);
            }
        }
    }

    public void handleLobbyCommands(String command, String[] parameters) {
           switch(command) {
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

               case "help":
                   System.out.println("Lobby commands: create *num_players*  |  join *game_id*  | list ");
                   break;
               default:
                   System.out.println("Invalid command");
           }
    }

    public void handleWaitingCommands(String command, String[] parameters) {
        switch(command) {
            case "chat":
                StringBuilder message = new StringBuilder();
                //message.append("[CHAT] ");
                for(int i = 1; i < parameters.length; i++) {
                    if(i >= 2) {
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
        switch(command) {
            case "help":
                System.out.println("Drawing commands: draw *row(1:UpperRow, 0:LowerRow)*  *card_index*  |  chat *message*");
                break;
            case "draw":
                boolean row = false;
                if (parameters[1].equals("1")) {
                    row = true;
                }
                try {
                    if(parameters.length == 3) {
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

        switch(command) {
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
            }
            else if (currentGamePhase.equals("RESOLUTION") || currentGamePhase.equals("EXTRA_PICK")) {
                this.currentState = CliState.DRAWING;
            }
            System.out.println("It's your turn!");
        } else {
            this.currentState = CliState.WAITING;
            System.out.println("It's " + currentPlayerNickname.toUpperCase()+"'s turn");
        }

        System.out.println("---------------------------------");
        System.out.print("> ");
    }

    @Override
    public void updateBoard(Board board, List<Player> players) {
        System.out.println("\n======BOARD UPDATE!======\n");

        //TILE TRACK
        for (Tile t : board.getTrack()) {
            String status = "";
            if (t.getStatus()) {
                String player = t.getPlayer().getNickname();
                status = "Occupied by " + player;
            }
            else {
                status = "is free";
            }
            System.out.println("Tile " + t.getLetter() + ": "+status+", " + t.getUpperRow() + " upper and " + t.getLowerRow() + " lower");
        }

        System.out.println("\n");

        //UPPER ROW / LOWER ROW
        System.out.println("------UPPER ROW------ \n");
        for (Card c : board.getUpperRow()) {
            System.out.println(c.getShortString()); //provo a stamparle tutte su una riga (crazy)
        }
        System.out.println("\n------LOWER ROW------\n");
        for (Card c : board.getLowerRow()) {
            System.out.println(c.getShortString());
        }

        //System.out.println();

        //STATUS PERSONALE + ALTRI PLAYER
        for (Player p : players) {
            if (p.getNickname().equals(this.nickname)) {
                System.out.println("\n------YOUR STATS AND CARDS------\n");
                System.out.println("Food: "+p.getFood()+", Prestige: "+p.getPrestige()+", Your cards: \n");
                List<Card> everyCard = new ArrayList<>();
                everyCard.addAll(p.getArtists());
                everyCard.addAll(p.getBuilders());
                everyCard.addAll(p.getHarvesters());
                everyCard.addAll(p.getHunters());
                everyCard.addAll(p.getShamans());
                everyCard.addAll(p.getInventors());
                everyCard.addAll(p.getBuildings());
                for (Card c : everyCard) {
                    System.out.print(c.getShortString()+",  ");
                }
                System.out.println("\n\n");
            }
        }
        System.out.println("------OPPONENTS STATS AND CARDS------\n");
        for (Player p : players) {      //due cicli diversi per carte personali e stats di altri per printare prima le proprie carte sempre
            if (!p.getNickname().equals(this.nickname)) {
                System.out.println("Nickname: "+p.getNickname()+", Food: "+p.getFood()+", Prestige: "+p.getPrestige());
                System.out.println("Artists: "+p.getArtists().size() +", Builder: "+p.getBuilders().size()+", Harvesters: "+p.getHarvesters().size()+", Hunters: "+p.getHunters().size()+", Inventors: "+p.getInventors().size()+", Shamans: "+p.getShamans().size()+"\n");
            }
        }
        //System.out.println("---------------------------------");
        //System.out.print("> ");

    }

    @Override
    public void showError(String errorMsg) {
        final String ANSI_RED = "\u001B[31m";       //stampa l'errore in rosso, poi resetta a bianco
        final String ANSI_RESET = "\u001B[0m";
        System.out.println("\n"+ANSI_RED+errorMsg+ANSI_RESET);
        System.out.println("---------------------------------");
        System.out.print("> ");
    }

    @Override
    public void notifyEndGame(List<String> rankings) {
        System.out.println("\n\n======GAME OVER======");
        System.out.println("Standings: ");
        for(int i = 0; i < rankings.size(); i++) {
            System.out.println((i+1)+")"+rankings.get(i));
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

        System.out.println("---------------------------------");
        System.out.print("> ");
    }

}
