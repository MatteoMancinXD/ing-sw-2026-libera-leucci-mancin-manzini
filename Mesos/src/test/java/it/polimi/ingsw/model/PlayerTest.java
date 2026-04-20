package it.polimi.ingsw.model;

import it.polimi.ingsw.model.buildings.InventorSetForFoodBuilding;
import it.polimi.ingsw.model.buildings.RitualEventNoMalusBuilding;
import it.polimi.ingsw.model.characters.HunterCard;
import it.polimi.ingsw.model.characters.Invention;
import it.polimi.ingsw.model.characters.InventorCard;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void creationTest() {
        Player p = new Player("Player1");

        // Checking default stats
        assertEquals("Player1", p.getNickname());
        assertEquals(0, p.getFood());
        assertEquals(0, p.getPrestige());

        // Checking building deck
        assertNotNull(p.getBuildings());
        assertEquals(0, p.getBuildings().size());

        // Checking character decks
        for(Character ch : Character.values()) {
            assertNotNull(p.getCharacterDeck(ch));
            assertEquals(0, p.getCharacterDeck(ch).size());
        }
    }

    @Test
    void drawCardTest() {
        Player p = new Player("Player1");
        CharacterCard cc = new HunterCard(1, 1, 2, Character.HUNTER, false);

        p.drawCard(cc);

        assertEquals(1, p.getCharacterDeck(Character.HUNTER).size());
        assertEquals(cc, p.getCharacterDeck(Character.HUNTER).getFirst());
    }

    @Test
    void inventorSetForFoodTest() {
        Player p = new Player("Player1");
        BuildingCard bc = new InventorSetForFoodBuilding(1,1,0, 0);
        CharacterCard inv1 = new InventorCard(2,1,2, Character.INVENTOR, Invention.ROPE);
        CharacterCard inv2 = new InventorCard(3,2,2, Character.INVENTOR, Invention.ROPE);

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
        assertEquals(1, p.getCharacterDeck(Character.INVENTOR).size());
        assertEquals(inv1, p.getCharacterDeck(Character.INVENTOR).getFirst());

        // Checking no food bonus yet
        assertEquals(0, p.getFood());

        p.drawCard(inv2);
        inv2.notifyBuildings(p);

        // Checking Inventor Deck
        assertEquals(2, p.getCharacterDeck(Character.INVENTOR).size());
        assertEquals(inv1, p.getCharacterDeck(Character.INVENTOR).getFirst());
        assertEquals(inv2, p.getCharacterDeck(Character.INVENTOR).get(1));

        // Checking food bonus
        assertEquals(3, p.getFood());
    }

    @Test
    void noNegativeFoodTest() {
        Player p = new Player("Player1");

        // Checking 0 food
        assertEquals(0, p.getFood());

        BuildingCard bc = new RitualEventNoMalusBuilding(1,1,5,0);

        assertThrows(IllegalArgumentException.class, () -> {
            p.drawCard(bc);
        });

        assertEquals(0, p.getFood());
    }

    @Test
    void immutabilityTest() {
        Player p =  new Player("Player1");
        BuildingCard bc = new InventorSetForFoodBuilding(1,1,0,0);

        p.drawCard(bc);
        List<BuildingCard> list = p.getBuildings();

        // Checking that Player is immutated
        assertEquals(1, p.getBuildings().size());
        assertEquals(bc, p.getBuildings().getFirst());
    }
}