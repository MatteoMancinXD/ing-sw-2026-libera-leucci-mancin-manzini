package it.polimi.ingsw.model;

import it.polimi.ingsw.model.buildings.InventorSetForFoodBuilding;
import it.polimi.ingsw.model.buildings.PrestigeGivingBuilding;
import it.polimi.ingsw.model.buildings.RitualEventNoMalusBuilding;
import it.polimi.ingsw.model.characters.*;
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
        assertEquals(0, p.getTotStars());

        // Checking buildings deck
        assertNotNull(p.getBuildings());
        assertEquals(0, p.getBuildings().size());

        // Checking hunters deck
        assertNotNull(p.getHunters());
        assertEquals(0, p.getHunters().size());

        // Checking builders deck
        assertNotNull(p.getBuilders());
        assertEquals(0, p.getBuilders().size());

        // Checking harvesters deck
        assertNotNull(p.getHarvesters());
        assertEquals(0, p.getHarvesters().size());

        // Checking artists deck
        assertNotNull(p.getArtists());
        assertEquals(0, p.getArtists().size());

        // Checking inventors deck
        assertNotNull(p.getInventors());
        assertEquals(0, p.getInventors().size());

        // Checking shamans deck
        assertNotNull(p.getShamans());
        assertEquals(0, p.getShamans().size());
    }

    @Test
    void drawCardTest() {
        Player p = new Player("Player1");
        p.editFood(7);

        CharacterCard hunter = new HunterCard(1, 1, 2, false);
        CharacterCard builder = new BuilderCard(2, 1, 2, 0, 2);
        CharacterCard harvester = new HarvesterCard(3, 1, 2);
        CharacterCard artist = new ArtistCard(1, 1, 2);
        CharacterCard inventor = new InventorCard(1, 1, 2, Invention.ROPE);
        CharacterCard shaman = new ShamanCard(1, 1, 2, 2);

        BuildingCard b = new PrestigeGivingBuilding(1, 1, 5, 0);

        p.drawCard(hunter);
        p.drawCard(builder);
        p.drawCard(harvester);
        p.drawCard(artist);
        p.drawCard(inventor);
        p.drawCard(shaman);
        p.drawCard(b);

        // Checking hunters deck
        assertEquals(1, p.getHunters().size());
        assertEquals(hunter, p.getHunters().getLast());

        // Checking builders deck
        assertEquals(1, p.getBuilders().size());
        assertEquals(builder, p.getBuilders().getLast());

        // Checking harvesters deck
        assertEquals(1, p.getHarvesters().size());
        assertEquals(harvester, p.getHarvesters().getLast());

        // Checking artists deck
        assertEquals(1, p.getArtists().size());
        assertEquals(artist, p.getArtists().getLast());

        // Checking inventors deck
        assertEquals(1, p.getInventors().size());
        assertEquals(inventor, p.getInventors().getLast());

        // Checking shamans deck
        assertEquals(1, p.getShamans().size());
        assertEquals(shaman, p.getShamans().getLast());

        // Checking buildings deck
        assertEquals(1, p.getBuildings().size());
        assertEquals(b, p.getBuildings().getLast());

        // Checking player's food is 4 (7 - (5-2))
        assertEquals(4, p.getFood());
    }

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
        list.clear();

        // Checking that Player is immutated
        assertEquals(1, p.getBuildings().size());
        assertEquals(bc, p.getBuildings().getFirst());
    }
}