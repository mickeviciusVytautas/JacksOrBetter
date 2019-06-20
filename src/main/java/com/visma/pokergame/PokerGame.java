package com.visma.pokergame;

import com.visma.pokergame.compare.RankComparator;
import com.visma.pokergame.deck.Deck;
import com.visma.pokergame.deck.PlayingCard;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class PokerGame {

    private static final int HAND_SIZE = 5;

    public static final int ROYAL_FLUSH = 800;
    public static final int STRAIGHT_FLUSH = 50;
    public static final int FOUR_OF_A_KIND = 25;
    public static final int FULL_HOUSE = 9;
    public static final int FLUSH = 6;
    public static final int STRAIGHT = 4;
    public static final int THREE_OF_A_KIND = 3;
    public static final int TWO_PAIR = 2;
    public static final int JACKS_OR_BETTER = 1;

    private final int ACE_NUMBER = Arrays.asList(PlayingCard.ranks).indexOf("Ace");
    private final int KING_NUMBER = Arrays.asList(PlayingCard.ranks).indexOf("King");
    private final int QUEEN_NUMBER = Arrays.asList(PlayingCard.ranks).indexOf("Queen");
    private final int JACK_NUMBER = Arrays.asList(PlayingCard.ranks).indexOf("Jack");

    private final String WELCOME_MSG = "Welcome! If you wish to start the pokergame press ENTER.";
    private final String CHOOSE_CARDS_TO_REPLACE_MSG = "\nEnter index of a card you wish to replace. Else - enter q.";
    private final String INPUT_NOT_NUMBER_MSG = "That is not a number! Enter q if you want to stop.";
    private final String INPUT_NUMBER_INCORRECT_MSG = "That is not a correct number!";
    private final String INSUFFICIENT_FUNDS_MSG = "Insufficient funds";
    private final String CONTINUE_MGS = "Want to play another game? y/n";
    private final String LOST_GAME_MSG = "Sorry, but you lost all your money!";

    private List<PlayingCard> hand = new ArrayList<>();

    private Deck deck;

    private int balance;

    private int betAmount = 1;

    private int currentPrize;
    private static Scanner scanner;

    public PokerGame(){
        deck = new Deck();
        scanner = new Scanner(System.in);
    }
    public void initGame(){
        balance = 10;
        displayWelcome();
    }

    public void startRound(){
        hand.clear();
        hand.addAll(deck.dealCards(HAND_SIZE));
        betAmountPrompt();
        displayInitialHand();
        replacingCardsPrompt();
        balance += calculatePayout();
        displayFinalInfo();
    }

    private void betAmountPrompt() {
        System.out.println("Enter your bet:");
        int betInput = 0;
        while(betInput == 0) {
            while (!scanner.hasNextInt()) {
                System.out.println(INPUT_NOT_NUMBER_MSG);
            }
            betInput = scanner.nextInt();
            if (betInput > balance) {
                System.out.println(INSUFFICIENT_FUNDS_MSG);
                betInput = 0;
            }
        }
    }

    public boolean continueGame(){
        if(balance <= 0){
            System.out.println(LOST_GAME_MSG);
            return false;
        }
        boolean continueGame = false;
        String answer = "";
        System.out.println(CONTINUE_MGS);
        do{
            String input = scanner.next();
            if(input.equalsIgnoreCase("y")) {
                answer = input;
                continueGame = true;
            }
            if(input.equalsIgnoreCase("n")){
                answer = input;
                System.out.println("Thank you for playing!");
                System.out.println("Final score is " + balance);
            }
        } while(answer.isEmpty());
        return continueGame;
    }


    public void replacingCardsPrompt() {
        String answer = "";
        while (hand.size() != 0) {
            while (!scanner.hasNextInt()) {
                answer = scanner.next();
                if (answer.equalsIgnoreCase("q")) {
                    break;
                }
                System.out.println(INPUT_NOT_NUMBER_MSG);
            }
            if (!answer.equalsIgnoreCase("q")) {
                int cardId = scanner.nextInt();
                PlayingCard playingCard = replaceCard(cardId);
                if (playingCard == null) {
                    System.out.println(INPUT_NUMBER_INCORRECT_MSG);
                } else {
                    System.out.println("\nYou removed " + playingCard.toString() + " from your hand.");
                    displayInitialHand();
                }
            } else {
                break;
            }
        }
        fillHand();
    }


    private int calculatePayout(){
//        Hand contains a flush
        if(cardsOfSameSuit()){
//            Hand also contains a straight
            if(isStraight()){
                return betAmount * STRAIGHT_FLUSH;
            }
//            RoyalFlush - Ace is at the end of straight
                else if(isRoyalFlush()){
                    return betAmount * ROYAL_FLUSH;
            }
            return betAmount * FLUSH;
        }

//        Hand contains any of pairs
        int winningsFromPairs = numberOfIdenticalRankCards();
        if(winningsFromPairs != 0){
            return winningsFromPairs;
        };
//        Hand contains a straight
        if(isStraight()){
            return betAmount * STRAIGHT;
        }
//        Hand contains jack or a higher card
        if(hasRoyal()) {
            return betAmount * JACKS_OR_BETTER;
        }
//        No winning
        return -betAmount;
    }

    private boolean cardsOfSameSuit(){
        boolean isSame = true;
        PlayingCard playingCard = hand.get(0);
        for (int i = 1; i < hand.size(); i++) {
            isSame = playingCard.suitEquals(hand.get(i));
            if(!isSame){
                break;
            }
        }
        return isSame;
    }

    private boolean isStraight() {
        boolean isStraight = true;
        hand.sort(new RankComparator());
        PlayingCard playingCard = hand.get(0);
        for (int i = 1; i < hand.size(); i ++){
            isStraight = playingCard.getCardRank() + i == hand.get(i).getCardRank();
            if(!isStraight){
                break;
            }
        }
        return isStraight;
    }

    private boolean hasRoyal(){
        return handContainsCardWithRankHigherThan(JACK_NUMBER) || handContainsCardWithRank(ACE_NUMBER);
    }
    //TODO refactor method
    private boolean isRoyalFlush() {
        boolean hasAce = handContainsCardWithRank(ACE_NUMBER);
        boolean hasKing = handContainsCardWithRank(KING_NUMBER);
        boolean hasQueen = handContainsCardWithRank(QUEEN_NUMBER);
        boolean hasJack = handContainsCardWithRank(JACK_NUMBER);
        boolean has10 = handContainsCardWithRank(9);
        return hasAce && hasKing && hasQueen && hasJack && has10;
    }

    private int numberOfIdenticalRankCards(){
        Map<Integer, Long> map = hand.stream()
                .collect(groupingBy(PlayingCard::getCardRank, Collectors.counting()));
        if(map.containsValue(4L)){
            return FOUR_OF_A_KIND;
        }
        if(map.containsValue(3L)){
            if(map.containsValue(2L)){
                return FULL_HOUSE;
            } else {
                return THREE_OF_A_KIND;
            }
        }
        if(map.containsValue(2L)){
            map.values().removeAll(Collections.singleton(2L));
            if(map.size() == 1){
                return TWO_PAIR;
            }
        }
        return 0;
    }

    public PlayingCard replaceCard(int cardId) {
        if (cardId <= 0 || cardId > hand.size()) {
            return null;
        }
        return hand.remove(cardId - 1);

    }

    private boolean handContainsCardWithRank(int rankNumber){
        return hand.stream().anyMatch(playingCard -> playingCard.getCardRank() == rankNumber);
    }

    private boolean handContainsCardWithRankHigherThan(int rankNumber){
        return hand.stream().anyMatch(playingCard -> playingCard.getCardRank() >= rankNumber);
    }

    private void fillHand() {
        int cardsToDeal = HAND_SIZE - hand.size();
        hand.addAll(deck.dealCards(cardsToDeal));
    }

//    Methods that are concerned with displaying information to user
    private void displayHand(){
        System.out.println("Your hand: ");
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + 1 + ". " + hand.get(i).toString());
        }
    }

    private void displayInitialHand(){
        displayHand();
        System.out.println(CHOOSE_CARDS_TO_REPLACE_MSG);
    }

    private void displayWelcome() {
        System.out.println(WELCOME_MSG);
        try{
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayFinalInfo() {
        System.out.println("Your final hand is: ");
        displayHand();
        System.out.println("___________________________________________");
//        System.out.println("Your won: " + balance ".");

        System.out.println("Your balance: " + balance);
        System.out.println();
    }

    public Deck getDeck() {
        return deck;
    }

    public void setDeck(Deck deck) {
        this.deck = deck;
    }

}
