package it.polimi.ingsw.model;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


public class BoardTest {

    private Board board;

    class DummyCard extends Card {
        public DummyCard() {this.era = 1;}
        @Override public void assignTo(Player p) {}
    }

    class DummyBuildingCard extends BuildingCard {
        public DummyBuildingCard(int era) {this.era = era;}
        @Override public int getBuildingPrestigeGain() {return 0;}
    }

    @BeforeEach
    public void setup() {
        board = new Board(3);
    }

    @Test      //per 3 player
    void testSetupForThreePlayers() {
        assertEquals(5, board.getTrack().size(), "3 player -> 5 tile track");
        assertEquals('B', board.getTrack().get(0).getLetter(), "1st tile should be B");
    }

    @Test      //per 5 player
    void testSetupForFivePlayers() {
        Board board5 = new Board(5);
        assertEquals(7, board5.getTrack().size(), "5 player -> 7 tile track");
        assertEquals('A', board5.getTrack().get(0).getLetter(), "1st tile should be A");
    }

    @Test
    void testShiftRow() {
        board.getUpperRow().add(new DummyCard());
        board.getUpperRow().add(new DummyCard());
        board.getUpperRow().add(new DummyCard());

    }

}
