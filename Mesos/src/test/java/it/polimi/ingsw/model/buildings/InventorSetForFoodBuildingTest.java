package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.BuildingCard;
import it.polimi.ingsw.model.CharacterCard;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.Invention;
import it.polimi.ingsw.model.characters.InventorCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InventorSetForFoodBuildingTest {

    @Test
    void inventorSetForFoodTest() {
        Player p = new Player("Player1");
        BuildingCard bc = new InventorSetForFoodBuilding(1,1,0, 0);
        CharacterCard inv1 = new InventorCard(2,1,2, Invention.ROPE);
        CharacterCard inv2 = new InventorCard(3,2,2, Invention.ROPE);

        // Checking that player has 0 food now
        assertEquals(0, p.getFood());

        p.drawCard(bc);
        bc.notifyBuildings(p);

        // Checking BuildingDeck
        assertEquals(1, p.getBuildings().size());
        assertEquals(bc, p.getBuildings().getFirst());

        p.drawCard(inv1);
        inv1.notifyBuildings(p);

        // Checking Inventor Deck
        assertEquals(1, p.getInventors().size());
        assertEquals(inv1, p.getInventors().getFirst());

        // Checking no food bonus yet
        assertEquals(0, p.getFood());

        p.drawCard(inv2);
        inv2.notifyBuildings(p);

        // Checking Inventor Deck
        assertEquals(2, p.getInventors().size());
        assertEquals(inv1, p.getInventors().getFirst());
        assertEquals(inv2, p.getInventors().get(1));

        // Checking food bonus
        assertEquals(3, p.getFood());

        p.drawCard(new InventorCard(0,0,0,Invention.ROPE));

        // Checking food bonus not added
        assertEquals(3, p.getFood());

        p.drawCard(new InventorCard(0,0,0,Invention.POT));

        // Checking food bonus not added
        assertEquals(3, p.getFood());
    }
}