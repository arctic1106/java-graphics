package com.arcticsoft.domain.card;

public abstract class AbstractCard {
    private final CardType type;
    private final CardColor color;

    protected AbstractCard(CardType type, CardColor color) {
        this.type = type;
        this.color = color;
    }

    public CardType getType() {
        return type;
    }

    public CardColor getColor() {
        return color;
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();

    @Override
    public abstract String toString();
}
