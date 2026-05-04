package it.polimi.ingsw.model;
import it.polimi.ingsw.controller.GameController;
import it.polimi.ingsw.controller.GameObserver;
import it.polimi.ingsw.model.buildings.PrestigeGivingBuilding;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.*;
import it.polimi.ingsw.model.Tile;
public class GameTest {

    private Game game;

    @BeforeEach
    void setup() {
        GameObserver observer = new GameObserver() {
            @Override
            public void onEventResolution(EventCard event) {

            }
        };

        game = new Game(2, observer);
        game.addPlayer(new Player("Player1"));
        game.addPlayer(new Player("Player2")); }

    @Test
    void creationTest() {
        GameObserver observer = new GameObserver() {
            @Override
            public void onEventResolution(EventCard event) {

            }
        };

        Game g = new Game(2, observer);
        assertEquals(0, g.getRound());
        assertEquals(1, g.getEra());
        assertEquals(2, g.getNumPlayers());
        assertTrue(g.getPlayers().isEmpty());    }

    @Test
    void addPlayerTest() {        assertEquals(2, game.getPlayers().size());
        game.addPlayer(new Player("Player3"));
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    void setNumPlayersTest() {
        game.setNumPlayers(3);
          assertEquals(3, game.getNumPlayers());    }

    @Test
    void setNumPlayersInvalidTest() {
        assertThrows(IllegalArgumentException.class, () -> { game.setNumPlayers(1); });
        assertThrows(IllegalArgumentException.class, () -> { game.setNumPlayers(6); });
    }
    @Test
    void startGameTest() {
        game.startGame();
        assertEquals(1, game.getRound());
        assertEquals(GamePhase.PLACEMENT, game.getCurrentPhase());    }
    @Test
    void startGameFoodTest() {
        game.startGame();
        int totalFood = 0;
       for (Player p : game.getPlayers()) {
            totalFood = totalFood + p.getFood();
        }
         assertEquals(5, totalFood);
    }

    @Test
    void placeTotemOccupiedTest() {
         game.startGame();
        game.placeTotem(0);
        assertThrows(IllegalArgumentException.class, () -> { game.placeTotem(0);  });
    }

    @Test
    void placeTotemPhaseTransitionTest() {
        game.startGame();
         game.placeTotem(0);
        game.placeTotem(1);
        assertEquals(GamePhase.RESOLUTION, game.getCurrentPhase());
    }

    @Test
    void resolveActionUpperRowOverDrawTest() {
        game.startGame();
         game.placeTotem(0);
        game.placeTotem(1);
        assertThrows(IllegalArgumentException.class, () -> {  game.resolveAction(true, 0);   });
    }

    @Test
    void resolveActionLowerRowOverDrawTest() {
          game.startGame();
        game.placeTotem(1);
         game.placeTotem(0);
        assertThrows(IllegalArgumentException.class, () -> {game.resolveAction(false, 0); });
    }

    @Test
    void roundIncrementTest() {
        game.startGame();
        int roundBefore = game.getRound();
        for (Player p : game.getPlayers()) {p.editFood(20);}
        completeTurn();
        assertEquals(roundBefore + 1, game.getRound());    }

    @Test
    void phaseAfterRoundTest() {
        game.startGame();
        for (Player p : game.getPlayers()) p.editFood(20);
        completeTurn();
         assertEquals(GamePhase.PLACEMENT, game.getCurrentPhase());
    }

    @Test
    void skipExtraPickWrongPhaseTest() {
        game.startGame();
        assertThrows(IllegalStateException.class, () -> {            game.skipExtraPick();        });
    }

    @Test
    void endGameHigherPrestigeTest() {
        game.startGame();
        game.getPlayers().get(0).editPrestige(100);
        game.getPlayers().get(1).editPrestige(10);
         ArrayList<Player> winners = game.endGame();
        assertTrue(winners.contains(game.getPlayers().get(0)));
    }

      @Test
    void endGameTiebreakerFoodTest() {
        game.startGame();
       game.getPlayers().get(0).editPrestige(50);
        game.getPlayers().get(1).editPrestige(50);
        game.getPlayers().get(0).setFood(3);
        game.getPlayers().get(1).setFood(10);
         ArrayList<Player> winners = game.endGame();
        assertTrue(winners.contains(game.getPlayers().get(1)));
    }

    @Test
    void endGameSharedVictoryTest() {
        game.startGame();
       game.getPlayers().get(0).editPrestige(50);
        game.getPlayers().get(1).editPrestige(50);
         game.getPlayers().get(0).setFood(5);
        game.getPlayers().get(1).setFood(5);
        ArrayList<Player> winners = game.endGame();
        assertTrue(winners.containsAll(game.getPlayers()));
    }

     @Test
    void endGamePrestigeGivingBuildingTest() {
        game.startGame();
         game.getPlayers().get(0).editPrestige(10);
        game.getPlayers().get(1).editPrestige(10);
        BuildingCard b = new PrestigeGivingBuilding(99, 1, 0, 0);
        game.getPlayers().get(1).drawCard(b);
          ArrayList<Player> winners = game.endGame();
        assertTrue(winners.contains(game.getPlayers().get(1)));
    }

    private void completeTurn() {
        game.placeTotem(0);
        game.placeTotem(1);
         Tile tileB = game.getBoard().getTrack().get(0);
        for (int i = 0; i < tileB.getUpperRow(); i++) game.resolveAction(true, 0);
          for (int i = 0; i < tileB.getLowerRow(); i++) game.resolveAction(false, 0);
        Tile tileC = game.getBoard().getTrack().get(1);
       for (int i = 0; i < tileC.getUpperRow(); i++) game.resolveAction(true, 0);
        for (int i = 0; i < tileC.getLowerRow(); i++) game.resolveAction(false, 0);  }
}