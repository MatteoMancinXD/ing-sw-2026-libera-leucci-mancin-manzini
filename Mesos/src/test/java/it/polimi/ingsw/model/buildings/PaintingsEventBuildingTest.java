package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.ArtistCard;
import it.polimi.ingsw.model.events.PaintingsEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PaintingsEventBuildingTest {

    @Test
    void paintingEventBuildingTest() {
        PaintingsEvent paint = new PaintingsEvent(0,3);
        Player p = new Player("Player");

        for(int i = 0; i < 2; i++) {
            p.drawCard(new ArtistCard(0,2,0));
        }

        p.drawCard(new PaintingsEventBuilding(0,0,0,0));

        paint.solveEventCard(p, new ArrayList<>());

        // Checking player has -2 prestige and 2 food
        assertEquals(2, p.getFood());
        assertEquals(-2, p.getPrestige());
    }
}