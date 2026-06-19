package it.polimi.ingsw.model;

import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;
import it.polimi.ingsw.model.buildings.InventorSetForFoodBuilding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CharacterCardTest {

    /**
     * Classe finta per poter istanziare e testare la logica della classe astratta CharacterCard.
     */
    private static class DummyCharacterCard extends CharacterCard {
        public DummyCharacterCard(int id, int era, int minPlayers) {
            super(id, era, minPlayers);
        }

        public DummyCharacterCard() {
            super();
        }

        @Override
        public String getShortString() {
            return "Dummy Character";
        }
    }

    /**
     * Finto edificio per verificare che la notifica arrivi correttamente.
     */
    private static class DummyBuilding extends BuildingCard {
        public boolean wasNotified = false;

        public DummyBuilding() {
            super(99, 1, 0, 0); // Parametri finti: id, era, cost, prestige
        }

        @Override
        public void onCharacterCardPurchase(Player p, CharacterCard c) {
            this.wasNotified = true;
        }

        @Override
        public String getShortString() { return "Dummy"; }
    }

    @Test
    void testConstructors() {
        DummyCharacterCard card1 = new DummyCharacterCard(10, 2, 3);
        assertNotNull(card1);

        DummyCharacterCard card2 = new DummyCharacterCard();
        assertNotNull(card2);
    }

    @Test
    void testNotifyAllBuildings() {
        Player player = new Player("TestPlayer");

        DummyBuilding building1 = new DummyBuilding();
        DummyBuilding building2 = new DummyBuilding();
        player.addBuilding(building1);
        player.addBuilding(building2);

        assertFalse(building1.wasNotified);
        assertFalse(building2.wasNotified);

        DummyCharacterCard card = new DummyCharacterCard(1, 1, 2);
        card.notifyBuildings(player);

        assertTrue(building1.wasNotified);
        assertTrue(building2.wasNotified);
    }

    @Test
    void testEmptyDefaultMethods() {
        DummyCharacterCard card = new DummyCharacterCard(1, 1, 2);

        assertDoesNotThrow(() -> {
            card.registerForCardSet(null);
            card.registerInvention(null);
            card.getShortString();
        });
    }
    @Test
    void testCharacterTypeEnumCoverage() {
        // Test per coprire l'enum associato alle carte
        Character[] values = Character.values();
        assertNotNull(values);
        assertTrue(values.length > 0);

        Character type = Character.valueOf("ARTIST"); // Usa un valore reale
        assertEquals(Character.ARTIST, type);
    }
}