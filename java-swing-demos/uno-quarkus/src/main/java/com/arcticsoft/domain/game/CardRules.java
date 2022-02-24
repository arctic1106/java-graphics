package com.arcticsoft.domain.game;

import com.arcticsoft.domain.card.*;

public class CardRules {

    private CardRules() {
        throw new IllegalStateException("Utility class");
    }

    public static boolean isValidNumberCard(AbstractCard topCard, NumberCard playedCard) {
        if (isSameColor(topCard, playedCard)) {
            return true;
        }
        if (topCard.getType() == CardType.NUMBER) {
            return ((NumberCard) topCard).getValue() == playedCard.getValue();
        }
        return false;
    }

    public static boolean isValidActionCard(AbstractCard topCard, ActionCard playedCard) {
        if (isSameColor(topCard, playedCard)) {
            return true;
        }
        return topCard.getType() == playedCard.getType();
    }

    public static boolean isValidWildCard(WildCard playedCard) {
        return playedCard.getColor() != null;
    }

    private static boolean isSameColor(AbstractCard topCard, AbstractCard playedCard) {
        return topCard.getColor() == playedCard.getColor();
    }
}
