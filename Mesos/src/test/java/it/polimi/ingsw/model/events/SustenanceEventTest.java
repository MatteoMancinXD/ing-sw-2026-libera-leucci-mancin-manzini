package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.*;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class SustenanceEventTest {

    @Test
    void sustenanceEventTest() {
        SustenanceEvent sustenance = new SustenanceEvent(1,2,false);
        Player p = new Player("Player1");
        p.editFood(4);

        HunterCard hunter = new HunterCard(2,1,2,false);
        BuilderCard builder = new BuilderCard(3,2,2,0,0);
        HarvesterCard harvester = new HarvesterCard(4,2,2);
        ArtistCard artist = new ArtistCard(5,1,2);
        InventorCard inventor = new InventorCard(6,2,2, Invention.ROPE);
        ShamanCard shaman = new ShamanCard(7,1,2,2);

        p.drawCard(hunter);
        p.drawCard(builder);
        p.drawCard(harvester);
        p.drawCard(artist);
        p.drawCard(inventor);
        p.drawCard(shaman);

        // Checking player has 4 food and 0 prestige
        assertEquals(4, p.getFood());
        assertEquals(0, p.getPrestige());

        sustenance.solveEventCard(p, new ArrayList<Player>());

        // Checking player paid only 3 food instead of 6 (harvester effect) and 0 prestige
        assertEquals(1, p.getFood());
        assertEquals(0, p.getPrestige());

        sustenance.solveEventCard(p, new ArrayList<Player>());

        // Checking player has now 0 food and -4 prestige (2 characters not fed * 2 era)
        assertEquals(0, p.getFood());
        assertEquals(-4, p.getPrestige());
    }

}