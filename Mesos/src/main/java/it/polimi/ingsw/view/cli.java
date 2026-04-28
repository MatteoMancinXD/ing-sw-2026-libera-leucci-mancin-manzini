package it.polimi.ingsw.view;

import it.polimi.ingsw.network.NetworkClient;

import java.util.Scanner;

public class cli implements ui{

    private final Scanner scanner;
    private NetworkClient client;s

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
                   client.createGame(nickname, Integer.parseInt(parameters[1]));
                   System.out.println("Game creation request sent");
                   break;
               case "join":
                   client.joinGame(nickname, Integer.parseInt(parameters[1]));
                   break;
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
                String message = null;
                for (String word : parameters) {
                    if (!word.equals(parameters[0])) {
                        message += word;
                    }
                }
                client.sendChatMessage(message);
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
                client.askToDrawCard(row, Integer.parseInt(parameters[2]));
                System.out.println("Draw request sent");
                break;
            case "chat":
                String message = null;
                for (String word : parameters) {
                    if (!word.equals(parameters[0])) {
                        message += word;
                    }
                }
                client.sendChatMessage(message);
                System.out.println("Chat message sent");
                break;
            default:
                System.out.println("Invalid command");

        }
    }

    public void handlePlacingCommands(String command, String[] parameters) {

        switch(command) {
            case "help":
                System.out.println("Placing commands:  totem *tile_index*  |  end  |  chat *message*");
                break;
            case "totem":
                client.askToPlaceTotem(Integer.parseInt(parameters[1]));
                System.out.println("Totem placing request sent");
                this.currentState = CliState.WAITING;
                break;
            case "chat":
                String message = null;
                for (String word : parameters) {
                    if (!word.equals(parameters[0])) {
                        message += word;
                    }
                }
                client.sendChatMessage(message);
                System.out.println("Chat message sent");
                break;
            case "end":         //Se il giocatore non vuole pescare tutte le carte che potrebbe
                client.askToEndTurn();
                break;
            default:
                System.out.println("Invalid command");

        }

    }

    @Override
    public void notifyTurn(String currentPlayerNickname, String currentGamePhase) {
        System.out.println("\n---------------------------------");

        if (currentPlayerNickname.equals(this.nickname)) {
            if (currentGamePhase.equals("PLACING")) {
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

}
