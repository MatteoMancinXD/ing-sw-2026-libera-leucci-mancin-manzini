package it.polimi.ingsw.model;

/**
 * Enumerates the phases of a game round.
 * <ul>
 *   <li>{@link #PLACEMENT} - players choose which track tile to place their totem on</li>
 *   <li>{@link #RESOLUTION} - players draw cards from the upper and lower rows</li>
 *   <li>{@link #EXTRA_PICK} - bonus draw phase granted by the Extra Pick building</li>
 * </ul>
 */
public enum GamePhase {
    PLACEMENT,        //Fase in cui i giocatori scelgono dove posizionarsi
    RESOLUTION,        //Fase in cui i giocatori prendono le carte della righe sopra e sotto
    EXTRA_PICK         //Fase adibita al solo building Extra Pick
}