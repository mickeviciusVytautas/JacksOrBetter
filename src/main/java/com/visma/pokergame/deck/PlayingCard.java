package com.visma.pokergame.deck;

/*
 * PlayingCard
 *
 * 2019-06-19
 *
 * Represents playing card in card game.
 *
 */
public class PlayingCard {

    public static final String[] ranks = {"Ace", "2", "3", "4", "5", "6", "7", "8", "9", "10", "Jack", "Queen", "King"};

    public static final String[] suits = {"Spades", "Clubs", "Hearts", "Diamonds"};

    private int cardSuit;

    private int cardRank;

    public PlayingCard(int cardSuit, int cardRank) {
        this.cardSuit = cardSuit;
        this.cardRank = cardRank;
    }
    public boolean suitEquals(PlayingCard card){
        return card.getCardSuit() == this.cardSuit;
    }

    public int getCardSuit() {
        return cardSuit;
    }

    public void setCardSuit(int cardSuit) {
        this.cardSuit = cardSuit;
    }

    public int getCardRank() {
        return cardRank;
    }

    public void setCardRank(int cardRank) {
        this.cardRank = cardRank;
    }

    @Override
    public String toString(){
        return ranks[cardRank] + " of " + suits[cardSuit];
    }
}
