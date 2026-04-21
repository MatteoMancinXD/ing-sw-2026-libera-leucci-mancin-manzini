package it.polimi.ingsw.model;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.characters.ArtistCard;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.Card;


import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Collectors;

public class TribeCardTest {
    private List<TribeCard> allCards;

    @BeforeEach
    void setUp() {
        allCards = loadCardsFromJson();

    }

    private List<TribeCard> loadCardsFromJson() { //method in game
        ObjectMapper mapper = new ObjectMapper();
        List<TribeCard> cards = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/json/cardsInfo.json");
            TypeReference<Map<String, List<TribeCard>>> typeRef = new TypeReference<>() {
            };
            Map<String, List<TribeCard>> data = mapper.readValue(is, typeRef);

            for (Map.Entry<String, List<TribeCard>> entry : data.entrySet()) {
                int eraNumber = Integer.parseInt(entry.getKey().substring(3));
                for (TribeCard card : entry.getValue()) {
                    card.setEra(eraNumber);
                    cards.add(card);
                }
            }
        } catch (Exception e) {
            fail("Impossibile caricare il file JSON: " + e.getMessage());
        }
        return cards;
    }





    @Test
    public void testAllJSON() {
        int id, numPl, era;
        Player player;


        for (TribeCard card : allCards) {

            assertTrue(card.getId() > 0 && card.getId() < 97);
            assertTrue(card.getEra() >= 0 && card.getEra() <= 3);
            assertTrue(card.getMinPlayers() >= 2 && card.getMinPlayers() <= 6);

            assertFalse(card.getId() == 0 && card.getId() >= 97);
            assertFalse(card.getEra() < 0 && card.getEra() > 3);
            assertFalse(card.getMinPlayers() < 2 && card.getMinPlayers() > 6);


        }

    }

}