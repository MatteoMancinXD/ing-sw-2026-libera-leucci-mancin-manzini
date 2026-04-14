package it.polimi.ingsw.model;

public abstract class BuildingCard extends Card{
    private int foodCost; //purchase cost
    private int prestigeGain; //prestige gain at the end of the game



    public int getBuildingFoodCost() {
        return foodCost;
    }
    public int getBuildingPrestigeGain(){
        return prestigeGain;
    }

    //protected because can be modified just inside BuildindCard subclasses
    protected void setBuildingFoodCost(int foodCost){

        this.foodCost = foodCost;
    }
    protected void setBuildingPrestigeGain(int prestigeGain){
        this.prestigeGain = prestigeGain;
    }

}
