package it.polimi.ingsw.model;
import it.polimi.ingsw.controller.GameObserver;
import it.polimi.ingsw.model.events.SustenanceEvent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
/**
 * Represents the main game board. Manages the two rows, the track tiles, the building card display. Also handles row shifting and event resolution between rounds.
 */
public class Board implements Serializable {
    private List<Card> upperRow;
    private List<Card> lowerRow;
    private List<Tile> track;
    private OrderTile order;
    /**
     * The main game deck containing the 96 Tribe cards. The board uses this deck to draw new cards and fill the offer rows during the game.
     */
    //private TribeDeck deck;
    private ArrayList<BuildingCard> buildingPool;      // Aggiunta questa lista per conservare i 21 edifici in attesa di essere messi in gioco. L'UML prevede solo un Deck da 96 carte tribu
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
        //this.deck = new TribeDeck();
        this.buildingPool = new ArrayList<>();

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
        if (numPlayers >= 3) { track.add(new Tile(3, 'D', 0, 2));}
        track.add(new Tile(2, 'E', 1, 1));
        track.add(new Tile(2, 'F', 2, 0));
        if (numPlayers >= 4){track.add(new Tile(4, 'G', 2, 1));}
    }

    /**
     * Fills the board with cards drawn from the deck. On the first call (round 0) fills both rows and adds Era 1 building cards. On subsequent calls fills only the upper row.
     * @param numPlayers the number of players
     * @param currentEra the current era of the game
     * @return true if a card from a new era was drawn, false otherwise
     */
    public boolean fill(int numPlayers, int currentEra, TribeDeck deck, BuildingDeck bDeck) {
        boolean eraChanged = false;
        if (!initialized) {
            int i = 0;
            while (i < numPlayers + 1) {
                Card c = deck.draw();
                if (c == null) {
                    break;
                }

                // Gli eventi vanno sempre nella riga superiore
                if (c.isEventCard()) {
                    upperRow.add(c);
                } else {
                    lowerRow.add(c);
                    i++;
                }
            }

            while (upperRow.size() < numPlayers + 4){
                Card c = deck.draw();
                if (c == null) {
                    break;
                }
                upperRow.add(c);
            }

            fillBuildings(1, numPlayers, bDeck);  // Gli edifici di era I vengono prelevati dalla riserva e messi nella upperrow

            initialized = true;
        }// La upperrow riceve numPlayers + 4 carte ad ogni round
        else {
            int numBuildings = findBuildings();
            while (upperRow.size() < numPlayers + 4 + numBuildings && deck.size() > 0) {
                Card c = deck.draw();
                if (c == null) {break;}
                upperRow.add(c);
                if (c.getEra() > currentEra) {
                    eraChanged = true;
                }
            }
        }
        return eraChanged;
    }

    private int findBuildings() {
        int numBuildings = 0;
        for (Card c : upperRow) {
            if (c.isBuildingCard()) {
                numBuildings++;
            }
        }
        return numBuildings;
    }

    /**
     * Removes all cards that are not building type from the lowerrow at the end of a round. Building cards remain until the next era transition.
     */
    public void clearLowerRow() {
        ArrayList<Card> surviving = new ArrayList<>();
        for (Card c : lowerRow) {
            if (c.isBuildingCard())
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
            if (c.isBuildingCard())
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
    public void solveEvents(List<Player> players, GameObserver observer) {
        EventCard sustenance = null;

        for (Card c : lowerRow) {
            if (c.isSustenanceEvent()) {
                sustenance = (EventCard) c;
            } else if (c.isEventCard()) {
                observer.onEventResolution((EventCard) c);

                for (Player p : players) {
                    ((EventCard) c).solveEventCard(p, players);
                }
            }
        }

        if (sustenance != null) {
            observer.onEventResolution(sustenance);
            for (Player p : players) {
                ((EventCard) sustenance).solveEventCard(p, players);
            }
        }
    }

    /**
     * Handles building card movement at the start of a new era, discards lower row buildings and moves upper row buildings down.
     */
    public void shiftBuildings() {
        // Scarta gli edifici rimasti nella riga inferiore
        ArrayList<Card> lowerNoBuildings = new ArrayList<>();
        for (Card c : lowerRow) {
            if (!(c.isBuildingCard()))
                lowerNoBuildings.add(c);
        }
        lowerRow = lowerNoBuildings;

        // Sposta gli edifici dell'era appena finita dalla riga superiore alla inferiore
        ArrayList<Card> buildings = new ArrayList<>();
        ArrayList<Card> rest = new ArrayList<>();
        for (Card c : upperRow) {
            if (c.isBuildingCard())
                buildings.add(c);
            else
                rest.add(c);
        }
        upperRow = rest;
        lowerRow.addAll(buildings);
    }
    /**
     * Extracts building cards of the current era from the buildingDeck and put them into the upperRow.
     * @param currentEra the current game era
     * @param numPlayers number of players in the Game
     * @param buildDeck building deck containing every building in the game
     */
    public void fillBuildings(int currentEra, int numPlayers, BuildingDeck buildDeck)
     {
         //mazzo di building arriva già shufflato
        ArrayList<BuildingCard> bEraDeck = new ArrayList<>();
        int numBuilds = buildDeck.getBuildingCardsForPlayers().get(numPlayers).get(currentEra-1);   //Prende il numero di building da mettere nella buildingPool
        int cont = 0;

        for(BuildingCard b : buildDeck.getBuildingsCards()) {
            if (cont == numBuilds) {break;}
            if(b.getEra() == currentEra) {
                bEraDeck.add(b);
                cont++;
            }
        }
        this.upperRow.addAll(bEraDeck);
     }
    /**
     * Resets all track tiles, freeing them from any placed totems.
     */
    public void resetTrackTiles() {
        for (Tile t : track) {
            t.reset();
        }
    }

    /**
     * Loads the valid building cards for the current game into the board.
     * This method is expected to be called by the Game class after
     * generating and shuffling the cards via the BuildingDeck.
     *
     * @param buildings the list of valid building cards to be added to the pool
     */
    public void setBuildingPool(ArrayList<BuildingCard> buildings)
    {
        this.buildingPool = buildings;
    }

    public Card removeUpper(int pos) {
        return this.upperRow.remove(pos);
    }

    public Card removeLower(int pos) {
        return this.lowerRow.remove(pos);
    }

    public List<Tile> getTrack() {return track;}
    public List<Card> getUpperRow() {return upperRow;}
    public List<Card> getLowerRow() {return lowerRow;}
    public OrderTile getOrder() {return order;}
}