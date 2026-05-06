package it.polimi.ingsw.model;
import it.polimi.ingsw.controller.GameObserver;
import it.polimi.ingsw.model.buildings.ExtraPickBuilding;
import it.polimi.ingsw.model.characters.BuilderCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
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
    private ArrayList<Player> players;

    private GamePhase currentPhase;

    private Board board;
    private TribeDeck deck;
    private BuildingDeck buildingDeck;

    private int currentDrawnUpper;
    private int currentDrawnLower;

    GameObserver observer;

    /**
     * Constructor for the Game class.
     * Initializes the starting parameters of the game, creating the board and the decks.
     *
     * @param numPlayers The number of players participating in the game (between 2 and 5).
     */
    public Game(int numPlayers, GameObserver observer) {
        this.round = 0;
        this.era = 1;
        this.numPlayers = numPlayers;
        this.currentPlayerIndex = 0;
        this.players = new ArrayList<>();
        this.board = new Board(numPlayers);     //La board viene inizializzata in base al numero di players

        List<TribeCard> AllCards = loadCardsFromJson();
        ArrayList<BuildingCard> allBuildings = loadBuildingsFromJson();

        this.deck = new TribeDeck(AllCards, numPlayers);
        deck.shuffle();

        this.buildingDeck = new BuildingDeck(allBuildings, numPlayers);
        //board.setTribeDeck(this.deck);

        this.observer = observer;
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
    public String getCurrentPhase() {  return currentPhase.toString(); }
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex);}

    private ArrayList<BuildingCard> loadBuildingsFromJson() {
        ObjectMapper mapper = new ObjectMapper();
        ArrayList<BuildingCard> allBuildingsInGame = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/json/buildingsInfo.json");
            if (is == null){throw new RuntimeException("IMPOSSIBILE TROVARE IL FILE JSON BUILDING");}
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
            InputStream is = getClass().getResourceAsStream("/json/cardsInfo.json");
            if (is == null) {throw new RuntimeException("IMPOSSIBILE TROVARE IL FILE JSON CARDS");}
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

    public Board getBoard() {return board; }
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
                p.editPrestige(b.getPrestigeGain());
                b.onGameEnd(p);                         //Attivazione effetti building onGameEnd()
            }
            List<BuilderCard> builders = p.getBuilders();
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
                recalculateDrawOrder();
                currentPlayerIndex = 0;

                // If there are 5 player, tile A only adds food, so we manually skip that player's turn
                if(numPlayers == 5 && board.getTrack().getFirst().getStatus()) {
                    currentPlayerIndex++;
                }
            }
            else if( currentPhase == GamePhase.RESOLUTION) {
                Player bonusPlayer = checkExtraPickBuilding();
                if (bonusPlayer != null) {
                    currentPhase = GamePhase.EXTRA_PICK;
                    currentPlayerIndex = players.indexOf(bonusPlayer);          //Impostiamo "a mano" il player index al player che ha il building extra pick
                }
                else {
                    nextTurn();
                }
            }
            else if (currentPhase == GamePhase.EXTRA_PICK) {
                nextTurn();
            }
        }
    }

    public void recalculateDrawOrder() {
        ArrayList<Player> drawOrder = new ArrayList<>();
        for(Tile tile: board.getTrack()) {
            if (tile.getStatus()) {
                Player p = tile.getPlayer();
                drawOrder.add(p);
            }
        }
        this.players = drawOrder;
    }

    private Player checkExtraPickBuilding() {
        for(Player p: players) {
            for(BuildingCard b : p.getBuildings()) {
                if (b.grantsExtraPick()) {
                    return p;
                }
            }
        }
        return null;
    }

    public void skipExtraPick() throws IllegalStateException {  //Metodo per gestire il caso in cui l'utente decide di NON prendere la carta extra
        if(this.currentPhase != GamePhase.EXTRA_PICK) {
            throw new IllegalStateException();
        }
        currentPlayerIndex = players.size();
        nextPlayer();
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

        board.solveEvents(this.players, observer);

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
                Player p = tile.getPlayer();
                for(BuildingCard b : p.getBuildings()) {
                    b.onOrderTilePlacement(p, nextTurnOrder.size(), board.getOrder());
                }
                nextTurnOrder.add(p);
            }
        }
        board.resetTrackTiles();
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
     * @throws IllegalStateException if the player choices are more than the possible choices the Tile offers
    */
    public void resolveAction(boolean row, int index) {         //row = true riga sopra, row = false riga sotto

        Player p = this.players.get(currentPlayerIndex);
        Card c = null;
        boolean event = false;

        Tile targetTile = null;
        for(Tile t : this.board.getTrack()) {
            if (t.getStatus() && t.getPlayer().equals(p)) {
                targetTile = t;
                break;
            }
        }

        int upCards = Math.min(targetTile.getUpperRow(), this.board.getUpperRow().size());
        int downCards = Math.min(targetTile.getLowerRow(), this.board.getLowerRow().size());

        if (row && currentDrawnUpper >= upCards) {
            throw new IllegalStateException("You already drawn the max number of cards from the upper row");
        }
        if (!row && currentDrawnLower >= downCards) {
            throw new IllegalStateException("You already drawn the max number of cards from the lower row");
        }

        if (row) {
            c = this.board.getUpperRow().get(index);
            event = c.isEventCard();
            if (event) {
                throw new IllegalArgumentException("You cannot draw an EVENT CARD!!");
            }
            c = this.board.removeUpper(index);
            currentDrawnUpper++;
        }
        else{
            c = this.board.getUpperRow().get(index);
            event = c.isEventCard();
            if (event) {
                throw new IllegalArgumentException("You cannot draw an EVENT CARD!!");
            }
            c = this.board.removeLower(index);
            currentDrawnLower++;

        }
        p.drawCard(c);
        c.notifyBuildings(p);

        if(currentDrawnLower == targetTile.getLowerRow() && currentDrawnUpper == targetTile.getUpperRow()) {  //nextplayer se il giocatore ha pescato tutte le carte che poteva
            nextPlayer();
        }

    }

    public void resolveExtraPick(int pos) {

        if (this.currentPhase != GamePhase.EXTRA_PICK) {
            throw new IllegalArgumentException("Its not the phase to extra pick");
        }
        Player p = this.players.get(currentPlayerIndex);

        Card c = this.board.removeUpper(pos);
        p.drawCard(c);
        c.notifyBuildings(p);

        currentPlayerIndex = players.size();     //Re-impostiamo il player Index al max perchè prima era stato impostato al giocatore con l'extrapick building
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
