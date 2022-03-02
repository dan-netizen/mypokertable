package com.higedata.mypokertable.cardslogic;

import java.util.ArrayList;

public class PokerTable {
    private Deck deck;
    private ArrayList<Player> players;
    private int nrpl;
    private int pot;
    private int dealer;
    private int id;
    private Card[] comCards;
    private int blind;
    private int currentBet;

    PokerTable(int id){
        this.id = id;
        this.players = new ArrayList<Player>();
        nrpl = 0;
        dealer = -1;
    }

    public void setBlind(int blind) {
        this.blind = blind;
    }

    public int getNextPlayerIndex(int index) {
        do {
            index++;
            if (index >= nrpl) {
                index = 0;
            }
        } while(!players.get(index).isPlaying());
        return index;
    }

    //pass the dealer to next Player still in game
    public void nextDealer() {
        dealer = getNextPlayerIndex(dealer);
    }

    //payblinds
    public void payBlinds() {
        int smallBlind = getNextPlayerIndex(dealer);
        int bigBlind = getNextPlayerIndex(smallBlind);
        pot += players.get(smallBlind).betAmount(this.blind);
        pot += players.get(bigBlind).betAmount(this.blind * 2);
        currentBet = blind * 2;
    }

    public void startNewRound() {
        pot = 0;
        nextDealer();

    }

    public void addPlayer(Player player) {
        player.setPosition(nrpl);
        players.add(player);
        nrpl++;
    }


}
