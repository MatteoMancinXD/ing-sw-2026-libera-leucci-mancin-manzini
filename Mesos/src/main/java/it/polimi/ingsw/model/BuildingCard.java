package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;
import it.polimi.ingsw.model.buildings.ExtraPickBuilding;
import it.polimi.ingsw.model.characters.*;
import it.polimi.ingsw.model.events.HuntEvent;
import it.polimi.ingsw.model.events.PaintingsEvent;
import it.polimi.ingsw.model.events.RitualEvent;
import it.polimi.ingsw.model.events.SustenanceEvent;
import it.polimi.ingsw.model.buildings.*;
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type" // Il campo nel JSON che distingue TUTTO
)
@JsonSubTypes({
        // Buildings
        @JsonSubTypes.Type(value = CardSetForFoodBuilding.class, name = "CardSetForFoodBuilding"),
        @JsonSubTypes.Type(value = ExtraPickBuilding.class, name = "ExtraPickBuilding"),
        @JsonSubTypes.Type(value = HuntEventBuilding.class, name = "HuntEventBuilding"),
        @JsonSubTypes.Type(value = InventorSetForFoodBuilding.class, name = "InventorSetForFoodBuilding"),
        @JsonSubTypes.Type(value = OrderTileBuilding.class, name = "OrderTileBuilding"),
        @JsonSubTypes.Type(value = PaintingsEventBuilding.class, name = "PaintingsEventBuilding"),
        @JsonSubTypes.Type(value = PrestigeDoublingBuilderValueBuilding.class, name="prestigeDoublingBuilderValueBuilding"),
        @JsonSubTypes.Type(value = PrestigeForCharacterBuilding.class, name = "PrestigeForCharacterBuilding"),
        @JsonSubTypes.Type(value = PrestigeForSextetBuilding.class, name = "PrestigeForSextetBuilding"),
        @JsonSubTypes.Type(value = PrestigeGivingBuilding.class, name = "PrestigeGivingBuilding"),
        @JsonSubTypes.Type(value = RitualEventBonusStarsBuilding.class, name = "RitualEventBonusStarsBuilding"),
        @JsonSubTypes.Type(value = RitualEventDoublePrestigeBuilding.class, name = "RitualEventDoublePrestigeBuilding"),
        @JsonSubTypes.Type(value = RitualEventNoMalusBuilding.class, name = "RitualEventNoMalusBuilding"),
        @JsonSubTypes.Type(value = SustenanceBuilding.class, name = "SustenanceBuilding")

})
public abstract class BuildingCard extends Card{
    private int foodCost; //purchase cost
    private int prestigeGain; //prestige gain at the end of the game



    public BuildingCard(int id,int era, int foodCost, int prestigeGain) {
        super(id, era);
        this.foodCost = foodCost;
        this.prestigeGain = prestigeGain;
    }

    protected BuildingCard() {
    }

    public int getBuildingFoodCost() {
        return foodCost;
    }
    public int getBuildingPrestigeGain(){
        return prestigeGain;
    }


    //protected because can be modified just inside BuildindCard subclasses
    protected void setBuildingFoodCost(int foodCost){this.foodCost = foodCost;}
    protected void setBuildingPrestigeGain(int prestigeGain){
        this.prestigeGain = prestigeGain;
    }

    public void assignTo(Player player) {
        player.addBuilding(this);
        player.buyBuilding(this);
    }


    //these methods are made to avoid the usage of "instance of"
    //during x event we check if the building is present inside the deck: for(BuildingCard card : player.getBuildings()){ ...
    //if the building is NOT present a default return value is needed
    //these are default return values

    //SustenanceEvents' buildings
    public int getSustenanceEventFoodBonus(Player p){ return 0;}  //3 building cards
    public int getPaintingsEventFoodBonus(int artists) {return 0;}
    public int getHuntEventFoodBonus(int hunters) { return 0; }

    public boolean getRitualEventNoPrestigeMalus(){return false;}//default is false (you don't own the building)
    public boolean getRitualEventDoublePrestigeBonus(){ return false;} //if the double prestige bonus is not to be used default value is 0
    //public void getRitualEventBonusStars(Player player){} //in onPurchase


    // Hook methods to handle building events
    public void onPurchase(Player player) {}; //used in RitualEventBonusStarsBuilding
    public void onRoundEnd(Player player) {};
    public void onGameEnd(Player player) {};
    //public void onCharacterCardGameEnd(Player player, CharacterCard card) {}; useless
    public void onCharacterCardPurchase(Player player, CharacterCard card) {};
}
