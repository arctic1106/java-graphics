package com.arcticsoft.domain.game;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.player.HandCardList;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DealerService {
    public static final int TOTAL_INITIAL_HAND_CARDS = 7;

    private DealerService() {
    }

    public static List<AbstractCard> shuffle(List<AbstractCard> cards) {
        var shuffledCards = new ArrayList<>(cards);
        Collections.shuffle(shuffledCards);
        return shuffledCards;
    }

    public static DrawPile shuffle(DrawPile drawPile, AbstractCard lastPlayedCard) {
        var oldCards = new ArrayList<AbstractCard>();
        oldCards.add(lastPlayedCard);
        for (int i = 0; i < drawPile.getSize(); i++) {
            oldCards.add(drawPile.drawCard());
        }
        var shuffledCards = shuffle(oldCards);
        return new DrawPile(shuffledCards);
    }

    private static void swapCard(List<AbstractCard> shuffledList, int currentIndex, int randomIndex) {
        var randomCard = shuffledList.get(randomIndex);
        shuffledList.set(randomIndex, shuffledList.get(currentIndex));
        shuffledList.set(currentIndex, randomCard);
    }

    public static HandCardList[] dealInitialHandCards(DrawPile drawPile, int totalPlayers) {
        var handCardLists = new HandCardList[totalPlayers];
        for (int i = 0; i < TOTAL_INITIAL_HAND_CARDS; i++) {
            for (int p = 0; p < totalPlayers; p++) {
                if (i == 0) {
                    handCardLists[p] = new HandCardList();
                }
                handCardLists[p].addCard(drawPile.drawCard());
            }
        }
        return handCardLists;
    }
}
