package it.polimi.ingsw.model;


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


    //these methods are made to avoid the usage of "instance of"
    //during x event we check if the building is present inside the deck: for(BuildingCard card : player.getBuildings()){ ...
    //if the building is NOT present a default return value is needed
    //these are default return values

    //SustenanceEvents' buildings
    public int getSustenanceEventFoodBonus(Player p){ return 0;}  //3 building cards
    public int getPaintingsEventFoodBonus(int artists) {return 0;}
    public int getHuntEventFoodBonus(int hunters) { return 0; }

    public boolean getRitualEventNoPrestigeMalus(){return false;}//default is false (you don't own the building)
    public int getRitualEventDoublePrestigeBonus(){ return 1;} //if the double prestige bonus is not to be used default value is 0
    //public void getRitualEventBonusStars(Player player){} //in onPurchase


    // Hook methods to handle building events
    public void onPurchase(Player player) {}; //used in RitualEventBonusStarsBuilding
    public void onRoundEnd(Player player) {};
    public void onGameEnd(Player player) {};
    //public void onCharacterCardGameEnd(Player player, CharacterCard card) {}; useless
    public void onCharacterCardPurchase(Player player, CharacterCard card) {};
}
