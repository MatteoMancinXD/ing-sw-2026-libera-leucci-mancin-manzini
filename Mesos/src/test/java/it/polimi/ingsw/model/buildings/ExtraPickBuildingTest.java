package it.polimi.ingsw.model.buildings;

import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ExtraPickBuildingTest {

    @Test
    void testConstructors() {
        ExtraPickBuilding building = new ExtraPickBuilding(10, 3, 9, 3);
        assertNotNull(building);
        assertEquals(3, building.getEra());
        assertEquals(9, building.getFoodCost());
        assertEquals(3, building.getPrestigeGain());

        assertFalse(building.isExtraPickAvailable());

        ExtraPickBuilding emptyBuilding = new ExtraPickBuilding();
        assertNotNull(emptyBuilding);
    }

    @Test
    void testGrantsExtraPick() {
        ExtraPickBuilding building = new ExtraPickBuilding(1, 3, 9, 3);
        assertTrue(building.grantsExtraPick(), "ExtraPickBuilding should always grant extra pick");
    }

    @Test
    void testStandardBuildingsReturnFalse() {
        SustenanceBuilding normalCard = new SustenanceBuilding(1, 0, 0, 0);
        assertFalse(normalCard.grantsExtraPick(), "Other buildings should not grant extra pick");
    }

    @Test
    void testOnRoundEndRefreshesExtraPick() {
        ExtraPickBuilding building = new ExtraPickBuilding(1, 3, 9, 3);
        Player dummyPlayer = new Player("TestPlayer");
        assertFalse(building.isExtraPickAvailable());
        //simulazione fine round
        building.onRoundEnd(dummyPlayer);
        assertTrue(building.isExtraPickAvailable(), "onRoundEnd should set extraPickAvailable to true");
    }

    @Test
    void testUseExtraPick() {
        ExtraPickBuilding building = new ExtraPickBuilding(1, 3, 9, 3);

        building.setExtraPickAvailable(true);
        assertTrue(building.isExtraPickAvailable());

        building.useExtraPick();
        assertFalse(building.isExtraPickAvailable(), "useExtraPick should consume the availability");
    }

    @Test
    void testGetShortString() {
        ExtraPickBuilding building = new ExtraPickBuilding(1, 3, 9, 5);
        String description = building.getShortString();

        assertNotNull(description);
        assertTrue(description.contains("cost=9"));
        assertTrue(description.contains("pp=5"));
        assertTrue(description.contains("pick 1 extra card"));
    }
}
