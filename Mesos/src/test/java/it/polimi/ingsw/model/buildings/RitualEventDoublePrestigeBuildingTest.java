package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.ShamanCard;
import it.polimi.ingsw.model.events.RitualEvent;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RitualEventDoublePrestigeBuildingTest {

    @Test
    void ritualEventDoublePrestigeBuildingTest() {
        RitualEvent ritual = new  RitualEvent(0,2,false);
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            players.add(new Player("Player"+i));
        }

        players.getFirst().drawCard(new ShamanCard(0,0,0,3));
        players.get(1).drawCard(new ShamanCard(0,0,0,3));
        players.getFirst().drawCard(new RitualEventDoublePrestigeBuilding(0,0,0,0));

        for(Player p : players) {
            ritual.solveEventCard(p, players);
        }

        // Checking prestige for players
        assertEquals(20, players.getFirst().getPrestige());
        assertEquals(10, players.get(1).getPrestige());
        assertEquals(-5, players.get(2).getPrestige());
    }
}