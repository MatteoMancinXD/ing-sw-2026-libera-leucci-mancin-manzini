package it.polimi.ingsw.model.events;

import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.RitualEventBonusStarsBuilding;
import it.polimi.ingsw.model.buildings.RitualEventDoublePrestigeBuilding;
import it.polimi.ingsw.model.buildings.RitualEventNoMalusBuilding;
import it.polimi.ingsw.model.characters.ShamanCard;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RitualEventTest {

    @Test
    void RitualEventMultiWinnerTest() {
        RitualEvent ritual = new RitualEvent(1, 1,false);
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            players.add(new Player("Player"+Integer.toString(i)));
        }

        players.get(1).drawCard(new ShamanCard(2, 1, 2, 1));
        players.get(2).drawCard(new ShamanCard(3, 1, 2, 1));

        // Checking 0 food and prestige for each player
        for(Player p : players) {
            assertEquals(0, p.getFood());
            assertEquals(0, p.getPrestige());
        }

        for(Player p : players) {
            ritual.solveEventCard(p, players);
        }

        // Checking Player0 prestige is -3
        assertEquals(-3, players.getFirst().getPrestige());

        // Checking Player1 and Player2 prestige is 5
        assertEquals(5, players.get(1).getPrestige());
        assertEquals(5, players.get(2).getPrestige());
    }

    @Test
    void RitualEventMultiLoserTest() {
        RitualEvent ritual = new RitualEvent(1, 1,false);
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 3; i++) {
            players.add(new Player("Player"+Integer.toString(i)));
        }

        players.getFirst().drawCard(new ShamanCard(2, 1, 2, 1));

        // Checking 0 food and prestige for each player
        for(Player p : players) {
            assertEquals(0, p.getFood());
            assertEquals(0, p.getPrestige());
        }

        for(Player p : players) {
            ritual.solveEventCard(p, players);
        }

        // Checking Player0 prestige is 5
        assertEquals(5, players.getFirst().getPrestige());

        // Checking Player1 and Player2 prestige is -3
        assertEquals(-3, players.get(1).getPrestige());
        assertEquals(-3, players.get(2).getPrestige());
    }

    @Test
    void RitualEventMultiGeneralTest() {
        RitualEvent ritual = new RitualEvent(1, 1,false);
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 4; i++) {
            players.add(new Player("Player"+Integer.toString(i)));
        }

        players.getFirst().drawCard(new ShamanCard(2, 1, 2, 1));
        players.get(1).drawCard(new ShamanCard(3, 1, 2, 1));

        // Checking 0 food and prestige for each player
        for(Player p : players) {
            assertEquals(0, p.getFood());
            assertEquals(0, p.getPrestige());
        }

        for(Player p : players) {
            ritual.solveEventCard(p, players);
        }

        // Checking Player0 and Player1 prestige is 5
        assertEquals(5, players.getFirst().getPrestige());
        assertEquals(5, players.get(1).getPrestige());

        // Checking Player1 and Player2 prestige is -3
        assertEquals(-3, players.get(2).getPrestige());
        assertEquals(-3, players.get(3).getPrestige());
    }

    @Test
    void RitualEventWinAndLoseTest() {
        RitualEvent ritual = new RitualEvent(1, 1,false);
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 2; i++) {
            players.add(new Player("Player"+Integer.toString(i)));
        }

        // Checking 0 food and prestige for each player
        for(Player p : players) {
            assertEquals(0, p.getFood());
            assertEquals(0, p.getPrestige());
        }

        for(Player p : players) {
            ritual.solveEventCard(p, players);
        }

        // Checking Player0 and Player1 prestige is 2
        assertEquals(2, players.getFirst().getPrestige());
        assertEquals(2, players.get(1).getPrestige());
    }

    @Test
    void RitualEventNoMalusTest() {
        RitualEvent ritual = new RitualEvent(1, 1,false);
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 2; i++) {
            players.add(new Player("Player"+Integer.toString(i)));
        }

        players.getFirst().drawCard(new ShamanCard(2, 1, 2, 1));
        players.get(1).drawCard(new RitualEventNoMalusBuilding(3,1,0,0));

        // Checking 0 food and prestige for each player
        for(Player p : players) {
            assertEquals(0, p.getFood());
            assertEquals(0, p.getPrestige());
        }

        for(Player p : players) {
            ritual.solveEventCard(p, players);
        }

        // Checking Player0 prestige is 5
        assertEquals(5, players.getFirst().getPrestige());

        // Checking Player1 prestige is 0
        assertEquals(0, players.get(1).getPrestige());
    }

    @Test
    void RitualEventDoubleBonusTest() {
        RitualEvent ritual = new RitualEvent(1, 1,false);
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 2; i++) {
            players.add(new Player("Player"+Integer.toString(i)));
        }

        players.getFirst().drawCard(new ShamanCard(2, 1, 2, 1));
        players.getFirst().drawCard(new RitualEventDoublePrestigeBuilding(3,1,0,0));

        // Checking 0 food and prestige for each player
        for(Player p : players) {
            assertEquals(0, p.getFood());
            assertEquals(0, p.getPrestige());
        }

        for(Player p : players) {
            ritual.solveEventCard(p, players);
        }

        // Checking Player0 prestige is 10
        assertEquals(10, players.getFirst().getPrestige());

        // Checking Player1 prestige is 0
        assertEquals(-3, players.get(1).getPrestige());
    }

    @Test
    void RitualEventFlatStarsTest() {
        RitualEvent ritual = new RitualEvent(1, 1,false);
        List<Player> players = new ArrayList<>();

        for(int i = 0; i < 2; i++) {
            players.add(new Player("Player"+Integer.toString(i)));
        }

        players.getFirst().drawCard(new ShamanCard(2, 1, 2, 1));
        players.get(1).drawCard(new RitualEventBonusStarsBuilding(3,1,0,0));

        // Checking 0 food and prestige for each player
        for(Player p : players) {
            assertEquals(0, p.getFood());
            assertEquals(0, p.getPrestige());
        }

        for(Player p : players) {
            ritual.solveEventCard(p, players);
        }

        // Checking Player0 prestige is -3
        assertEquals(-3, players.getFirst().getPrestige());

        // Checking Player1 prestige is 5
        assertEquals(5, players.get(1).getPrestige());
    }

}