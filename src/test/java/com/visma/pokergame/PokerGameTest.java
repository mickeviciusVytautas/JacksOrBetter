package com.visma.pokergame;

import com.visma.pokergame.deck.Deck;
import com.visma.pokergame.deck.PlayingCard;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
class PokerGameTest {

    private Deck mockDeck = Mockito.mock(Deck.class);

    private PokerGame pokerGame;

    private List<PlayingCard> playingCards = new ArrayList<>();
    private PlayingCard aceOfSpades;
    private PlayingCard tenOfSpades;
    private PlayingCard jacksOfSpades;
    private PlayingCard queenOfSpades;
    private PlayingCard kingOfSpades;

    @BeforeEach
    public void setUp() throws Exception{
        pokerGame = new PokerGame(mockDeck);
        MockitoAnnotations.initMocks(this);

        aceOfSpades = new PlayingCard(1, 0);
        tenOfSpades = new PlayingCard(1, 9);
        jacksOfSpades = new PlayingCard(1, 10);
        queenOfSpades = new PlayingCard(1, 11);
        kingOfSpades = new PlayingCard(1, 12);
    }

    @Test
    void shouldFindRoyalFlush() {

        playingCards.add(aceOfSpades);
        playingCards.add(tenOfSpades);
        playingCards.add(jacksOfSpades);
        playingCards.add(queenOfSpades);
        playingCards.add(kingOfSpades);

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.ROYAL_FLUSH));
    }

    @Test
    void shouldFindStraightFlush() {

        playingCards.add(new PlayingCard(1, 8));
        playingCards.add(tenOfSpades);
        playingCards.add(jacksOfSpades);
        playingCards.add(queenOfSpades);
        playingCards.add(kingOfSpades);

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.STRAIGHT_FLUSH));
    }

    @Test
    void shouldFindStraight() {

        playingCards.add(new PlayingCard(2, 0));
        playingCards.add(tenOfSpades);
        playingCards.add(jacksOfSpades);
        playingCards.add(queenOfSpades);
        playingCards.add(kingOfSpades);

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.STRAIGHT));
    }

    @Test
    void shouldFindFlush() {

        playingCards.add(new PlayingCard(1, 5));
        playingCards.add(tenOfSpades);
        playingCards.add(jacksOfSpades);
        playingCards.add(queenOfSpades);
        playingCards.add(kingOfSpades);

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.FLUSH));
    }

    @Test
    void shouldFindFourOfAKind() {

        playingCards.add(new PlayingCard(1, 5));
        playingCards.add(new PlayingCard(2, 5));
        playingCards.add(new PlayingCard(3, 5));
        playingCards.add(new PlayingCard(4, 5));
        playingCards.add(new PlayingCard(1, 6));

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.FOUR_OF_A_KIND));
    }

    @Test
    void shouldFindFullHouse() {

        playingCards.add(new PlayingCard(1, 5));
        playingCards.add(new PlayingCard(2, 5));
        playingCards.add(new PlayingCard(3, 6));
        playingCards.add(new PlayingCard(4, 6));
        playingCards.add(new PlayingCard(4, 6));

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.FULL_HOUSE));
    }

    @Test
    void shouldFindThreeOfAKind() {

        playingCards.add(new PlayingCard(1, 5));
        playingCards.add(new PlayingCard(2, 5));
        playingCards.add(new PlayingCard(3, 5));
        playingCards.add(new PlayingCard(4, 6));
        playingCards.add(new PlayingCard(4, 4));

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.THREE_OF_A_KIND));
    }

    @Test
    void shouldFindTwoPair() {

        playingCards.add(new PlayingCard(1, 5));
        playingCards.add(new PlayingCard(2, 5));
        playingCards.add(new PlayingCard(3, 6));
        playingCards.add(new PlayingCard(4, 6));
        playingCards.add(new PlayingCard(4, 4));

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.TWO_PAIR));
    }

    @Test
    void shouldFindJacksOrBetter() {

        playingCards.add(new PlayingCard(1, 5));
        playingCards.add(new PlayingCard(2, 1));
        playingCards.add(new PlayingCard(3, 4));
        playingCards.add(jacksOfSpades);
        playingCards.add(new PlayingCard(4, 6));

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.JACKS_OR_BETTER));
    }

    @Test
    void shouldFindNoPrize() {

        playingCards.add(new PlayingCard(1, 5));
        playingCards.add(new PlayingCard(2, 2));
        playingCards.add(new PlayingCard(3, 4));
        playingCards.add(new PlayingCard(4, 6));
        playingCards.add(new PlayingCard(4, 6));

        Mockito.when(mockDeck.dealCards(Mockito.anyInt())).thenReturn(playingCards);

        pokerGame.dealCards();
        pokerGame.calculatePrize();
        assertThat(pokerGame.getPrizeMultiplier(), is(PokerGame.NO_PRIZE));
    }

}