package it.polimi.ingsw.model.characters;
import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.InputStream;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;

import java.util.stream.Collectors;

public class HunterCardTest {


    private List<TribeCard> allCards;
    private List<HunterCard> hunterCards;

    @BeforeEach
    void setUp() {
        allCards = loadCardsFromJson();

        //just artists
        hunterCards = allCards.stream()
                .filter(c -> c instanceof HunterCard)
                .map(c -> (HunterCard) c)
                .collect(Collectors.toList());
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
    public void testConstructor(){
        HunterCard hunterCard1 = new HunterCard(0,0,2,true);
        HunterCard hunterCard2 = new HunterCard();

        assertFalse(hunterCards.isEmpty());
    }
    @Test
    public void testMethods(){
        Player player = new Player("giacomo");
        HunterCard hunterCard1 = new HunterCard(0,0,2,false);

        hunterCard1.setGetsFood(true);
        assertTrue(hunterCard1.getGetsFood());

        //assignTo
        hunterCard1.assignTo(player);
        assertTrue(player.getHunters().contains(hunterCard1));

    }


}