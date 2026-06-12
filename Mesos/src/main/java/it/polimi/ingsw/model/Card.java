package it.polimi.ingsw.model;

import it.polimi.ingsw.network.snapshots.CardSnapshot;

import java.io.Serializable;
/**
 * Abstract base class for all cards in the game.
 * Every card has a unique identifier and belongs to a specific era (1, 2, or 3).
 * Subclasses include {@link TribeCard} (characters and events) and {@link BuildingCard}.
 *
 * @see TribeCard
 * @see BuildingCard
 */
public abstract class Card implements Serializable {
    private int id;
    private int era;

    public Card(int id, int era) {
        this.id = id;
        this.era = era;
    }


    protected Card() {}

    public void setEra(int era) {
        this.era = era;
    }

    /** @return the era this card belongs to (1, 2, or 3) */
    public int getEra() {
        return era;
    }

    /** @return the unique identifier of this card */
    public int getId() {return id;}

    /**
     * Assigns this card to a player. Each subclass overrides this to add
     * the card to the appropriate collection in {@link Player}.
     * @param player the player receiving this card
     */
    public void assignTo(Player player) {}

    /**
     * Notifies the player's buildings that a new card has been acquired.
     * Used to trigger building effects on card purchase.
     * @param player the player whose buildings are notified
     */
    public void notifyBuildings(Player player) {};

    /** @return the food cost to acquire this card, 0 by default */
    public int getFoodCost() { return 0; }

    /** @return a short text description of this card for CLI display */
    public abstract String getShortString();

    /** @return true if this card is an event card */
    public boolean isEventCard() {
        return false;
    }

    /** @return true if this card is a building card */
    public boolean isBuildingCard() {return false;}

    /** @return true if this card is a sustenance event */
    public boolean isSustenanceEvent() {return false;}

    public CardSnapshot toSnapshot() { return new CardSnapshot(id, getShortString()); }
}
