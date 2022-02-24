package com.arcticsoft.domain.player;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.card.CardType;
import com.arcticsoft.domain.card.CardUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class HandCardList {
    private final List<AbstractCard> handCards = new ArrayList<>();

    public void addCard(AbstractCard newCard) {
        handCards.add(newCard);
    }

    public boolean removeCard(AbstractCard card) {
        var cardToRemove = CardUtil.isWildCard(card) ? findCardOfType(card.getType()) : card;
        return handCards.remove(cardToRemove);
    }

    private AbstractCard findCardOfType(CardType type) {
        for (var card : handCards) {
            if (card.getType() == type) {
                return card;
            }
        }
        return null;
    }

    public Stream<AbstractCard> getCardStream() {
        return handCards.stream();
    }

    public boolean hasCard(AbstractCard card) {
        return CardUtil.isWildCard(card)
                ? getCardStream().anyMatch(c -> c.getType() == card.getType())
                : getCardStream().anyMatch(c -> c.equals(card));
    }

    public int size() {
        return handCards.size();
    }
}
