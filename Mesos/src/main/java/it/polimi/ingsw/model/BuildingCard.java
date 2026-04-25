package it.polimi.ingsw.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import it.polimi.ingsw.model.buildings.CardSetForFoodBuilding;
import it.polimi.ingsw.model.buildings.ExtraPickBuilding;
import it.polimi.ingsw.model.buildings.*;
import it.polimi.ingsw.model.buildings.prestigebuildings.*;
import it.polimi.ingsw.model.buildings.sustenancebuildings.*;

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
        @JsonSubTypes.Type(value = PrestigeDoublingBuilderValueBuilding.class, name="PrestigeDoublingBuilderValueBuilding"),
        //PRESTIGE BUILDINGS:
        @JsonSubTypes.Type(value = PrestigeForArtistsBuilding.class, name = "PrestigeForArtistsBuilding"),
        @JsonSubTypes.Type(value = PrestigeForBuildersBuilding.class, name = "PrestigeForBuildersBuilding"),
        @JsonSubTypes.Type(value = PrestigeForHarvestersBuilding.class, name = "PrestigeForHarvestersBuilding"),
        @JsonSubTypes.Type(value = PrestigeForHuntersBuilding.class, name="PrestigeForHuntersBuilding"),
        @JsonSubTypes.Type(value = PrestigeForInventorsBuilding.class, name = "PrestigeForInventorsBuilding"),
        @JsonSubTypes.Type(value = PrestigeForShamansBuilding.class, name = "PrestigeForShamansBuilding"),

        @JsonSubTypes.Type(value = CardSetForPrestigeBuilding.class, name = "CardSetForPrestigeBuilding"),
        @JsonSubTypes.Type(value = PrestigeGivingBuilding.class, name = "PrestigeGivingBuilding"),
        @JsonSubTypes.Type(value = RitualEventBonusStarsBuilding.class, name = "RitualEventBonusStarsBuilding"),
        @JsonSubTypes.Type(value = RitualEventDoublePrestigeBuilding.class, name = "RitualEventDoublePrestigeBuilding"),
        @JsonSubTypes.Type(value = RitualEventNoMalusBuilding.class, name = "RitualEventNoMalusBuilding"),
        @JsonSubTypes.Type(value = SustenanceForArtistsBuilding.class, name = "SustenanceForArtistsBuilding"),
        @JsonSubTypes.Type(value = SustenanceForHarvestersBuilding.class, name = "SustenanceForHarvestersBuilding"),
        @JsonSubTypes.Type(value = SustenanceForInventorsBuilding.class, name = "SustenanceForInventorsBuilding")

})
public abstract class BuildingCard extends Card{
    private int foodCost; //purchase cost
    private int prestigeGain; //prestige gain at the end of the game



    public BuildingCard(int id,int era, int foodCost, int prestigeGain) {
        super(id, era);
        this.foodCost = foodCost;
        this.prestigeGain = prestigeGain;
    }

    public BuildingCard() {}

    public void setFoodCost (int foodCost) {this.foodCost = foodCost;}
    public void setPrestigeGain(int prestigeGain) {this.prestigeGain = prestigeGain;}

    @Override
    public int getFoodCost() {
        return foodCost;
    }
    public int getPrestigeGain(){
        return prestigeGain;
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
    public int getSustenanceEventFoodBonus(Player p){ return 0; }

    public int getPaintingsEventFoodBonus(int artists) {return 0;}
    public int getHuntEventFoodBonus(int hunters) { return 0; }

    public boolean getRitualEventNoPrestigeMalus(){ return false; }//default is false (you don't own the building)
    public int getRitualEventDoublePrestigeBonus(){ return 1; } //if the double prestige bonus is not to be used default value is 0
    //public void getRitualEventBonusStars(Player player){} //in onPurchase


    // Hook methods to handle building events
    public void onPurchase(Player player) {}     //used in RitualEventBonusStarsBuilding
    public void onRoundEnd(Player player) {}
    public void onGameEnd(Player player) {}
    //public void onCharacterCardGameEnd(Player player, CharacterCard card) {}; useless
    public void onCharacterCardPurchase(Player player, CharacterCard card) {}

    public boolean grantsExtraPick() {return false;}
    public void onOrderTilePlacement(Player player, int position, OrderTile order) {}
}
