package com.visma;

import com.visma.pokergame.PokerGame;
import com.visma.pokergame.deck.Deck;
import com.visma.pokergame.deck.PlayingCard;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        PokerGame pokerGame = new PokerGame(new Deck());
        pokerGame.initGame();
        do {
            pokerGame.startRound();
        } while(pokerGame.continueGame());
    }

}
