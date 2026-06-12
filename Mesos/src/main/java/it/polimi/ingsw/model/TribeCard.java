package it.polimi.ingsw.model;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.events.*;
import it.polimi.ingsw.model.characters.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // Il campo nel JSON che distingue TUTTO
)
@JsonSubTypes({
        // Personaggi
        @JsonSubTypes.Type(value = ArtistCard.class, name = "ARTIST"),
        @JsonSubTypes.Type(value = BuilderCard.class, name = "BUILDER"),
        @JsonSubTypes.Type(value = HarvesterCard.class, name = "HARVESTER"),
        @JsonSubTypes.Type(value = HunterCard.class, name = "HUNTER"),
        @JsonSubTypes.Type(value = InventorCard.class, name = "INVENTOR"),
        @JsonSubTypes.Type(value = ShamanCard.class, name = "SHAMAN"),
        // Eventi
        @JsonSubTypes.Type(value = HuntEvent.class, name = "HUNT"),
        @JsonSubTypes.Type(value = PaintingsEvent.class, name = "PAINTINGS"),
        @JsonSubTypes.Type(value = RitualEvent.class, name = "RITUAL"),
        @JsonSubTypes.Type(value = SustenanceEvent.class, name = "SUSTENANCE")

})

/**
 * Abstract class representing a tribe card drawn from the main deck.
 * Tribe cards include both {@link CharacterCard} (characters) and {@link EventCard} (events).
 * Each tribe card has a minimum player count that determines whether it is
 * included in the deck for a given game size.
 *
 * @see CharacterCard
 * @see EventCard
 */
public abstract class TribeCard extends Card{
    private int minPlayers;

    public TribeCard(int id, int era, int minPlayers) {
        super(id, era);
        this.minPlayers = minPlayers;
    }

    protected TribeCard() {
    }

    /** @return the minimum number of players required for this card to be in the deck */
    public int getMinPlayers() {
        return minPlayers;
    }

    public boolean isEventCard() {return false;}
}
;