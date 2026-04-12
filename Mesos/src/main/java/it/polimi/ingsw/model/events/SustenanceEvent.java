/*  //inside class Player
 *  private ArrayList<Card> playerDeck;
 *  private int harvest; //VA SETTATO A ZERO AD OGNI PARTITA E SERVE AD OGNI PLAYER + VA AUMENTATO AD OGNI HARVESTER CHE VIENE DRAWATO
 *  //...
 *  private int buildingsFoodDiscount; //in Player  VA SETTATO A ZERO AD OGNI PARTITA E SERVE AD OGNI PLAYER
 *  //...
 *
 *
 * void editBuildingFoodDiscount(int discount){
 *      this.buildingFoodDiscount = buildingFoodDiscount + discount;
 * }
 * int getBuildingFoodDiscount(){
 *      return buildingFoodDiscount;
 * }
 * void editHarvest(int i){
 *      this.harvest = harvest + i;
 * }
 * int getHarvest(){
 *      return harvest;
 * }
 *
 * boolean CardInDeck(Card checkCard){ //use to check the presence or of a Building (BuildingCards are unique)
 *     for(Card card : playerDeck){
 *          if(card instanceof checkCard){
 *              return true;
 *       }
 *      return false;
 * }
 *
 *
 *
 * //IMPORTANTE  deve returnare le carte possedute dal player in quel momento
 *
 * public ArrayList<Card> getPlayerCards(){
 *
 *      //... BO ...
 *
 *      ArrayList<Card> playerDeck = cards; //bo ...
 *      return playerDeck;
 *
 *      //gemini dice di farla synchronized giustamente
 *      //di farne una copia per evitare .clear() che ti distruggano il mazzo
 *      //va unita al controller
 * }
 *
 * //in class Card il metodo Card per mettere l era alla carta ha un nome un po generico (però l ho usato)
 *
 * //inside class Game ?
 *
 *
 *   public ArrayList<Player> getPlayers(){ // (Game game) come parametro se ci dovessero essere piu game
 *      ArrayList<Player> playersInGame = players;
 *      return playersInGame;
 *
 *
 *
 */

package it.polimi.ingsw.model;

public class SustenanceEvent extends EventCard{

    public solveEventCard(Player player,Era era){
        int foodPoints;
        int currentHunger;

        ArrayList<Card> playerDeck = player.getPlayerCards();

        currentHunger = size(playerDeck) - player.getHarvest() - player.getBuildingFoodDiscount(); // current Hunger = number of cards - value of the harvest - BuildingFoodDiscount
        foodPoints = player.getFood() - currentHunger;

        if(foodPoints>=0){
            player.setFood(foodPoints);

        }else{
            //not having enough food -> food is put to 0
            //and implies prestige loss depending on the SustenanceCard's era

            player.setFood(0);

            switch (era){
                case(1):
                    player.editPrestige(foodPoints); //foodPoints are negative
                    break;
                case(2):
                    player.editPrestige(foodPoints*2);
                    break;
                case(3):
                    player.editPrestige(foodPoints*3);
                    break;
            }

        }





    }
}
