package com.visma.pokergame;

import com.visma.pokergame.compare.RankComparator;
import com.visma.pokergame.deck.Deck;
import com.visma.pokergame.deck.PlayingCard;
import com.visma.pokergame.utils.NumberOfSameRank;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class PokerGame {

    private static final String WELCOME = "Welcome! If you wish to start the pokergame press ENTER.";
    private static final String CHOOSE_CARDS_TO_REPLACE = "Select cards that you wish to replace.";
    private static final int [] ROYAL_FLUSH_CHECK = new int[]{9, 10, 11, 12, 13};
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

    private static final int ACE_NUMBER = 1;
    private static final int KING_NUMBER = 12;
    private static final int QUEEN_NUMBER = 11;
    private static final int JACK_NUMBER = 10;

    private List<PlayingCard> hand = new ArrayList<>();

    private Deck deck;

    public PokerGame(){
        deck = new Deck();
    }
    public void initGame(){
        deck.dealCards(HAND_SIZE, hand);
    }
    public int calculateWinnings(){
//        Hand contains a flush - no other combination is possible
        if(cardsOfSameSuit()){
            if(isStraight()){
                return STRAIGHT_FLUSH;
            } else if(isRoyal()){
                return ROYAL_FLUSH;
            }
            return FLUSH;
        }
        NumberOfSameRank numberOfSameRank = numberOfIdenticalRankCards();
        System.out.println(numberOfSameRank);

        if(isStraight()){
            return STRAIGHT;
        }


        return JACKS_OR_BETTER;
    }

    public void displayCards(){
        for (PlayingCard card :
                hand) {
            System.out.println(card.toString());

        }
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

    private boolean handContainsCardWithRank(int rankNumber){
        return hand.stream().anyMatch(playingCard -> playingCard.getCardRank() == rankNumber);
    }
    private boolean isRoyal() {
        boolean hasAce = handContainsCardWithRank(ACE_NUMBER);
        boolean hasKing = handContainsCardWithRank(KING_NUMBER);
        boolean hasQueen = handContainsCardWithRank(QUEEN_NUMBER);
        boolean hasJack = handContainsCardWithRank(JACK_NUMBER);
        boolean has10 = handContainsCardWithRank(9);
        return hasAce && hasKing && hasQueen && hasJack && has10;
    }

    private NumberOfSameRank numberOfIdenticalRankCards(){
        Map<Integer, Long> map = hand.stream()
                .collect(groupingBy(PlayingCard::getCardRank, Collectors.counting()));
        if(map.containsValue(4L)){
            return NumberOfSameRank.FOUR_OF_A_KIND;
        }
        if(map.containsValue(3L)){
            if(map.containsValue(2L)){
                return NumberOfSameRank.FULL_HOUSE;
            } else {
                return NumberOfSameRank.THREE_OF_A_KIND;
            }
        }
        if(map.containsValue(2L)){
            map.values().removeAll(Collections.singleton(2L));
            if(map.size() == 1){
                return NumberOfSameRank.TWO_PAIR;
            }
        }
        return null;
    }
}
