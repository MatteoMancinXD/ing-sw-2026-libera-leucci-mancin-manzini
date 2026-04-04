package it.polimi.ingsw;
import java.util.List;
import java.util.ArrayList;
import java.util.Collections;


public class Game {

    private int round;
    private int era;
    private int numPlayers;
    private int currentPlayerIndex;
    private GamePhase currentPhase;
    private ArrayList<Player> players;
    private Board board;
    private TribeDeck deck;
    private BuildingDeck buildingDeck;


    public Game(int numPlayers) {
        this.round = 0;
        this.era = 1;
        this.numPlayers = numPlayers;
        this.currentPlayerIndex = 0;
        this.players = new ArrayList<>();
        this.board = new Board(numPlayers);     //La board viene inizializzata in base al numero di players
        this.deck = new TribeDeck();
        this.buildingDeck = new BuildingDeck();

    }

    public void setRound(int round){
        this.round = round;
    }
    public int getRound() {
        return this.round;
    }
    public void setEra(int era) {
        this.era = era;
    }
    public int getEra() {
        return era;
    }
    public void setNumPlayers(int n) throws IllegalArgumentException {
        if (n>5 || n<2) {
            throw new IllegalArgumentException("Invalid number of players! Max = 5");
        }
        numPlayers = n;
    }
    public int getNumPlayers() {
        return numPlayers;
    }
    public void addPlayer(Player p) {
        if (players.size() < numPlayers) {
            players.add(p);
        }
    }

    public void startGame() {
        board.fill();       //Round 0 : .fill riempie entrambe le righe e i buildings
        Collections.shuffle(players);        //Shuffle dei player per avere un ordine casuale all'inizio
        for (int i = 0; i < players.size(); i++) {
            Player p = players.get(i);
            if(i==0) {                  //Primo giocatore prende 2 cibo
                p.editFood(2);
            }
            else if (i == 1 || i == 2) { //Secondo e terzo giocatore prendono 3 cibo
                p.editFood(3);
            }
            else {                       //Quarto e quinto giocatore prendono 4 cibo
                è.editFood(4);
            }
        }
        this.currentPhase = GamePhase.PLACEMENT;
        this.currentPlayerIndex = 0;
        this.round = 1;
    }

    public ArrayList<Player> endGame() {
        int max = Integer.MIN_VALUE;
        Player winner = new Player();
        ArrayList<Player> winners = new ArrayList<>();
        for (Player p : players) {
            if(p.getPrestige() > max) {
                max = p.getPrestige();
                winner = p;
                winners.clear();
            }
            else if (p.getPrestige() == max) {
                if(p.getFood() > winner.getFood()) {
                    winner = p;
                    winners.clear();
                }
                else if (p.getFood() == winner.getFood()) {
                    winners.add(p);
                }
            }
        }
        winners.add(winner);
        return winners;
    }

    public void nextPlayer(){
        currentPlayerIndex++;

        if(currentPlayerIndex >= players.size()) {      //Fine player
            if(currentPhase == GamePhase.PLACEMENT) {   //Si passa dalla fase di piazzamento alla risoluzione
                currentPhase = GamePhase.RESOLUTION;
                currentPlayerIndex = 0;
            }
            else if( currentPhase == GamePhase.RESOLUTION) {
                nextTurn();
            }
        }
    }

    public void nextTurn() {
        board.solveEvents();

        board.clearLowerRow();
        board.shiftRow();
        if(board.fill()) {              //Round !=0 : .fill() riempie solo la riga superiore ; Fill Boolean() true = nextEra, false = niente
            nextEra();
        }

        this.round++;
        if (this.round > 10) {
            endGame();
            return;
        }

        this.currentPhase = GamePhase.PLACEMENT;
        this.currentPlayerIndex = 0;
        //logica per riordinare i players in base all'ordine sulla tileboard: ipotizzo che in Tile sia salvato il player
        ArrayList<Player> nextTurnOrder = new ArrayList<>();
        for(Tile tile: board.getTrack()) {
            if (tile.getStatus()) {
                nextTurnOrder.add(tile.getPlayer());
            }
        }
        this.players = nextTurnOrder;
    }

    public void nextEra() {
        this.era++;
        board.shiftBuildings();
        board.fillBuildings();
    }

    public void placeTotem(int pos) throws IllegalArgumentException{     //si presuppone che pos sia un numero sempre legale (compreso tra 0 e tiles-1)
        if (board.getTrack().get(pos).getStatus()) {
            throw new IllegalArgumentException("Position already taken by another player! Please choose a free tile");
        }
        Player p = players.get(currentPlayerIndex);
        board.getTrack().get(pos).place(p);             //place imposta lo Status della Tile a True e salva il player
        nextPlayer();
    }

}