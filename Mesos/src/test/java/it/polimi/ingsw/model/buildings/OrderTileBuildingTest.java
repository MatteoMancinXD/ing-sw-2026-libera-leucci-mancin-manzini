package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Game;
import it.polimi.ingsw.model.OrderTile;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class OrderTileBuildingTest {

    @Test
    void testConstructors() {
        OrderTileBuilding building = new OrderTileBuilding(10, 1, 3, 5);
        assertNotNull(building);
        assertEquals(3, building.getFoodCost());
        assertEquals(5, building.getPrestigeGain());

        OrderTileBuilding emptyBuilding = new OrderTileBuilding();
        assertNotNull(emptyBuilding);
    }

    @Test
    void testOnOrderTilePlacement() {
        Player player = new Player("TestPlayer");
        OrderTileBuilding building = new OrderTileBuilding(1, 1, 3, 3);

        Game game = new Game(4);
        OrderTile realOrderTile = game.getBoard().getOrder();
        int[] modifiers = realOrderTile.getModifiers();
        int positiveIndex = -1;
        int zeroOrNegativeIndex = -1;

        for (int i = 0; i < modifiers.length; i++) {
            if (modifiers[i] > 0) {
                positiveIndex = i;
            } else {
                zeroOrNegativeIndex = i;
            }
        }

        if (positiveIndex != -1) {
            player.setFood(2); // Cibo di partenza
            building.onOrderTilePlacement(player, positiveIndex, realOrderTile);

            assertEquals(3, player.getFood());
        }

        if (zeroOrNegativeIndex != -1) {
            player.setFood(2); // Cibo di partenza
            building.onOrderTilePlacement(player, zeroOrNegativeIndex, realOrderTile);
            assertEquals(2, player.getFood());
        }
    }

    @Test
    void testGetShortString() {
        OrderTileBuilding building = new OrderTileBuilding(1, 1, 4, 6);
        String description = building.getShortString();

        assertNotNull(description);
        assertTrue(description.contains("cost=4"));
        assertTrue(description.contains("pp=6"));
        assertTrue(description.contains("+1 food"));
    }
}