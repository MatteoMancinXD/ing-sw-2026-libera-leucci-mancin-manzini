package it.polimi.ingsw.model;

import java.util.*;

//To be completed
public class BuildingDeck {
    private Map<Integer,List<Integer>> buildingCardsForPlayers = Map.ofEntries(
            Map.entry(2, Arrays.asList(1,2,3)),
            Map.entry(3, Arrays.asList(2,2,4)),
            Map.entry(4, Arrays.asList(2,3,4)),
            Map.entry(5, Arrays.asList(2,3,5))
    );
    private ArrayList<BuildingCard> cards;

    public BuildingDeck(ArrayList<BuildingCard> cards, int numPlayers) {
        this.cards = new ArrayList<>();

        ArrayList<ArrayList<BuildingCard>> eras = new ArrayList<>();
        eras.add(new ArrayList<>());
        eras.add(new ArrayList<>());
        eras.add(new ArrayList<>());

        for(BuildingCard card : cards) {
            int era = card.getEra();
            eras.get(era - 1).add(card);
        }

        for(int i = 0; i < 3; i++)
            Collections.shuffle(eras.get(i));

        for(int era = 0; era < 3; era++) {
            for(int j = 0; j < buildingCardsForPlayers.get(numPlayers).get(era); j++) {
                this.cards.add(eras.get(era).get(j));
            }
        }
    }

    public BuildingDeck() {
        this.cards = new ArrayList<>();
    }

    public void shuffle() {
        ArrayList<BuildingCard> era1 = new ArrayList<>();
        ArrayList<BuildingCard> era2 = new ArrayList<>();
        ArrayList<BuildingCard> era3 = new ArrayList<>();

        for(BuildingCard card : cards) {
            switch(card.getEra()) {
                case 1:
                    era1.add(card);
                    break;

                case 2:
                    era2.add(card);
                    break;

                case 3:
                    era3.add(card);
                    break;
            }
        }

        Collections.shuffle(era1);
        Collections.shuffle(era2);
        Collections.shuffle(era3);

        cards.clear();
        cards.addAll(era1);
        cards.addAll(era2);
        cards.addAll(era3);
    }

    public BuildingCard draw() {
        return cards.removeFirst();
    }

    public ArrayList<BuildingCard> getBuildingsCards() {
        return cards;
    }

    public Map<Integer,List<Integer>> getBuildingCardsForPlayers() {return  buildingCardsForPlayers;}
}
