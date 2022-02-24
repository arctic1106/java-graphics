package com.arcticsoft.domain.game.events;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.common.DomainEvent;

import java.util.UUID;

public class CardPlayed extends DomainEvent {
    private final UUID playerId;
    private final AbstractCard playedCard;

    public CardPlayed(UUID playerId, AbstractCard playedCard) {
        this.playerId = playerId;
        this.playedCard = playedCard;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public AbstractCard getPlayedCard() {
        return playedCard;
    }
}
