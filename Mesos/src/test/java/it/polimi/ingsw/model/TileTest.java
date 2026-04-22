package it.polimi.ingsw.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class TileTest {

    @Test
    void testPlaceAndReset() {
        Tile tile = new Tile(2, 'B', 0 , 1);
        Player p = new Player("Matteo");

        tile.place(p);

        assertTrue(tile.getStatus(), "Tile's status with a player placed should be true");
        assertEquals(p, tile.getPlayer(), "Player should match the tile.getPlayer()");

        tile.reset();

        assertFalse(tile.getStatus(), "Tile's status after a reset should be false");
        assertNull(tile.getPlayer(), "Tile's player after reset should be null");
    }

}
