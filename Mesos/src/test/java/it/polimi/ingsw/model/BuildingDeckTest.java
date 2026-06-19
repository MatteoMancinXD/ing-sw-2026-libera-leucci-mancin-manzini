package it.polimi.ingsw.model;

import it.polimi.ingsw.model.buildings.PrestigeGivingBuilding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Map;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class BuildingDeckTest {

    private ArrayList<BuildingCard> dummyCards;

    @BeforeEach
    void setup() {
        dummyCards = new ArrayList<>();
        //  carte finte per l'Era 1
        dummyCards.add(new PrestigeGivingBuilding(1, 1, 0, 0));
        dummyCards.add(new PrestigeGivingBuilding(2, 1, 0, 0));

        //  carte finte per l'Era 2
        dummyCards.add(new PrestigeGivingBuilding(3, 2, 0, 0));
        dummyCards.add(new PrestigeGivingBuilding(4, 2, 0, 0));
        dummyCards.add(new PrestigeGivingBuilding(5, 2, 0, 0));

        //  carte finte per l'Era 3
        dummyCards.add(new PrestigeGivingBuilding(6, 3, 0, 0));
        dummyCards.add(new PrestigeGivingBuilding(7, 3, 0, 0));
        dummyCards.add(new PrestigeGivingBuilding(8, 3, 0, 0));
        dummyCards.add(new PrestigeGivingBuilding(9, 3, 0, 0));
    }

    @Test
    void emptyConstructorTest() {
        BuildingDeck emptyDeck = new BuildingDeck();
        assertNotNull(emptyDeck.getBuildingsCards());
        assertTrue(emptyDeck.getBuildingsCards().isEmpty());
    }

    @Test
    void parameterizedConstructorTest() {
        // Test per 2 giocatori (si aspetta 1 carta Era1, 2 carte Era2, 3 carte Era3 = 6 totali)
        BuildingDeck deck = new BuildingDeck(dummyCards, 2);

        assertEquals(6, deck.getBuildingsCards().size());

        // Verifico che le carte siano in ordine di Era (la mappa era 1-2-3 per 2 player)
        assertEquals(1, deck.getBuildingsCards().get(0).getEra()); // Prima carta deve essere Era 1
        assertEquals(3, deck.getBuildingsCards().get(5).getEra()); // Ultima carta deve essere Era 3
    }

    @Test
    void shuffleTest() {
        BuildingDeck deck = new BuildingDeck(dummyCards, 2);

        deck.shuffle();

        assertEquals(6, deck.getBuildingsCards().size());

        assertEquals(1, deck.getBuildingsCards().get(0).getEra());
        assertEquals(2, deck.getBuildingsCards().get(1).getEra());
        assertEquals(3, deck.getBuildingsCards().get(5).getEra());
    }

    @Test
    void drawTest() {
        BuildingDeck deck = new BuildingDeck(dummyCards, 2);
        int initialSize = deck.getBuildingsCards().size(); // 6

        BuildingCard drawn = deck.draw();

        assertNotNull(drawn);
        assertEquals(initialSize - 1, deck.getBuildingsCards().size());
    }

    @Test
    void getBuildingCardsForPlayersTest() {
        BuildingDeck deck = new BuildingDeck();
        Map<Integer, List<Integer>> map = deck.getBuildingCardsForPlayers();

        assertNotNull(map);

        List<Integer> reqFor4Players = map.get(4);
        assertEquals(2, reqFor4Players.get(0));
        assertEquals(3, reqFor4Players.get(1));
        assertEquals(4, reqFor4Players.get(2));
    }
}
