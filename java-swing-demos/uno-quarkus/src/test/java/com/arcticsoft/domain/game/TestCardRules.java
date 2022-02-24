package com.arcticsoft.domain.game;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.card.ActionCard;
import com.arcticsoft.domain.card.CardColor;
import com.arcticsoft.domain.card.CardType;
import com.arcticsoft.domain.testhelper.CardTestFactory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TestCardRules {

    private static Stream<Arguments> provideValidTopCardsForNumberCard() {
        return Stream.of(
                Arguments.of(CardTestFactory.createNumberCard(5, CardColor.RED)),
                Arguments.of(CardTestFactory.createNumberCard(4, CardColor.RED)),
                Arguments.of(CardTestFactory.createNumberCard(5, CardColor.BLUE)),
                Arguments.of(CardTestFactory.createSkipCard(CardColor.RED)),
                Arguments.of(CardTestFactory.createReverseCard(CardColor.RED)),
                Arguments.of(CardTestFactory.createDrawTwoCard(CardColor.RED)),
                Arguments.of(CardTestFactory.createWildColorCard(CardColor.RED)),
                Arguments.of(CardTestFactory.createWildDrawFourCard(CardColor.RED)));
    }

    private static Stream<Arguments> provideInvalidTopCardsForNumberCard() {
        return Stream.of(
                Arguments.of(CardTestFactory.createNumberCard(4, CardColor.BLUE)),
                Arguments.of(CardTestFactory.createSkipCard(CardColor.BLUE)),
                Arguments.of(CardTestFactory.createReverseCard(CardColor.BLUE)),
                Arguments.of(CardTestFactory.createDrawTwoCard(CardColor.BLUE)),
                Arguments.of(CardTestFactory.createWildColorCard(CardColor.BLUE)),
                Arguments.of(CardTestFactory.createWildDrawFourCard(CardColor.BLUE)),
                Arguments.of(CardTestFactory.createWildColorCard()),
                Arguments.of(CardTestFactory.createWildColorCard(CardColor.BLUE)),
                Arguments.of(CardTestFactory.createWildDrawFourCard()),
                Arguments.of(CardTestFactory.createWildDrawFourCard(CardColor.BLUE)));
    }

    private static Stream<Arguments> provideValidTopCardsForActionCard() {
        var arguments = new ArrayList<Arguments>();
        var actionTypes = new CardType[] { CardType.SKIP, CardType.REVERSE, CardType.DRAW_TWO };
        for (var action : actionTypes) {
            var cardToPlay = CardTestFactory.createActionCard(action, CardColor.YELLOW);
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createActionCard(action, CardColor.RED)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createNumberCard(5, CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createSkipCard(CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createReverseCard(CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createDrawTwoCard(CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildColorCard(CardColor.YELLOW)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildDrawFourCard(CardColor.YELLOW)));
        }
        return arguments.stream();
    }

    private static Stream<Arguments> provideInvalidTopCardsForActionCard() {
        var arguments = new ArrayList<Arguments>();
        var actionTypes = new CardType[] { CardType.SKIP, CardType.REVERSE, CardType.DRAW_TWO };
        for (var action : actionTypes) {
            var cardToPlay = CardTestFactory.createActionCard(action, CardColor.YELLOW);
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createNumberCard(5, CardColor.BLUE)));
            for (var otherAction : actionTypes) {
                if (otherAction != action) {
                    arguments.add(
                            Arguments.of(cardToPlay, CardTestFactory.createActionCard(otherAction, CardColor.BLUE)));
                }
            }
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildColorCard()));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildColorCard(CardColor.BLUE)));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildDrawFourCard()));
            arguments.add(Arguments.of(cardToPlay, CardTestFactory.createWildDrawFourCard(CardColor.BLUE)));
        }
        return arguments.stream();
    }

    @ParameterizedTest
    @MethodSource("provideValidTopCardsForNumberCard")
    void WhenNumberCardPlayed_ShouldBeValid(AbstractCard topCard) {
        var cardToPlay = CardTestFactory.createNumberCard(5, CardColor.RED);
        var result = CardRules.isValidNumberCard(topCard, cardToPlay);
        Assertions.assertTrue(result, createTestMessage(topCard, cardToPlay));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTopCardsForNumberCard")
    void WhenMisMatchNumberCardPlayed_ShouldBeInvalid(AbstractCard topCard) {
        var cardToPlay = CardTestFactory.createNumberCard(5, CardColor.RED);
        var result = CardRules.isValidNumberCard(topCard, cardToPlay);
        Assertions.assertFalse(result, createTestMessage(topCard, cardToPlay));
    }

    @ParameterizedTest
    @MethodSource("provideValidTopCardsForActionCard")
    void WhenActionCardPlayed_ShouldBeValid(AbstractCard cardToPlay, AbstractCard topCard) {
        var result = CardRules.isValidActionCard(topCard, (ActionCard) cardToPlay);
        Assertions.assertTrue(result, createTestMessage(topCard, cardToPlay));
    }

    @ParameterizedTest
    @MethodSource("provideInvalidTopCardsForActionCard")
    void WhenMismatchActionCardPlayed_ShouldBeInvalid(AbstractCard cardToPlay, AbstractCard topCard) {
        var result = CardRules.isValidActionCard(topCard, (ActionCard) cardToPlay);
        Assertions.assertFalse(result, createTestMessage(topCard, cardToPlay));
    }

    @Test
    void WhenWildCardWithChosenColor_ShouldBeValid() {
        var wildCard = CardTestFactory.createWildColorCard(CardColor.RED);
        var result = CardRules.isValidWildCard(wildCard);
        assertTrue(result, wildCard.toString());
    }

    @Test
    void WhenWildCardWithoutColor_ShouldBeInvalid() {
        var wildCard = CardTestFactory.createWildColorCard();
        var result = CardRules.isValidWildCard(wildCard);
        assertFalse(result, wildCard.toString());
    }

    private String createTestMessage(AbstractCard topCard, AbstractCard playedCard) {
        return String.format("Top Card %s, Played Card %s", topCard, playedCard);
    }
}
