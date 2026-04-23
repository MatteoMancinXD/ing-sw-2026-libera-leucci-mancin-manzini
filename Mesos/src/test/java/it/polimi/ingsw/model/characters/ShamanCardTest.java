package it.polimi.ingsw.model.characters;

import it.polimi.ingsw.model.*;
import it.polimi.ingsw.model.Character;
import it.polimi.ingsw.model.Player;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;
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

public class ShamanCardTest {


    private List<TribeCard> allCards;
    private List<ShamanCard> shamanCards;

    @BeforeEach
    void setUp() {
        allCards = loadCardsFromJson();

        //just artists
        shamanCards = allCards.stream()
                .filter(c -> c instanceof ShamanCard)
                .map(c -> (ShamanCard) c)
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
        ShamanCard shamanCard1 = new ShamanCard(0,1,2,1);
        ShamanCard shamanCard2 = new ShamanCard();

        assertFalse(shamanCards.isEmpty());
    }

    @Test
    public void testMethods(){
        Player player = new Player("giacomo");
        ShamanCard shamanCard1 = new ShamanCard(0,1,2,1);

        shamanCard1.setStars(3);
        assertEquals(3,shamanCard1.getStars());

        //assignTo
        shamanCard1.assignTo(player);
        assertTrue(player.getShamans().contains(shamanCard1));

    }

    @Test
    public void testRegisterForCardSet(){
        ShamanCard shamanCard6 = new  ShamanCard(1,1,2,1);
        CardSetForFoodBuilding newSpecificBuildingCard = new CardSetForFoodBuilding();

        shamanCard6.registerForCardSet(newSpecificBuildingCard);
    }

    @Test
    public void testJSON(){
        for(ShamanCard card: shamanCards){
            assertTrue(card.getStars()>=0 && card.getStars()<=3);

            assertFalse(card.getStars()<0 && card.getStars()>3);
        }
    }









}
