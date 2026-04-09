package it.polimi.ingsw;
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
        modifiers[0] = 3;

        if (numPlayers >= 2)
        {modifiers[1] = 1; }

        for (int i = 2; i < numPlayers - 1; i++)
        { modifiers[i] = 0;}

        if (numPlayers > 2)
        {modifiers[numPlayers - 1] = -1; // L'ultimo paga 1 cibo
             }
    }
    /**
     * Returns the modifiers array.
     *  @return food modifiers for each position
     */
    public int[] getModifiers() { return modifiers; }
}