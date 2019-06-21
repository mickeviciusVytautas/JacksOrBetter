package com.visma.pokergame;

import com.visma.pokergame.compare.RankComparator;
import com.visma.pokergame.deck.Deck;
import com.visma.pokergame.deck.PlayingCard;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.groupingBy;

public class PokerGame {

    private static final int HAND_SIZE = 5;

    private final Map<Integer, String> prizes = new HashMap<Integer, String>(){
        {
            put(ROYAL_FLUSH, "Royal flush");
            put(STRAIGHT_FLUSH, "Straight flush");
            put(FOUR_OF_A_KIND, "Four of a kind");
            put(FULL_HOUSE, "Full house");
            put(FLUSH, "Flush");
            put(STRAIGHT, "Straight");
            put(THREE_OF_A_KIND, "Three of a kind");
            put(TWO_PAIR, "Two pair");
            put(JACKS_OR_BETTER, "Jacks or better");
        }
    };

    private final int ACE_NUMBER = Arrays.asList(PlayingCard.ranks).indexOf("Ace");
    private final int KING_NUMBER = Arrays.asList(PlayingCard.ranks).indexOf("King");
    private final int QUEEN_NUMBER = Arrays.asList(PlayingCard.ranks).indexOf("Queen");
    private final int JACK_NUMBER = Arrays.asList(PlayingCard.ranks).indexOf("Jack");

    static final int ROYAL_FLUSH = 800;
    static final int STRAIGHT_FLUSH = 50;
    static final int FOUR_OF_A_KIND = 25;
    static final int FULL_HOUSE = 9;
    static final int FLUSH = 6;
    static final int STRAIGHT = 4;
    static final int THREE_OF_A_KIND = 3;
    static final int TWO_PAIR = 2;
    static final int JACKS_OR_BETTER = 1;
    static final int NO_PRIZE = -1;
    private List<PlayingCard> hand = new ArrayList<>();

    private Deck deck;

    private int balance;

    private int betAmount = 1;

    private int prizeMultiplier;

    private Scanner scanner;

    public PokerGame(Deck deck){
        this.deck = deck;
        scanner = new Scanner(System.in);
    }
    public void initGame(){
        balance = 10;
        displayWelcome();
    }

    public void startRound(){
        dealCards();
        betAmountPrompt();
        replacingCardsPrompt();
        calculatePrize();
        balance += prizeMultiplier * betAmount;
        displayFinalInfo();
    }

    void dealCards(){
        hand.clear();
        hand.addAll(deck.dealCards(HAND_SIZE));
    }

    private void betAmountPrompt() {
        System.out.println("Enter your bet:");
        int betInput = 0;
        while(betInput == 0) {
            while (!scanner.hasNextInt()) {
                scanner.next();
                System.out.println("That is not a number!");
            }
            betInput = scanner.nextInt();
            if (betInput > balance) {
                System.out.println("Insufficient funds");
                betInput = 0;
            }
        }
        betAmount = betInput;
        displayInitialHand();
    }

    public boolean continueGame(){
        if(balance <= 0){
            System.out.println("Sorry, but you lost all your money!");
            return false;
        }
        boolean continueGame = false;
        String answer = "";
        System.out.println("Want to play another game? y/n");
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

    private void replacingCardsPrompt() {
        String answer = "";
        while (hand.size() != 0) {
            while (!scanner.hasNextInt()) {
                answer = scanner.next();
                if (answer.equalsIgnoreCase("q")) {
                    break;
                }
                System.out.println("That is not a number! Enter q if you want to stop.");
            }
            if (!answer.equalsIgnoreCase("q")) {
                int cardId = scanner.nextInt();
                PlayingCard playingCard = replaceCard(cardId);
                if (playingCard == null) {
                    System.out.println("That is not a correct number!");
                } else {
                    System.out.println("\nYou removed " + playingCard.toString() + " from your hand.");
                    if(hand.size() != 0) {
                        displayInitialHand();
                    }
                }
            } else {
                break;
            }
        }
        fillHand();
    }

//  Method that calculates a prize
    void calculatePrize(){
        if(cardsOfSameSuit()){
            if(isStraight()){
                prizeMultiplier = STRAIGHT_FLUSH;       //Case when FLUSH && STRAIGHT
                return;
            } else if(isRoyal()) {
                prizeMultiplier = ROYAL_FLUSH;          //Case when FLUSH && STRAIGHT && ROYAL
                return;
            }
            prizeMultiplier = FLUSH;                    //Case when only FLUSH
            return;
        }
        int prizeFromPairs = containsPairOf();
        if(prizeFromPairs != 0){
            prizeMultiplier = prizeFromPairs;           //Case when TWO_PAIR or better pair
            return;
        };
        if(isStraight() || isRoyal()){
            prizeMultiplier = STRAIGHT;                 //Case when STRAIGHT || ROYAL (Ace is at the end)
            return;
        }
        if(hasRoyal()) {
            prizeMultiplier = JACKS_OR_BETTER;          //Case when > Jack
            return;
        }
        prizeMultiplier = NO_PRIZE;
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
    private boolean isRoyal() {
        boolean hasAce = handContainsCardWithRank(ACE_NUMBER);
        boolean hasKing = handContainsCardWithRank(KING_NUMBER);
        boolean hasQueen = handContainsCardWithRank(QUEEN_NUMBER);
        boolean hasJack = handContainsCardWithRank(JACK_NUMBER);
        boolean has10 = handContainsCardWithRank(9);
        return hasAce && hasKing && hasQueen && hasJack && has10;
    }

    private int containsPairOf(){
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

//    Methods that display information to user
    private void displayHand(){
        for (int i = 0; i < hand.size(); i++) {
            System.out.println(i + 1 + ". " + hand.get(i).toString());
        }
    }

    private void displayInitialHand(){
        System.out.println("Your hand: ");
        displayHand();
        System.out.println("\nEnter index of a card you wish to replace. Else - enter q.");
    }

    private void displayWelcome() {
        System.out.println( "Welcome! If you wish to start the poker game press ENTER.");
        try{
            System.in.read();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displayFinalInfo() {
        System.out.println("\n\nYour final hand is: ");
        displayHand();
        System.out.println("*********************************************************");
        if(prizeMultiplier > 0) {
            System.out.println("It is " + prizes.get(prizeMultiplier) + "!"
                    + "\nYou won: " + prizeMultiplier * betAmount + ".");
        } else {
            System.out.println("You lost: " +  betAmount + ".");
        }
        System.out.println("Your balance: " + balance);
        System.out.println();
    }

    int getPrizeMultiplier() {
        return prizeMultiplier;
    }

}
