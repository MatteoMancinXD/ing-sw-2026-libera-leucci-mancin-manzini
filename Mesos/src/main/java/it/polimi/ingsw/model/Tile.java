package it.polimi.ingsw.model;

import java.io.Serializable;

/**
 * Represents an offer tile on the board track, each tile specifies how many cards a player draws from each row.
 */
public class Tile implements Serializable {
    private int minPlayers;
    private char letter;
    private int upperRow;
    private int lowerRow;

    private int foodBonus;
    private boolean status;
    private Player player;

    /**
     * Creates a standard offer tile with no food bonus.
     * @param minPlayers minimum number of players for this tile
     * @param letter     letter identifying this tile
     * @param upperRow   cards to draw from the upper row
     * @param lowerRow   cards to draw from the lower row
     */
    public Tile(int minPlayers, char letter, int upperRow, int lowerRow) {
        this.minPlayers = minPlayers;
        this.letter= letter;
        this.upperRow = upperRow;
        this.lowerRow= lowerRow;
        this.foodBonus = 0;
        this.status= false; // Naturalmente la tessera è libera
        this.player= null;
    }

    /**
     * Creates a tile that grants food instead of card draws.
     * @param minPlayers minimum number of players for this tile
     * @param letter     letter identifying this tile
     * @param upperRow   cards to draw from the upper row
     * @param lowerRow   cards to draw from the lower row
     * @param foodBonus  immediate food granted by this tile
     */
    public Tile(int minPlayers, char letter, int upperRow, int lowerRow, int foodBonus) {
        this(minPlayers, letter, upperRow, lowerRow);
        this.foodBonus= foodBonus;
    }

    /**
     * Places a player's totem on this tile and marks it as occupied.
     * @param p the player placing their totem
     */
    public void place(Player p) {
        this.status= true;
        this.player =p;
        p.editFood(this.foodBonus);
    }

    /**
     * Clears the tile at the end of a round.
     */
    public void reset() {
        this.status= false;
        this.player = null;
    }
    /**
     * @return true if a totem is currently on this tile
     */
    public boolean getStatus() { return status; }
    /**
     * @return the player whose totem is on this tile, null if free
     */
    public Player getPlayer() {return player;}
    /**
     * @return the letter identifying this tile
     */
    public char getLetter() {  return letter;}
    /**
     * @return number of cards to draw from the upper row
     */
    public int getUpperRow() {return upperRow;}
    /**
     * @return number of cards to draw from the lower row
     */
    public int getLowerRow() {return lowerRow;}
    /**
     * @return minimum number of players required for this tile
     */
    public int getMinPlayers() {return minPlayers; }
    /**
     * @return immediate food bonus granted by this tile
     */
    public int getFoodBonus() { return foodBonus;   }
}