package com.arcticsoft.domain.common;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.card.CardColor;
import com.arcticsoft.domain.game.events.CardPlayed;
import com.arcticsoft.domain.testhelper.CardTestFactory;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TestDomainEventPublisher {

    @Test
    void WhenEventPublished_ShouldInvokeSubscriber() {
        var subscriber = new TestSubscriber();
        DomainEventPublisher.subscribe(subscriber);
        DomainEventPublisher.publish(
                new CardPlayed(UUID.randomUUID(), CardTestFactory.createNumberCard(1, CardColor.RED)));
        DomainEventPublisher.unsubscribe(subscriber);
        assertEquals(1, subscriber.timesInvoked);
        assertEquals(CardTestFactory.createNumberCard(1, CardColor.RED), subscriber.playedCard);
    }

    @Test
    void WhenUnsubscribed_ShouldNotInvokeSubscriber() {
        var subscriber = new TestSubscriber();
        DomainEventPublisher.subscribe(subscriber);
        DomainEventPublisher.unsubscribe(subscriber);
        DomainEventPublisher.publish(
                new CardPlayed(UUID.randomUUID(), CardTestFactory.createNumberCard(1, CardColor.RED)));
        assertEquals(0, subscriber.timesInvoked);
        assertNull(subscriber.playedCard);
    }

    static class TestSubscriber implements DomainEventSubscriber {
        int timesInvoked = 0;
        AbstractCard playedCard = null;

        @Override
        public void handleEvent(DomainEvent event) {
            timesInvoked += 1;
            playedCard = ((CardPlayed) event).getPlayedCard();
        }
    }
}
