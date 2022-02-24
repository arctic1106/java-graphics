package com.arcticsoft.domain.player;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.common.Entity;

import java.util.stream.Stream;

public class Player extends Entity {
    private final String name;
    private final HandCardList handCards;

    public Player(String name, HandCardList handCards) {
        super();
        this.name = name;
        this.handCards = handCards;
    }

    public String getName() {
        return name;
    }

    public Stream<AbstractCard> getHandCards() {
        return this.handCards.getCardStream();
    }

    public void addToHandCards(AbstractCard card) {
        handCards.addCard(card);
    }

    public void removePlayedCard(AbstractCard card) {
        handCards.removeCard(card);
    }

    public boolean hasHandCard(AbstractCard card) {
        return this.handCards.hasCard(card);
    }

    public ImmutablePlayer toImmutable() {
        return new ImmutablePlayer(this);
    }
}
