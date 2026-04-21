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

public class BuilderCardTest {


    private List<TribeCard> allCards;
    private List<BuilderCard> builderCards;

    @BeforeEach
    void setUp() {
        allCards = loadCardsFromJson();

        //just artists
        builderCards = allCards.stream()
                .filter(c -> c instanceof BuilderCard)
                .map(c -> (BuilderCard) c)
                .collect(Collectors.toList());
    }

    private List<TribeCard> loadCardsFromJson() { //method in game
        ObjectMapper mapper = new ObjectMapper();
        List<TribeCard> cards = new ArrayList<>();
        try {
            InputStream is = getClass().getResourceAsStream("/json/cardsInfo.json");
            TypeReference<Map<String, List<TribeCard>>> typeRef = new TypeReference<>() {};
            Map<String, List<TribeCard>> data = mapper.readValue(is, typeRef);

            for (Map.Entry<String, List<TribeCard>> entry : data.entrySet()) {
                int eraNumber = Integer.parseInt(entry.getKey().substring(3));
                for(TribeCard card : entry.getValue()) {
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
        BuilderCard builderCard1 = new BuilderCard(0,0,2,Character.BUILDER,3,3);

        assertFalse(builderCards.isEmpty());
    }

    @Test void testMethods(){
        BuilderCard builderCard1 = new BuilderCard(0,0,2,Character.BUILDER,3,3);

        builderCard1.setDiscount(4);
        assertEquals(4,builderCard1.getDiscount());

        builderCard1.setPps(4);
        assertEquals(4,builderCard1.getPps());

        //assignTo(player)
    }







}
