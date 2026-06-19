package it.polimi.ingsw.model;
import it.polimi.ingsw.controller.GameObserver;
import it.polimi.ingsw.model.buildings.ExtraPickBuilding;
import it.polimi.ingsw.model.characters.BuilderCard;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import it.polimi.ingsw.network.snapshots.GameSnapshot;

import java.io.File;
import java.io.InputStream;
import java.util.*;

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
    private List<Player> players;

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
        deck.shuffle();

        this.buildingDeck = new BuildingDeck(allBuildings, numPlayers);
        //board.setTribeDeck(this.deck);
    }

    /**
     * Registers a {@link GameObserver} to receive callbacks for game events
     * such as event resolution and game end.
     * @param observer the observer to register
     */
    public void addObserver(GameObserver observer) {
        this.observer = observer;
    }


    public void setRound(int round){
        this.round = round;
    }

    /** @return the current round number (1 to 10) */
    public int getRound() {
        return this.round;
    }
    public void setEra(int era) {
        this.era = era;
    }

    /** @return the current era (1, 2, or 3) */
    public int getEra() {
        return era;
    }

    /** @return the current game phase as a string ("PLACEMENT", "RESOLUTION", or "EXTRA_PICK") */
    public String getCurrentPhase() {  return currentPhase.toString(); }

    /** @return the player whose turn it currently is */
    public Player getCurrentPlayer() { return players.get(currentPlayerIndex);}

    /**
     * Loads building cards from the JSON resource file at {@code /json/buildingsInfo.json}.
     * Uses Jackson polymorphic deserialization to instantiate the correct subclass for each building.
     * @return list of all building cards loaded from the file
     */
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


    /**
     * Loads tribe cards from the JSON resource file at {@code /json/cardsInfo.json}.
     * Cards are organized by era in the JSON and the era number is assigned during parsing.
     * @return list of all tribe cards loaded from the file
     */
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

    /** @return the list of players in the game, ordered by current turn order */
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

        board.getOrder().setPlayers(players);

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

        BuildingCard extraPickCard = new ExtraPickBuilding();
        if (extraPickCard != null) {
            //this.players.get(0).editFood(100);
            this.players.get(0).drawCard(extraPickCard); // O il metodo che usi per dare l'edificio
            System.out.println("HACK: Extra Pick building regalato a " + this.players.get(0).getNickname());
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

        for (Player p: this.players) {
            for(BuildingCard b : p.getBuildings()) {
                p.editPrestige(b.getPrestigeGain());
                b.onGameEnd(p);
            }
            List<BuilderCard> builders = p.getBuilders();
            for (BuilderCard b : builders) {
                p.editPrestige(b.getPps());
            }
        }

        ArrayList<Player> rankings = new ArrayList<>(this.players);

        rankings.sort((p1, p2) -> {
            if (p1.getPrestige() != p2.getPrestige()) {
                return Integer.compare(p2.getPrestige(), p1.getPrestige());
            }
            else {
                return Integer.compare(p2.getFood(), p1.getFood());
            }
            });

        if(observer != null) {
            observer.onGameEnd(rankings);
        }
        return rankings;
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
            currentPlayerIndex = 0;
            if(currentPhase == GamePhase.PLACEMENT) {   //Si passa dalla fase di piazzamento alla risoluzione
                currentPhase = GamePhase.RESOLUTION;
                recalculateDrawOrder();

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

        if(currentPhase == GamePhase.RESOLUTION) {
            Player p = players.get(currentPlayerIndex);
            Tile targetTile = null;
            for(Tile t : board.getTrack()) {
                if(t.getStatus() && t.getPlayer().equals(p)) {
                    targetTile = t;
                    break;
                }
            }

            if(targetTile != null){
                handleRemainingCards(targetTile, p);
            }
        }

    }
    /**
     * Reorders the player list based on the track tile positions for the resolution phase.
     * Players are ordered from the first occupied tile to the last.
     */
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

    /**
     * Checks whether any player owns the Extra Pick building.
     * @return the player who owns the building, or null if nobody does
     */
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

    /**
     * Skips the extra pick phase when the player chooses not to use
     * the Extra Pick building bonus.
     * @throws IllegalStateException if the current phase is not EXTRA_PICK
     */
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
        this.board.getOrder().setPlayers(this.players);

        this.currentPlayerIndex = 0;
        //logica per riordinare i players in base all'ordine sulla tileboard
        int pos = 0;
        for(Tile tile: board.getTrack()) {
            if (tile.getStatus()) {
                Player p = tile.getPlayer();
                for(BuildingCard b : p.getBuildings()) {
                    b.onOrderTilePlacement(p, pos, board.getOrder());
                }
                pos++;
            }
        }

        //Dare prestigio in base all order tile:
        OrderTile oTile = board.getOrder();
        for (int i = 0; i < this.players.size(); i++) {
            try {
                players.get(i).editFood(oTile.getModifiers()[i]);
            } catch (IllegalArgumentException e) {
                players.get(i).editPrestige(-2);      //se non ha abbastanza cibo si toglie 2 di prestigio
            }

        }


        board.resetTrackTiles();

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
     * @throws IllegalStateException if the player choices are more than the possible choices the Tile offers on the 2 rows
     * @throws IllegalArgumentException if the player try to draw an event card
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

        if (targetTile == null) {
            throw new IllegalStateException("No tile found for this player! ");
        }

        if (row && currentDrawnUpper >= targetTile.getUpperRow()) {
            throw new IllegalStateException("You already drawn the max number of cards from the upper row");
        }
        if (!row && currentDrawnLower >= targetTile.getLowerRow()) {
            throw new IllegalStateException("You already drawn the max number of cards from the lower row");
        }

        if (row) {
            c = this.board.getUpperRow().get(index);
            event = c.isEventCard();
            if (event) {
                throw new IllegalArgumentException("You cannot draw an EVENT CARD!!");
            }
            if (c.isBuildingCard()) {
                int totalFood = p.getFood() + p.getTotDiscount();
                if (c.getFoodCost() > totalFood) {
                    throw new IllegalArgumentException("You don't have enough food to buy this building! ");
                }
            }
            c = this.board.removeUpper(index);
            currentDrawnUpper++;
        }
        else{
            c = this.board.getLowerRow().get(index);
            event = c.isEventCard();
            if (event) {
                throw new IllegalArgumentException("You cannot draw an EVENT CARD!!");
            }
            if (c.isBuildingCard()) {
                int totalFood = p.getFood() + p.getTotDiscount();
                if (c.getFoodCost() > totalFood) {
                    throw new IllegalArgumentException("You don't have enough food to buy this building! ");
                }
            }
            c = this.board.removeLower(index);
            currentDrawnLower++;

        }
        p.drawCard(c);
        c.notifyBuildings(p);

        handleRemainingCards(targetTile, p);
    }

    private void handleRemainingCards(Tile targetTile, Player p) {
        int remainingAllowedUpper = targetTile.getUpperRow() - currentDrawnUpper;
        int remainingAllowedLower = targetTile.getLowerRow() - currentDrawnLower;

        int availableUpper = countPlayableCards(this.board.getUpperRow(), p);
        int availableLower = countPlayableCards(this.board.getLowerRow(), p);

        boolean canStillDrawUpper = (remainingAllowedUpper > 0) && (availableUpper > 0); //il player può ancora pescare da sopra
        boolean canStillDrawLower = (remainingAllowedLower > 0) && (availableLower > 0);    //il player può ancora pescare da sotto

        // se non può pescare ne da sopra ne da sotto --> nextplayer
        if (!canStillDrawUpper && !canStillDrawLower) {
            nextPlayer();
        }
    }

    /**
     * Private support method to count how many drawable cards are available in a given row
     */
    private int countPlayableCards(List<Card> rowCards, Player p) {
        int available = 0;
        int totalFood = p.getFood() + p.getTotDiscount();

        for (Card card : rowCards) {
            if (!card.isEventCard() && !card.isBuildingCard()) {
                available++;
            } else if (card.isBuildingCard() && card.getFoodCost() <= totalFood) {
                available++;
            }
        }
        return available;
    }

    /**
     * Resolves the extra pick action, drawing one card from the upper row
     * for the player who owns the Extra Pick building.
     * @param pos the index of the card to draw from the upper row
     * @throws IllegalArgumentException if the current phase is not EXTRA_PICK
     */
    public void resolveExtraPick(int pos) {

        if (this.currentPhase != GamePhase.EXTRA_PICK) {
            throw new IllegalArgumentException("Its not the phase to extra pick");
        }
        Player p = this.players.get(currentPlayerIndex);

        Card c = this.board.getUpperRow().get(pos);

        if (c.isEventCard()) {
            throw new IllegalArgumentException("You cannot pick an EVENT CARD!!");
        }
        if (c.isBuildingCard()) {
            int totalFood = p.getFood() + p.getTotDiscount();
            if (c.getFoodCost() > totalFood) {
                throw new IllegalArgumentException("You don't have enough food to buy this building! ");
            }
        }
        this.board.removeUpper(pos);
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

        List<Player> orderPlayers = this.board.getOrder().getPlayers();
        orderPlayers.set(currentPlayerIndex, null);
        this.board.getOrder().setPlayers(orderPlayers);

        nextPlayer();
    }

    /**
     * Assigns a totem color to a player identified by their nickname.
     * @param nickname the nickname of the player
     * @param totem    the totem color to assign
     */
    public void assignTotem(String nickname, Totem totem) {
        players.stream().filter(p -> p.getNickname().equals(nickname)).findFirst().ifPresent(player -> player.setTotem(totem));
    }

    public GameSnapshot toSnapshot() {
        return new GameSnapshot(
                round,
                era,
                numPlayers,
                currentPlayerIndex,
                players.stream().map(Player::toSnapshot).toList(),
                currentPhase,
                board.toSnapshot()
        );
    }
}
