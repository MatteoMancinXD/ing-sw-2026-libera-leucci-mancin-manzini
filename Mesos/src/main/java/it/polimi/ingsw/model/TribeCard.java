package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.events.HuntEvent;
import it.polimi.ingsw.model.events.PaintingsEvent;
import it.polimi.ingsw.model.events.RitualEvent;
import it.polimi.ingsw.model.events.*;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({

        @JsonSubTypes.Type(value = ArtistCard.class, name = "ARTIST"),
        @JsonSubTypes.Type(value = BuilderCard.class, name = "BUILDER"),
        @JsonSubTypes.Type(value = HarvesterCard.class, name = "HARVESTER"),
        @JsonSubTypes.Type(value = HunterCard.class, name = "HUNTER"),
        @JsonSubTypes.Type(value = InventorCard.class, name = "INVENTOR"),
        @JsonSubTypes.Type(value = ShamanCard.class, name = "SHAMAN"),

        @JsonSubTypes.Type(value = HuntEvent.class, name = "HUNT"),
        @JsonSubTypes.Type(value = PaintingsEvent.class, name = "PAINTINGS"),
        @JsonSubTypes.Type(value = RitualEvent.class, name = "RITUAL"),
        @JsonSubTypes.Type(value = SustenanceEvent.class, name = "SUSTENANCE")
})

public abstract class TribeCard extends Card{
    private int minPlayers;

    public TribeCard(int id, int era, int minPlayers) {
        super(id, era);
        this.minPlayers = minPlayers;
    }
    public TribeCard() {}



    public int getMinPlayers() {
        return minPlayers;
    }
}
