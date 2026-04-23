package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CardSetForFoodBuildingTest {

    @Test
    void cardSetForFoodBuildingTest() {
        CardSetForFoodBuilding b = new CardSetForFoodBuilding(1,1,0,0);
        Player p = new Player("Player1");

        HunterCard hunter1 = new HunterCard(2,1,2,false);
        BuilderCard builder1 = new BuilderCard(3,2,2,0,0);
        HarvesterCard harvester1 = new HarvesterCard(4,2,2);
        ArtistCard artist1 = new ArtistCard(5,1,2);
        InventorCard inventor1 = new InventorCard(6,2,2, Invention.ROPE);
        ShamanCard shaman1 = new ShamanCard(7,1,2,2);

        p.drawCard(hunter1);
        hunter1.notifyBuildings(p);

        p.drawCard(builder1);
        builder1.notifyBuildings(p);

        p.drawCard(harvester1);
        harvester1.notifyBuildings(p);

        p.drawCard(artist1);
        artist1.notifyBuildings(p);

        p.drawCard(inventor1);
        inventor1.notifyBuildings(p);

        p.drawCard(shaman1);
        shaman1.notifyBuildings(p);

        p.drawCard(b);

        // Checking building's effect doesn't take place
        assertEquals(0, p.getFood());

        HunterCard hunter2 = new HunterCard(8,1,2,false);
        BuilderCard builder2 = new BuilderCard(9,2,2,0,0);
        HarvesterCard harvester2 = new HarvesterCard(10,2,2);
        ArtistCard artist2 = new ArtistCard(11,1,2);
        InventorCard inventor2 = new InventorCard(12,2,2, Invention.ROPE);
        ShamanCard shaman2 = new ShamanCard(13,1,2,2);

        p.drawCard(hunter2);
        hunter2.notifyBuildings(p);

        p.drawCard(builder2);
        builder2.notifyBuildings(p);

        p.drawCard(harvester2);
        harvester2.notifyBuildings(p);

        p.drawCard(artist2);
        artist2.notifyBuildings(p);

        p.drawCard(inventor2);
        inventor2.notifyBuildings(p);

        // Checking building's effect doesn't take place yet
        assertEquals(0, p.getFood());

        p.drawCard(shaman2);
        shaman2.notifyBuildings(p);

        // Checking building's effect takes place
        assertEquals(5, p.getFood());
    }
}