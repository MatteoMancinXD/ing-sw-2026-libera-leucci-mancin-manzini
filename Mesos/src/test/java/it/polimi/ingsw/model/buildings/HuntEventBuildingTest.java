package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.HunterCard;
import it.polimi.ingsw.model.events.HuntEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class HuntEventBuildingTest {

    @Test
    void huntEventBuildingTest() {
        HuntEvent hunt = new HuntEvent(1,2,false);
        HuntEventBuilding b = new HuntEventBuilding(0,0,0,0);
        Player p = new Player("Player");

        for(int i = 0; i < 3; i++) {
            p.drawCard(new HunterCard(0,0,0,false));
        }

        p.drawCard(b);

        hunt.solveEventCard(p, new ArrayList<>());

        // Checking player's food and prestige
        int num = 3;
        assertEquals(num * 2, p.getFood());
        assertEquals(num * (hunt.getEra() + 1), p.getPrestige());
    }
}