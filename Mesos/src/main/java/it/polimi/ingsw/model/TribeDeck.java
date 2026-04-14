package it.polimi.ingsw.model;
import java.util.ArrayList;
import java.util.Collections;

public class TribeDeck {
    private ArrayList<TribeCard> cards;

    /**
     * @param cards to be taken from an archive of all TribeCards
     * @param numPlayers number of players in game
     */
    public TribeDeck(ArrayList<TribeCard> cards, int numPlayers) {
        this.cards = new ArrayList<>();

        for(TribeCard card : cards) {
            if(card.getMinPlayers() <= numPlayers)
                this.cards.add(card);
        }
    }

    public TribeDeck() {
        this.cards = new ArrayList<>();
    }

    /**
     * Shuffles the deck segmenting the cards by era, shuffling them, and piling the up
     */
    public void shuffle() {
        ArrayList<TribeCard> era1 = new ArrayList<>();
        ArrayList<TribeCard> era2 = new ArrayList<>();
        ArrayList<TribeCard> era3 = new ArrayList<>();

        for(TribeCard card : cards) {
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
    public TribeCard draw() {
        return cards.removeFirst();
    }
}
