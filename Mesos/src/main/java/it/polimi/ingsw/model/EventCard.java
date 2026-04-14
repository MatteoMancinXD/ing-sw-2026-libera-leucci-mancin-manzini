package it.polimi.ingsw.model;


public abstract class EventCard extends TribeCard{

    public EventCard(int era) {
    super(era,2);
    }
    abstract void solveEventCard(Game game,Player player);  // ex. huntCardEventII.solveEventCard(game1,giacomo)
}
