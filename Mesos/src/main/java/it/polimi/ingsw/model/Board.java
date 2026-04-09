package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents the main game board. Manages the two rows, the track tiles, the building card display. Also handles row shifting and event resolution between rounds.
 */
public class Board {
    private List<Card> upperRow;
    private List<Card> lowerRow;
    private List<Tile> track;
    private OrderTile order;
    /**
     * The main game deck containing the 96 Tribe cards. The board uses this deck to draw new cards and fill the offer rows during the game.
     */
    private Deck deck;
    private ArrayList<Card> buildingPool;      // Aggiunta questa lista per conservare i 21 edifici in attesa di essere messi in gioco. L'UML prevede solo un Deck da 96 carte tribu
    private boolean initialized; // Serve perché fill si comporta in modo diverso al round 0 (riempie entrambe le row) rispetto alle successive (solo upperrow).

    /**
     * Creates the board, initializes rows, deck, and track tiles.
     * @param numPlayers number of players in the game
     */
    public Board(int numPlayers) {
        this.upperRow = new ArrayList<>();
        this.lowerRow = new ArrayList<>();
        this.track = new ArrayList<>();
        this.order = new OrderTile(numPlayers);
        this.deck = new Deck();
        this.buildingPool = new ArrayList<>();

        // In attesa della classe BuildingCard. da inserire qui le carte edificio


        this.initialized = false;

        setup(numPlayers);
    }

    /**
     * Initializes the track with the correct tiles based on player count. Tiles requiring more players than the current game are left out.
     * @param numPlayers number of players in the game
     */
    public void setup(int numPlayers) {
        if (numPlayers == 5) {track.add(new Tile(5, 'A', 0, 0, 3));}
        track.add(new Tile(2, 'B', 0, 1));
        track.add(new Tile(2, 'C', 1, 0));
        track.add(new Tile(2, 'D', 1, 1));
        if (numPlayers >= 3) {track.add(new Tile(3, 'E', 2, 1));}
        if (numPlayers >= 4){ track.add(new Tile(4, 'F', 2, 1));}
        if (numPlayers == 5) {track.add(new Tile(5, 'G', 3, 1));}
    }

    /**
     * Fills the board with cards drawn from the deck. On the first call (round 0) fills both rows and adds Era 1 building cards. On subsequent calls fills only the upper row.
     * @param numPlayers the number of players
     * @param currentEra the current era of the game
     * @return true if a card from a new era was drawn, false otherwise
     */
    public boolean fill(int numPlayers, int currentEra) {
        boolean eraChanged = false;
        if (!initialized) {
            int i = 0;
            while (i < numPlayers + 1) {
                Card c = deck.draw();
                if (c == null) {
                    break;
                };

                // Gli eventi vanno sempre nella riga superiore
                if (c instanceof EventCard) {
                    upperRow.add(c);
                } else {
                    lowerRow.add(c);
                    i++;
                }
            }
            fillBuildings(1);  // Gli edifici di era I vengono prelevati dalla riserva e messi nella upperrow
            initialized = true;
        }

        // La upperrow riceve numPlayers + 4 carte ad ogni round
        int i = 0;
        while (i < numPlayers + 4) {
            Card c = deck.draw();
            if (c == null) {break};
            upperRow.add(c);
            i++;
            if (c.getEra() > currentEra) {
                eraChanged = true;
            }
        }

        return eraChanged;
    }
    /**
     * Removes all cards that are not building type from the lowerrow at the end of a round. Building cards remain until the next era transition.
     */
    public void clearLowerRow() {
        ArrayList<Card> surviving = new ArrayList<>();
        for (Card c : lowerRow) {
            if (c instanceof BuildingCard)
                surviving.add(c);
        }
        lowerRow = surviving;
    }
    /**
     * Moves non-building cards from  upperrow down to the lowerrow.
     */
    public void shiftRow() {
        ArrayList<Card> keepUp = new ArrayList<>();
        ArrayList<Card> goDown = new ArrayList<>();
        for (Card c : upperRow) {
            if (c instanceof BuildingCard)
                keepUp.add(c);
            else
                goDown.add(c);
        }

        upperRow = keepUp;
        lowerRow.addAll(goDown);
    }

    /**
     * Resolves all event cards currently in  lowerrow.
     */
    public void solveEvents() {
        EventCard sustenance = null;

        for (Card c : lowerRow) {
            if (c instanceof SustenanceEvent) {
                sustenance = (EventCard) c;
            } else if (c instanceof EventCard) {
                ((EventCard) c).solve();
            }
        }

        if (sustenance != null) {
            sustenance.solve();
        }
    }

    /**
     * Handles building card movement at the start of a new era, discards lower row buildings and moves upper row buildings down.
     */
    public void shiftBuildings() {
        // Scarta gli edifici rimasti nella riga inferiore
        ArrayList<Card> lowerNoBuildings = new ArrayList<>();
        for (Card c : lowerRow) {
            if (!(c instanceof BuildingCard))
                lowerNoBuildings.add(c);
        }
        lowerRow = lowerNoBuildings;

        // Sposta gli edifici dell'era appena finita dalla riga superiore alla inferiore
        ArrayList<Card> buildings = new ArrayList<>();
        ArrayList<Card> rest = new ArrayList<>();
        for (Card c : upperRow) {
            if (c instanceof BuildingCard)
                buildings.add(c);
            else
                rest.add(c);
        }
        upperRow = rest;
        lowerRow.addAll(buildings);
    }
    /**
     * Extracts building cards of the current era from the pool and adds them to upperrow.
     * @param currentEra the current game era
     */
    public void fillBuildings(int currentEra)  //non presente in uml usato per gestire l'ingresso delle carte Edificio ad ogni cambio di Era.
     { ArrayList<Card> toKeep = new ArrayList<>();
        for (Card c : buildingPool) {
            if (c.getEra() == currentEra) {upperRow.add(c);}
            else {toKeep.add(c);}  // Lo teniamo nella riserva per le prossime ere
        }
        this.buildingPool = toKeep; } // rimuovo gli edifici piazzati dalla riserva
    /**
     * Resets all track tiles, freeing them from any placed totems.
     */
    public void resetTrackTiles() {
        for (Tile t : track) {
            t.reset();
        }
    }
    public List<Tile> getTrack() {return track;}
    public List<Card> getUpperRow() {return upperRow;}
    public List<Card> getLowerRow() {return lowerRow;}
    public OrderTile getOrder() {return order;}
}