package it.polimi.ingsw.model;
import it.polimi.ingsw.model.characters.BuilderCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

/**
 * Represents the main class of the Model, orchestrating the entire game flow.
 * Manages the game state, player turns, phases, eras, and board interaction.
 *
 * @author Matteo Mancin
 */
public class Game {

    private int round;
    private int era;
    private int numPlayers;
    private int currentPlayerIndex;
    private GamePhase currentPhase;
    private ArrayList<Player> players;
    private Board board;
    private TribeDeck deck;
    private BuildingDeck buildingDeck;

    /**
     * Constructor for the Game class.
     * Initializes the starting parameters of the game, creating the board and the decks.
     *
     * @param numPlayers The number of players participating in the game (between 2 and 5).
     */
    public Game(int numPlayers) {
        this.round = 0;
        this.era = 1;
        this.numPlayers = numPlayers;
        this.currentPlayerIndex = 0;
        this.players = new ArrayList<>();
        this.board = new Board(numPlayers);     //La board viene inizializzata in base al numero di players

        List<TribeCard> AllCards = loadCardsFromJson();

        this.deck = new TribeDeck(AllCards, numPlayers);
        this.buildingDeck = new BuildingDeck();
        board.setTribeDeck(this.deck);
    }


    public void setRound(int round){
        this.round = round;
    }
    public int getRound() {
        return this.round;
    }
    public void setEra(int era) {
        this.era = era;
    }
    public int getEra() {
        return era;
    }

    private List<TribeCard> loadCardsFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        List<TribeCard> allCardsInGame = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/resources/json/cardsInfo.json");
            TypeReference<Map<String, List<TribeCard>>> typeRef = new TypeReference<Map<String, List<TribeCard>>>() {};
            Map<String, List<TribeCard>> data = mapper.readValue(is, typeRef);

            for (Map.Entry<String, List<TribeCard>> entry : data.entrySet()) {
                String eraString = entry.getKey(); //Prende chiavi del JSON (era1, era2, era3)
                int eraNumber = Integer.parseInt(eraString.substring(3));
                for(TribeCard card : entry.getValue()) {
                    card.setEra(eraNumber); //l'era si imposta "manualmente" perchè NON è un parametro nel JSON
                    allCardsInGame.add(card);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return allCardsInGame;
    }

    /**
     * Sets the number of players, ensuring it is a valid amount.
     *
     * @param n The number of players to set.
     * @throws IllegalArgumentException If the number of players is not between 2 and 5.
     */
    public void setNumPlayers(int n) throws IllegalArgumentException {
        if (n>5 || n<2) {
            throw new IllegalArgumentException("Invalid number of players! Max = 5");
        }
        numPlayers = n;
    }
    public int getNumPlayers() {
        return numPlayers;
    }

    // return the list of players inside game
    public List<Player> getPlayers() {
        return players;
    }
    
    /**
     * Adds a new player to the game, if the maximum limit has not been reached.
     *
     * @param p The Player object to add.
     */
    public void addPlayer(Player p) {
        if (this.players.size() < this.numPlayers) {
            this.players.add(p);
        }
    }
    /**
     * Starts the game by preparing the initial board, shuffling the player order,
     * and assigning starting food rations based on the turn order.
     */
    public void startGame() {
        board.fill(this.numPlayers, this.era);       //Round 0 : .fill riempie entrambe le righe e i buildings
        Collections.shuffle(players);        //Shuffle dei player per avere un ordine casuale all'inizio
        for (int i = 0; i < this.players.size(); i++) {
            Player p = this.players.get(i);
            if(i==0) {                  //Primo giocatore prende 2 cibo
                p.editFood(2);
            }
            else if (i == 1 || i == 2) { //Secondo e terzo giocatore prendono 3 cibo
                p.editFood(3);
            }
            else {                       //Quarto e quinto giocatore prendono 4 cibo
                p.editFood(4);
            }
        }
        this.currentPhase = GamePhase.PLACEMENT;
        this.currentPlayerIndex = 0;
        this.round = 1;
    }
    /**
     * Calculates the final scores of the players and determines the winner(s).
     * In case of a tie in prestige, the player with the most food wins.
     *
     * @return A list containing the winning player(s).
     */
    public ArrayList<Player> endGame() {
        Player winner =  players.getFirst();
        ArrayList<Player> winners = new ArrayList<>();
        winners.add(winner);

        for (Player p: this.players) {                  //Aggiunta di prestigio finale per buildings e builder
            for(BuildingCard b : p.getBuildings()) {
                p.editPrestige(b.getBuildingPrestigeGain());
            }
            List<BuilderCard> builders = p.getCharacterDeck(Character.BUILDER);
            for (BuilderCard b : builders) {
                p.editPrestige(b.getPps());
            }
        }

        for (int i = 1; i < players.size(); i++) {
            Player p = players.get(i);

            if (p.getPrestige() > winner.getPrestige()) {
                // Nuovo record assoluto di prestigio
                winner = p;
                winners.clear();
                winners.add(winner);
            }
            else if (p.getPrestige() == winner.getPrestige()) {
                // Spareggio sul prestigio! Controlliamo il cibo
                if (p.getFood() > winner.getFood()) {
                    winner = p;
                    winners.clear();
                    winners.add(winner);
                }
                else if (p.getFood() == winner.getFood()) {
                    // Parità totale: vittoria condivisa
                    winners.add(p);
                }
            }
        }
        winners.add(winner);
        return winners;
    }
    /**
     * Advances the turn to the next player.
     * Automatically handles the phase change (from PLACEMENT to RESOLUTION) or
     * advances to the next round if all players have completed their actions.
     */
    public void nextPlayer(){
        currentPlayerIndex++;

        if(currentPlayerIndex >= players.size()) {      //Fine player
            if(currentPhase == GamePhase.PLACEMENT) {   //Si passa dalla fase di piazzamento alla risoluzione
                currentPhase = GamePhase.RESOLUTION;
                currentPlayerIndex = 0;
            }
            else if( currentPhase == GamePhase.RESOLUTION) {
                nextTurn();
            }
        }
    }
    /**
     * Handles the end of the entire round.
     * Resolves events, restores the board, advances the round counter (or ends the game),
     * and reorders the players based on the totems placed on the turn order tile.
     */
    public void nextTurn() {
        board.solveEvents(this.players);

        board.clearLowerRow();
        board.shiftRow();
        if(board.fill(numPlayers, era)) {              //Round !=0 : .fill() riempie solo la riga superiore ; Fill Boolean() true = nextEra, false = niente
            nextEra();
        }

        this.round++;
        if (this.round > 10) {
            endGame();
            return;
        }

        this.currentPhase = GamePhase.PLACEMENT;
        this.currentPlayerIndex = 0;
        //logica per riordinare i players in base all'ordine sulla tileboard: ipotizzo che in Tile sia salvato il player
        ArrayList<Player> nextTurnOrder = new ArrayList<>();
        for(Tile tile: board.getTrack()) {
            if (tile.getStatus()) {
                nextTurnOrder.add(tile.getPlayer());
            }
        }
        this.players = nextTurnOrder;
    }
    /**
     * Handles the transition to the next era.
     * Discards remaining buildings and fills the board with new ones from the current era.
     */
    public void nextEra() {
        this.era++;
        board.shiftBuildings();
        board.fillBuildings(era);
    }

    /**
     * Handles the RESOLUTION game phase.
     * Each of the choosen cards are placed into the player cards and removed from the board.
     * @param cardChoice indexes of the choosen cards by the Player
     * @throws IllegalArgumentException if the player choices are more than the possible choices the Tile offers
    */
    public void resolveAction(int[] cardChoice) {

        //cardChoice è una array di int tipo [1, 3, 2]
        //I numeri rappresentano la posizione delle carte PRIMA della upper row e DOPO della lower row

        Player p = this.players.get(currentPlayerIndex);
        List<Tile> track = this.board.getTrack();
        Tile targetTile = null;

        for(Tile t : track) {
            if (t.getStatus() && t.getPlayer().equals(p)) {
                targetTile = t;
                break;
            }
        }

        if (cardChoice.length > targetTile.getUpperRow() + targetTile.getLowerRow()) {
            throw new IllegalArgumentException("Too many choices for this tile");
        }

        int i = 0, j = 0;
        while (i < targetTile.getUpperRow()) {
            p.drawCard(this.board.removeUpper(cardChoice[i]));
            i++;
        }
        while (j < targetTile.getLowerRow()) {
            p.drawCard(this.board.removeLower(cardChoice[i]));
            j++;
            i++;
        }
        nextPlayer();
    }


    /**
     * Allows the current player to place their Totem on a specific tile.
     *
     * @param pos The index of the tile on which to place the Totem.
     * @throws IllegalArgumentException If the chosen tile is already occupied by another player.
     */
    public void placeTotem(int pos) throws IllegalArgumentException{     //si presuppone che pos sia un numero sempre legale (compreso tra 0 e tiles-1)
        if (board.getTrack().get(pos).getStatus()) {
            throw new IllegalArgumentException("Position already taken by another player! Please choose a free tile");
        }
        Player p = players.get(currentPlayerIndex);
        board.getTrack().get(pos).place(p);             //place imposta lo Status della Tile a True e salva il player
        nextPlayer();
    }

}
