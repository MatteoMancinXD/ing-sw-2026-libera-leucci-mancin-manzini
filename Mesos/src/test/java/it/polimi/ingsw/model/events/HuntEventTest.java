package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.HuntEventBuilding;
import it.polimi.ingsw.model.characters.HunterCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HuntEventTest {

    @Test
    void HuntEventTest() {
        HuntEvent hunt = new HuntEvent(1, 2, false);
        Player p1 =  new Player("Player1");
        Player p2 = new Player("Player2");

        List<Player> players = new ArrayList<>();
        players.add(p1);
        players.add(p2);

        HunterCard hunter1 = new HunterCard(2, 1, 2, false);
        HunterCard hunter2 = new HunterCard(3, 1, 2,false);
        HunterCard hunter3 = new HunterCard(4, 1, 2,false);

        HuntEventBuilding hb = new HuntEventBuilding(5, 1, 0, 0);

        p1.drawCard(hunter1);
        p2.drawCard(hunter2);
        p2.drawCard(hunter3);
        p2.drawCard(hb);

        // Checking default player stats
        assertEquals(0, p1.getFood());
        assertEquals(0, p1.getPrestige());
        assertEquals(0, p2.getFood());
        assertEquals(0, p2.getPrestige());

        for(Player p : players) {
            hunt.solveEventCard(p, players);
        }

        // Checking food and prestige for player 1
        int num1 = p1.getHunters().size();
        assertEquals(num1, p1.getFood());
        assertEquals(num1 * hunt.getEra(), p1.getPrestige());

        // Checking food and prestige for player 2
        int num2 = p2.getHunters().size();
        assertEquals(num2 * 2, p2.getFood());
        assertEquals(num2 * (hunt.getEra() + 1), p2.getPrestige());
    }
}