package com.visma;

import com.visma.pokergame.PokerGame;
import com.visma.pokergame.deck.Deck;
import com.visma.pokergame.deck.PlayingCard;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
//        Scanner scanner = new Scanner(System.in);
//        System.out.println();
//        try{
//            System.in.read();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

        PokerGame pokerGame = new PokerGame();
        pokerGame.initGame();

        pokerGame.displayCards();

        System.out.println(pokerGame.calculateWinnings());

    }



}
