package com.visma.pokergame.compare;

import com.visma.pokergame.deck.PlayingCard;

import java.util.Comparator;

public class RankComparator implements Comparator<PlayingCard> {
    @Override
    public int compare(PlayingCard pc1, PlayingCard pc2) {
        return pc1.getCardRank() - pc2.getCardRank();
    }
}
