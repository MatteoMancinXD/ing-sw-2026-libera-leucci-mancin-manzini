package it.polimi.ingsw.model;
/**
 * Represents the turn order tile where players move their totems at the end of the placement phase.
 */
public class OrderTile {
    private int[] modifiers;
    /**
     * Creates the order tile and sets up food modifiers for the given player count. The first player gains 3 food, the second gains 1, middle players gain nothing, and the last player loses 1 food.
     * @param numPlayers number of players in the game
     */
    public OrderTile(int numPlayers) {
        this.modifiers = new int[numPlayers];
        setupModifiers(numPlayers);    }
    /**
     * Initializes the modifiers array based on game rules.
     *  @param numPlayers number of players
     */
    private void setupModifiers(int numPlayers) {


        if (numPlayers == 2)
        {   modifiers[0] = 1;
            modifiers[1] = -1; }

        if (numPlayers == 3)
        {   modifiers[0] = 2;
            modifiers[1] = 0;
            modifiers[2] = -1;
        }
        if (numPlayers == 4)
        {   modifiers[0] = 2;
            modifiers[1] = 1;
            modifiers[2] = 0;
            modifiers[3] = -1;
        }
        if (numPlayers == 5)
        {   modifiers[0] = 3;
            modifiers[1] = 1;
            modifiers[2] = 0;
            modifiers[3] = 0;
            modifiers[4] = -1;
        }
    }
    /**
     * Returns the modifiers array.
     *  @return food modifiers for each position
     */
    public int[] getModifiers() { return modifiers; }
}