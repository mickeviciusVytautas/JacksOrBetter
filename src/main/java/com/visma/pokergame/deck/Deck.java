package com.visma.pokergame.deck;

import java.util.ArrayList;
import java.util.List;

/*
 * Deck.class
 *
 * 2019-06-19
 *
 * Represents a deck of cards in game.
 */
public class Deck {

    private final int numberOfCards = 52;

    private List<PlayingCard> playingCards = new ArrayList<>(52);

    public Deck() {
        initializeNewDeck();
    }

    public List<PlayingCard> dealCards(int numberOfCards){
        List<PlayingCard> cardsToDeal = new ArrayList<>();
        if(playingCards.size() >= numberOfCards) {
            initializeNewDeck();
        }
        for (int i = 0; i < numberOfCards; i++){
            PlayingCard playingCard = playingCards.remove((int) (Math.random() * playingCards.size()));
            cardsToDeal.add(playingCard);
        }
        return cardsToDeal;
    }

    private void initializeNewDeck() {
        playingCards.clear();
        for (int i = 0; i < PlayingCard.suits.length; i++) {
            for (int j = 0; j < PlayingCard.ranks.length; j++) {
                playingCards.add(new PlayingCard(i, j));
            }
        }
    }

    public List<PlayingCard> getPlayingCards() {
        return playingCards;
    }

    public void setPlayingCards(List<PlayingCard> playingCards) {
        this.playingCards = playingCards;
    }
}
