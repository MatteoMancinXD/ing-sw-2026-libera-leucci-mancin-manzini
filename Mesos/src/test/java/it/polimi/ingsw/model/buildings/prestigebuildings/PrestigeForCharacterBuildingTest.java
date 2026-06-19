package it.polimi.ingsw.model.buildings.prestigebuildings;

import it.polimi.ingsw.model.buildings.PrestigeForCharacterBuilding;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PrestigeForCharacterBuildingTest {

    @Test
    void testConstructors() {
        PrestigeForCharacterBuilding building = new PrestigeForCharacterBuilding(10, 2, 5, 3, 2);

        assertNotNull(building);
        assertEquals(2, building.getEra());
        assertEquals(5, building.getFoodCost());
        assertEquals(3, building.getPrestigeGain());

        PrestigeForCharacterBuilding emptyBuilding = new PrestigeForCharacterBuilding();
        assertNotNull(emptyBuilding);
    }

    @Test
    void testSetBonusPrestigeAndShortString() {
        PrestigeForCharacterBuilding building = new PrestigeForCharacterBuilding(1, 1, 4, 2, 1);

        String initialString = building.getShortString();
        assertNotNull(initialString);
        assertTrue(initialString.contains("cost=4"));
        assertTrue(initialString.contains("pp=2"));
        assertTrue(initialString.contains("+1 pp per character"));

        building.setBonusPrestige(5);

        String updatedString = building.getShortString();
        assertTrue(updatedString.contains("+5 pp per character"));
    }
}