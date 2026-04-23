package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RitualEventBonusStarsBuildingTest {

    @Test
    void ritualEventBonusStarsBuilding() {
        Player p = new Player("Player");
        RitualEventBonusStarsBuilding b = new RitualEventBonusStarsBuilding(0,0,0,0);

        // Checking player has 0 stars
        assertEquals(0, p.getTotStars());

        p.drawCard(b);

        // Checking player has 3 stars
        assertEquals(3, p.getTotStars());
    }
}