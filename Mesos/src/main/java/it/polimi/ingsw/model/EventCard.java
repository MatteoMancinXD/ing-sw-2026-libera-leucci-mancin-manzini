package it.polimi.ingsw.model;

public EventCard(int era, int minPlayers) {
    super(era, minPlayers);
}
public abstract class EventCard extends TribeCard{
    abstract void solveEventCard(Player player,Era era);  // ex. huntCardEventII.solveEventCard(giacomo,2)

}