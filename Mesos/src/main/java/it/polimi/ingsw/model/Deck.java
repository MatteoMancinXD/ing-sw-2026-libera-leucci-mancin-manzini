package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Collections;

public class Deck {
    private ArrayList<Card> cards;

    public Deck(ArrayList<Card> cards) {
        this.cards = cards;
    }

    public Deck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Shuffles the deck segmenting the cards by era, shuffling them, and piling the up
     */
    public void shuffle() {
        ArrayList<Card> era1 = new ArrayList<>();
        ArrayList<Card> era2 = new ArrayList<>();
        ArrayList<Card> era3 = new ArrayList<>();

        for(Card card : cards) {
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

    /**
     * Draws the first card in the deck
     * @return drawn card
     */
    public Card draw() {
        return cards.removeFirst();
    }
}
