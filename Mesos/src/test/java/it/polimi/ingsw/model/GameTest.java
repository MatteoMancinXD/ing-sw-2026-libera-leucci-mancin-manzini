package it.polimi.ingsw.model;

import it.polimi.ingsw.controller.GameObserver;
import it.polimi.ingsw.model.buildings.ExtraPickBuilding;
import it.polimi.ingsw.model.buildings.PrestigeGivingBuilding;
import it.polimi.ingsw.model.characters.BuilderCard;
import it.polimi.ingsw.model.events.SustenanceEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    private Game game;
    private GameObserver dummyObserver;

    @BeforeEach
    void setup() {
        // Dummy observer per evitare errori di costruttore
        game = new Game(2);
        game.addObserver(dummyObserver);
        game.addPlayer(new Player("Player1"));
        game.addPlayer(new Player("Player2"));
    }

    @Test
    void testGamePhaseEnum() {
        GamePhase[] phases = GamePhase.values();
        assertNotNull(phases);
        assertEquals(GamePhase.PLACEMENT, GamePhase.valueOf("PLACEMENT"));
        assertEquals(GamePhase.RESOLUTION, GamePhase.valueOf("RESOLUTION"));
        assertEquals(GamePhase.EXTRA_PICK, GamePhase.valueOf("EXTRA_PICK"));
    }


    @Test
    void creationTest() {
        Game g = new Game(2);
        assertEquals(0, g.getRound());
        assertEquals(1, g.getEra());
        assertEquals(2, g.getNumPlayers());
        assertTrue(g.getPlayers().isEmpty());
        assertNotNull(g.getBoard());
    }

    @Test
    void basicSettersTest() {
        game.setRound(5);
        assertEquals(5, game.getRound());

        game.setEra(2);
        assertEquals(2, game.getEra());

        Totem t = Totem.YELLOW;
        game.assignTotem("Player1", t);
        assertEquals(t, game.getPlayers().get(0).getTotem());
    }

    @Test
    void addPlayerTest() {
        assertEquals(2, game.getPlayers().size());
        game.addPlayer(new Player("Player3"));
        assertEquals(2, game.getPlayers().size());
    }

    @Test
    void setNumPlayersTest() {
        game.setNumPlayers(3);
        assertEquals(3, game.getNumPlayers());
    }

    @Test
    void setNumPlayersInvalidTest() {
        assertThrows(IllegalArgumentException.class, () -> game.setNumPlayers(1));
        assertThrows(IllegalArgumentException.class, () -> game.setNumPlayers(6));
    }

    @Test
    void startGameTest() {
        game.startGame();
        assertEquals(1, game.getRound());
        assertEquals(GamePhase.PLACEMENT.toString(), game.getCurrentPhase());
        assertNotNull(game.getCurrentPlayer()); // Copertura del getter currentPlayer
    }

    @Test
    void startGameFoodTest() {
        game.startGame();
        int totalFood = 0;
        for (Player p : game.getPlayers()) {
            totalFood += p.getFood();
        }
        assertEquals(5, totalFood); // P1(2) + P2(3) = 5
    }

    // --- TEST SULLA FASE PLACEMENT E RISOLUZIONE ---

    @Test
    void placeTotemOccupiedTest() {
        game.startGame();
        game.placeTotem(0);
        assertThrows(IllegalArgumentException.class, () -> game.placeTotem(0));
    }

    @Test
    void placeTotemPhaseTransitionTest() {
        game.startGame();
        game.placeTotem(0);
        game.placeTotem(1);
        assertEquals(GamePhase.RESOLUTION.toString(), game.getCurrentPhase());
    }

    // --- TEST SULLA VALIDAZIONE DELLA PESCA (Eccezioni di resolveAction) ---

    @Test
    void resolveActionDrawEventThrowsExceptionTest() {
        game.startGame();
        game.placeTotem(2);
        game.placeTotem(3);

        // Forziamo un Evento nella riga superiore all'indice 0
        game.getBoard().getUpperRow().set(0, new SustenanceEvent(1, 1, false));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> game.resolveAction(true, 0));
        assertTrue(ex.getMessage().contains("EVENT CARD"));
    }

    @Test
    void resolveActionNotEnoughFoodForBuildingTest() {
        game.startGame();
        game.placeTotem(2);
        game.placeTotem(3);

        // Azzeriamo il cibo del giocatore corrente
        Player current = game.getCurrentPlayer();
        current.editFood(-current.getFood());

        // Inseriamo un edificio costoso (costo 10)
        BuildingCard expensiveBuilding = new PrestigeGivingBuilding(99, 1, 10, 0);
        game.getBoard().getUpperRow().set(0, expensiveBuilding);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> game.resolveAction(true, 0));
        assertTrue(ex.getMessage().contains("food to buy this building"));
    }

    @Test
    void resolveActionMaxDrawThrowsExceptionTest() {
        game.startGame();
        game.placeTotem(0);
        game.placeTotem(1);

        Tile target = game.getBoard().getTrack().get(0);
        int maxUpper = target.getUpperRow();

        // Facciamo pescare al giocatore tutte le carte consentite dalla sua tile
        for(int i = 0; i < maxUpper; i++) {
            while(game.getBoard().getUpperRow().get(0).isEventCard() || game.getBoard().getUpperRow().get(0).isBuildingCard()) {
                game.getBoard().removeUpper(0);
            }
            game.resolveAction(true, 0);
        }

        assertThrows(IllegalStateException.class, () -> game.resolveAction(true, 0));
    }

    // --- TEST SULLA PROGRESSIONE DEI TURNI E DELLE ERE ---

    @Test
    void nextTurnAndNextEraTest() {
        game.startGame();
        game.getBoard().getUpperRow().clear();
        game.getBoard().getLowerRow().clear();

        game.nextTurn();

        assertEquals(2, game.getRound());
        assertEquals(GamePhase.PLACEMENT.toString(), game.getCurrentPhase());
    }

    // --- TEST SULLA MECCANICA EXTRA PICK ---

    @Test
    void extraPickPhaseActivationTest() {
        game.startGame();
        game.placeTotem(0);
        game.placeTotem(1);

        BuildingCard extraPickCard = new ExtraPickBuilding(80, 1, 0, 0);
        game.getPlayers().get(1).drawCard(extraPickCard);

        game.nextPlayer();
        game.nextPlayer();

        assertEquals(GamePhase.EXTRA_PICK.toString(), game.getCurrentPhase());
    }

    @Test
    void resolveExtraPickThrowsExceptionWrongPhaseTest() {
        game.startGame();
        // Siamo in fase PLACEMENT, chiamare resolveExtraPick deve fallire
        assertThrows(IllegalArgumentException.class, () -> game.resolveExtraPick(0));
    }

    @Test
    void skipExtraPickWrongPhaseTest() {
        game.startGame();
        assertThrows(IllegalStateException.class, () -> game.skipExtraPick());
    }

    // --- TEST DI FINE PARTITA ---

    @Test
    void endGameHigherPrestigeTest() {
        game.startGame();
        game.getPlayers().get(0).editPrestige(100);
        game.getPlayers().get(1).editPrestige(10);
        ArrayList<Player> rankings = game.endGame();
        assertTrue(rankings.contains(game.getPlayers().get(0)) && rankings.get(0).equals(game.getPlayers().get(0)));
        assertTrue(rankings.contains(game.getPlayers().get(1)) && rankings.get(1).equals(game.getPlayers().get(1)));
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

        // Il P2 ottiene 5 punti vittoria aggiuntivi a fine partita
        BuildingCard b = new PrestigeGivingBuilding(99, 1, 0, 5);
        game.getPlayers().get(1).drawCard(b);

        ArrayList<Player> winners = game.endGame();
        assertTrue(winners.contains(game.getPlayers().get(1)));
    }

    @Test
    void endGameBuilderCardBonusTest() {
        game.startGame();
        game.getPlayers().get(0).editPrestige(10);
        game.getPlayers().get(1).editPrestige(10);

        // Aggiungiamo un Builder al P1 che dia 3 punti
        BuilderCard builder = new BuilderCard(1, 1, 0, 0, 3);
        game.getPlayers().get(0).drawCard(builder);

        ArrayList<Player> winners = game.endGame();
        assertTrue(winners.contains(game.getPlayers().get(0)));
    }
}