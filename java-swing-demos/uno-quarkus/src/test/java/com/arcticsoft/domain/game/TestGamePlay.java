package com.arcticsoft.domain.game;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.card.CardColor;
import com.arcticsoft.domain.card.CardUtil;
import com.arcticsoft.domain.player.Player;
import com.arcticsoft.domain.player.PlayerRoundIterator;
import com.arcticsoft.domain.testhelper.CardTestFactory;
import com.arcticsoft.domain.testhelper.PlayerTestFactory;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TestGamePlay {
    private final Player[] players = PlayerTestFactory.createPlayers(4);
    private final PlayerRoundIterator playersIterator = new PlayerRoundIterator(players);

    private static Stream<Arguments> provideValidNumberCards() {
        var cardToPlay = CardTestFactory.createNumberCard(4, CardColor.BLUE);
        return Stream.of(
                Arguments.of(CardTestFactory.createNumberCard(5, CardColor.BLUE), cardToPlay),
                Arguments.of(CardTestFactory.createWildColorCard(), cardToPlay));
    }

    private static Stream<Arguments> provideValidSkipCards() {
        var cardToPlay = CardTestFactory.createSkipCard(CardColor.YELLOW);
        return Stream.of(
                Arguments.of(CardTestFactory.createNumberCard(5, CardColor.YELLOW), cardToPlay),
                Arguments.of(CardTestFactory.createWildColorCard(), cardToPlay));
    }

    private static Stream<Arguments> provideValidReverseCards() {
        var cardToPlay = CardTestFactory.createReverseCard(CardColor.YELLOW);
        return Stream.of(
                Arguments.of(CardTestFactory.createNumberCard(5, CardColor.YELLOW), cardToPlay),
                Arguments.of(CardTestFactory.createWildColorCard(), cardToPlay));
    }

    private static Stream<Arguments> provideValidDrawTwoCards() {
        var cardToPlay = CardTestFactory.createDrawTwoCard(CardColor.YELLOW);
        return Stream.of(
                Arguments.of(CardTestFactory.createNumberCard(5, CardColor.YELLOW), cardToPlay),
                Arguments.of(CardTestFactory.createWildColorCard(), cardToPlay));
    }

    private static Stream<Arguments> provideValidWildColorCards() {
        var cardToPlay = CardTestFactory.createWildColorCard(CardColor.RED);
        return Stream.of(
                Arguments.of(CardTestFactory.createNumberCard(5, CardColor.YELLOW), cardToPlay),
                Arguments.of(CardTestFactory.createWildColorCard(), cardToPlay));
    }

    private static Stream<Arguments> provideValidWildDrawFourCards() {
        var cardToPlay = CardTestFactory.createWildDrawFourCard(CardColor.RED);
        return Stream.of(
                Arguments.of(CardTestFactory.createNumberCard(5, CardColor.YELLOW), cardToPlay),
                Arguments.of(CardTestFactory.createWildColorCard(), cardToPlay));
    }

    private static Stream<Arguments> provideInvalidCardsForNumberCard() {
        var topCard = CardTestFactory.createNumberCard(5, CardColor.BLUE);
        return Stream.of(
                Arguments.of(topCard, CardTestFactory.createNumberCard(4, CardColor.RED)),
                Arguments.of(topCard, CardTestFactory.createSkipCard(CardColor.RED)),
                Arguments.of(topCard, CardTestFactory.createReverseCard(CardColor.RED)),
                Arguments.of(topCard, CardTestFactory.createDrawTwoCard(CardColor.RED)),
                Arguments.of(topCard, CardTestFactory.createWildColorCard()),
                Arguments.of(topCard, CardTestFactory.createWildDrawFourCard()));
    }

    @Test
    void WhenInvalidPlayerPlayed_ShouldBeRejected() {
        var cardToPlay = CardTestFactory.createNumberCard();
        var game = createGame(cardToPlay, CardTestFactory.createNumberCard());
        var playerToPlay = players[2].getId();
        assertThrows(IllegalArgumentException.class, () -> game.playCard(playerToPlay, cardToPlay));
    }

    @Test
    void WhenNonExistingCardPlayed_ShouldBeRejected() {
        var handCard = CardTestFactory.createNumberCard(1, CardColor.RED);
        var cardToPlay = CardTestFactory.createNumberCard(2, CardColor.RED);
        var game = createGame(handCard, CardTestFactory.createNumberCard());
        assertThrows(IllegalArgumentException.class, () -> playCardFromCurrentPlayer(game, cardToPlay));
    }

    @ParameterizedTest
    @MethodSource("provideValidNumberCards")
    void WhenValidNumberCardPlayed_ShouldBeAccepted(AbstractCard topCard, AbstractCard cardToPlay) {
        var game = createGame(cardToPlay, topCard);
        playCardFromCurrentPlayer(game, cardToPlay);
        assertGameState(game, cardToPlay, "2");
    }

    @ParameterizedTest
    @MethodSource("provideValidSkipCards")
    void WhenValidSkipCardPlayed_ShouldBeAccepted(AbstractCard topCard, AbstractCard cardToPlay) {
        var game = createGame(cardToPlay, topCard);
        playCardFromCurrentPlayer(game, cardToPlay);
        assertGameState(game, cardToPlay, "3");
    }

    @ParameterizedTest
    @MethodSource("provideValidReverseCards")
    void WhenValidReverseCardPlayed_ShouldBeAccepted(AbstractCard topCard, AbstractCard cardToPlay) {
        var game = createGame(cardToPlay, topCard);
        playCardFromCurrentPlayer(game, cardToPlay);
        assertGameState(game, cardToPlay, "4");
    }

    @ParameterizedTest
    @MethodSource("provideValidDrawTwoCards")
    void WhenValidDrawTwoCardPlayed_ShouldBeAccepted(AbstractCard topCard, AbstractCard cardToPlay) {
        var game = createGame(
                cardToPlay,
                CardTestFactory.createNumberCard(),
                CardTestFactory.createNumberCard(),
                topCard);
        playCardFromCurrentPlayer(game, cardToPlay);
        assertGameState(game, cardToPlay, "3");
        assertEquals(2, players[1].getHandCards().count());
    }

    @ParameterizedTest
    @MethodSource("provideValidWildColorCards")
    void WhenValidWildColorCardPlayed_ShouldBeAccepted(AbstractCard topCard, AbstractCard cardToPlay) {
        var game = createGame(cardToPlay, topCard);
        playCardFromCurrentPlayer(game, cardToPlay);
        assertGameState(game, cardToPlay, "2");
    }

    @ParameterizedTest
    @MethodSource("provideValidWildDrawFourCards")
    void WhenValidWildDrawFourCardPlayed_ShouldBeAccepted(AbstractCard topCard, AbstractCard cardToPlay) {
        var game = createGame(
                cardToPlay,
                CardTestFactory.createNumberCard(),
                CardTestFactory.createNumberCard(),
                CardTestFactory.createSkipCard(),
                CardTestFactory.createReverseCard(),
                topCard);
        playCardFromCurrentPlayer(game, cardToPlay);
        assertGameState(game, cardToPlay, "3");
        assertEquals(4, players[1].getHandCards().count());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidCardsForNumberCard")
    void WhenInvalidCardPlayed_ShouldBeRejected(AbstractCard topCard, AbstractCard cardToPlay) {
        var game = createGame(cardToPlay, topCard);
        assertThrows(IllegalArgumentException.class, () -> playCardFromCurrentPlayer(game, cardToPlay));
        assertGameState(game, topCard, "1");
    }

    @Test
    void WhenDrawnCardIsPlayable_ShouldPlay() {
        var cardToDraw = CardTestFactory.createWildColorCard();
        var game = createGame(
                CardTestFactory.createSkipCard(CardColor.GREEN),
                cardToDraw,
                CardTestFactory.createNumberCard(2, CardColor.RED));
        game.drawCard(game.getCurrentPlayer().getId());
        assertGameState(game, CardTestFactory.createWildColorCard(CardColor.RED), "2");
        assertEquals(1, players[0].getHandCards().count());
    }

    @Test
    void WhenDrawnCardIsNotPlayable_ShouldNotPlay() {
        var cardToDraw = CardTestFactory.createNumberCard(3, CardColor.GREEN);
        var topCard = CardTestFactory.createNumberCard(2, CardColor.RED);
        var game = createGame(CardTestFactory.createSkipCard(CardColor.GREEN), cardToDraw, topCard);
        game.drawCard(game.getCurrentPlayer().getId());
        assertGameState(game, topCard, "2");
        assertEquals(2, players[0].getHandCards().count());
        assertTrue(players[0].hasHandCard(cardToDraw));
    }

    @Test
    void GivenTwoCards_WhenPlayedWithoutSayingUno_ShouldReceivePenalty() {
        var currentPlayer = players[0];
        var penaltyCard1 = CardTestFactory.createNumberCard(1, CardColor.BLUE);
        var penaltyCard2 = CardTestFactory.createNumberCard(2, CardColor.BLUE);
        var cardToPlay = CardTestFactory.createNumberCard(3, CardColor.GREEN);
        var topCard = CardTestFactory.createNumberCard(3, CardColor.RED);
        currentPlayer.addToHandCards(CardTestFactory.createSkipCard());
        var game = createGame(cardToPlay, penaltyCard1, penaltyCard2, topCard);
        game.playCard(currentPlayer.getId(), cardToPlay, false);
        assertGameState(game, cardToPlay, "2");
        assertEquals(3, players[0].getHandCards().count());
        assertTrue(currentPlayer.hasHandCard(penaltyCard1));
        assertTrue(currentPlayer.hasHandCard(penaltyCard2));
    }

    @Test
    void GivenTwoCards_WhenPlayedWithSayingUno_ShouldNotReceivePenalty() {
        var currentPlayer = players[0];
        var penaltyCard1 = CardTestFactory.createNumberCard(1, CardColor.BLUE);
        var penaltyCard2 = CardTestFactory.createNumberCard(2, CardColor.BLUE);
        var cardToPlay = CardTestFactory.createNumberCard(3, CardColor.GREEN);
        var topCard = CardTestFactory.createNumberCard(3, CardColor.RED);
        currentPlayer.addToHandCards(CardTestFactory.createSkipCard());
        var game = createGame(cardToPlay, penaltyCard1, penaltyCard2, topCard);
        game.playCard(currentPlayer.getId(), cardToPlay, true);
        assertGameState(game, cardToPlay, "2");
        assertEquals(1, currentPlayer.getHandCards().count());
        assertFalse(game.isOver());
    }

    @Test
    void WhenPlayedLastCard_GameShouldBeOver() {
        var currentPlayer = players[0];
        var cardToPlay = CardTestFactory.createNumberCard(3, CardColor.GREEN);
        var topCard = CardTestFactory.createNumberCard(3, CardColor.RED);
        var game = createGame(cardToPlay, topCard);
        game.playCard(currentPlayer.getId(), cardToPlay);
        assertEquals(0, currentPlayer.getHandCards().count());
        assertTrue(game.isOver());
        assertEquals(currentPlayer.getId(), game.getWinner().getId());
    }

    private Game createGame(AbstractCard cardToPlay, AbstractCard... drawPileCards) {
        var drawPile = createDrawPile(drawPileCards);
        var game = new Game(drawPile, playersIterator);
        var cardToAdd = CardUtil.isWildCard(cardToPlay)
                ? CardTestFactory.createWildCard(cardToPlay.getType())
                : cardToPlay;
        playersIterator.getCurrentPlayer().addToHandCards(cardToAdd);
        return game;
    }

    private DrawPile createDrawPile(AbstractCard... cards) {
        return new DrawPile(Arrays.asList(cards));
    }

    private void playCardFromCurrentPlayer(Game game, AbstractCard cardToPlay) {
        game.playCard(playersIterator.getCurrentPlayer().getId(), cardToPlay);
    }

    private void assertGameState(Game game, AbstractCard expectedTopCard, String expectedCurrentPlayer) {
        assertEquals(expectedTopCard, game.peekTopCard());
        assertEquals(expectedCurrentPlayer, game.getCurrentPlayer().getName());
    }
}
