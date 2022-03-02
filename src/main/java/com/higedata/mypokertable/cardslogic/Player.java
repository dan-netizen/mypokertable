package com.higedata.mypokertable.cardslogic;

public class Player {
    private Card[] handCards;
    private int id;
    private String name;
    private int position;
    private boolean folded;
    private boolean playing;
    private int cash, betAmount;

    Player(int id, String name){
        this.id = id;
        this.name = name;
        handCards[0] = null;
        handCards[1] = null;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return this.position;
    }

    public int betAmount (int amount) {
        this.cash = this.cash - amount;
        this.betAmount = this.betAmount + amount;
        return amount;
    }

    public int call(int currentBet) {
        int newBet = currentBet - betAmount;
        return newBet;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    public boolean isPlaying() {
        return this.playing;
    }

    public void giveCard(Card card){
        if (handCards[0] == null){
            handCards[0] = card;
        } else if (handCards[1] == null) {
            handCards[1] = card;
        } else {System.out.println("Error, gave 3rd card to player "+this.name); }
    }
}
