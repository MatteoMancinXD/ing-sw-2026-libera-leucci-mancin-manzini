package it.polimi.ingsw.model.buildings;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class ExtraPickBuildingTest {

    //Il test vero è proprio della carta pescata in più si dovrà testare in game...

    @Test
    void testGrantsExtraPick() {
        ExtraPickBuilding testBuild = new ExtraPickBuilding(1, 0, 0, 0);
        assertTrue(testBuild.grantsExtraPick(), "Only this building should grant extra pick");
    }
    @Test
    void testStandardBuildingsReturnFalse() {
        SustenanceBuilding normalCard = new SustenanceBuilding(1, 0 , 0 , 0);
        assertFalse(normalCard.grantsExtraPick(), "Other buildings should not grant extra pick");
    }

}
