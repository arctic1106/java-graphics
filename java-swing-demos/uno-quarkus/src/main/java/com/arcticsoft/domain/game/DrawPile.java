package com.arcticsoft.domain.game;

import com.arcticsoft.domain.card.AbstractCard;

import java.util.List;
import java.util.Stack;

public class DrawPile {
    private final Stack<AbstractCard> cards = new Stack<>();

    public DrawPile(List<AbstractCard> shuffledCards) {
        cards.addAll(shuffledCards);
    }

    public AbstractCard drawCard() {
        return cards.pop();
    }

    public int getSize() {
        return cards.size();
    }
}
