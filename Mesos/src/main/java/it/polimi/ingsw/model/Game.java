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

    private int currentDrawnUpper;
    private int currentDrawnLower;

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
        ArrayList<BuildingCard> allBuildings = loadBuildingsFromJson();

        this.deck = new TribeDeck(AllCards, numPlayers);
        this.buildingDeck = new BuildingDeck(allBuildings, numPlayers);
        //board.setTribeDeck(this.deck);
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

    private ArrayList<BuildingCard> loadBuildingsFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<BuildingCard> allBuildingsInGame = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/resources/json/buildingsInfo.json");
            TypeReference<Map<String, List<BuildingCard>>> typeRef = new TypeReference<Map<String, List<BuildingCard>>>() {};
            Map<String, List<BuildingCard>> data = mapper.readValue(is, typeRef);

            for (Map.Entry<String, List<BuildingCard>> entry : data.entrySet()) {
                String eraString = entry.getKey(); //Prende chiavi del JSON (era1, era2, era3)
                int eraNumber = Integer.parseInt(eraString.substring(3));
                for(BuildingCard card : entry.getValue()) {
                    card.setEra(eraNumber); //l'era si imposta "manualmente" perchè NON è un parametro nel JSON ma chiavi
                    allBuildingsInGame.add(card);
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
        return allBuildingsInGame;
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
        board.fill(this.numPlayers, this.era, this.deck, this.buildingDeck);       //Round 0 : .fill riempie entrambe le righe e i buildings
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
                b.onGameEnd(p);                         //Attivazione effetti building onGameEnd()
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
                // Spareggio sul prestigio
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

        currentDrawnLower = 0;
        currentDrawnUpper = 0;

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

        for(Player p : this.players) {                              //Attivazione effetti building onRoundEnd()
            List<BuildingCard> buildings = p.getBuildings();
            for(BuildingCard b : buildings) {
                b.onRoundEnd(p);
            }
        }

        board.solveEvents(this.players);

        board.clearLowerRow();
        board.shiftRow();
        if(board.fill(numPlayers, era, this.deck, this.buildingDeck)) {              //Round !=0 : .fill() riempie solo la riga superiore ; Fill Boolean() true = nextEra, false = niente
            nextEra();
        }

        this.round++;
        if (this.round > 10) {
            endGame();
            return;
        }

        this.currentPhase = GamePhase.PLACEMENT;
        this.currentPlayerIndex = 0;
        //logica per riordinare i players in base all'ordine sulla tileboard
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
        board.fillBuildings(era, numPlayers, buildingDeck);
    }

    /**
     * Handles the RESOLUTION game phase.
     * The choosen card is placed in the collection of the current player
     * @param row is true if the card to be drawn is from the upper row, false if not
     * @param index indicates the index of the choosen card from the row (upper or lower)
     * @throws IllegalArgumentException if the player choices are more than the possible choices the Tile offers
    */
    public void resolveAction(boolean row, int index) {         //row = true riga sopra, row = false riga sotto

        Player p = this.players.get(currentPlayerIndex);
        Card c = null;

        Tile targetTile = null;
        for(Tile t : this.board.getTrack()) {
            if (t.getStatus() && t.getPlayer().equals(p)) {
                targetTile = t;
                break;
            }
        }

        if (row && currentDrawnUpper >= targetTile.getUpperRow()) {
            throw new IllegalArgumentException("You already drawn the max number of cards from the upper row");
        }
        if (!row && currentDrawnLower >= targetTile.getLowerRow()) {
            throw new IllegalArgumentException("You already drawn the max number of cards from the lower row");
        }

        if (row) {
            c = this.board.removeUpper(index);
        }
        else{
            c = this.board.removeLower(index);
        }
        p.drawCard(c);
        c.notifyBuildings(p);

        if(currentDrawnLower == targetTile.getLowerRow() && currentDrawnUpper == targetTile.getUpperRow()) {  //nextplayer se il giocatore ha pescato tutte le carte che poteva
            nextPlayer();
        }

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
