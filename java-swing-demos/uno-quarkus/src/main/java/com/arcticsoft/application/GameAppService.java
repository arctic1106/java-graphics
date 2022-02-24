package com.arcticsoft.application;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.game.Game;
import com.arcticsoft.domain.game.GameBuilder;
import com.arcticsoft.domain.player.ImmutablePlayer;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameAppService implements IGameAppService {
    private final Game game;

    public GameAppService() {
        game = new GameBuilder()
                .withPlayer("Player 1")
                .withPlayer("Player 2")
                .build();
    }

    @Override
    public List<PlayerInfoDTO> getPlayerInfos() {
        return game.getPlayers()
                .map(p -> new PlayerInfoDTO(p.getId(), p.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public PlayerInfoDTO getCurrentPlayer() {
        var currentPlayer = game.getCurrentPlayer();
        return new PlayerInfoDTO(currentPlayer.getId(), currentPlayer.getName());
    }

    @Override
    public Stream<AbstractCard> getHandCards(UUID playerId) {
        return game.getHandCards(playerId);
    }

    @Override
    public void playCard(UUID playerId, AbstractCard card, boolean hasSaidUno) {
        game.playCard(playerId, card, hasSaidUno);
    }

    @Override
    public void drawCard(UUID playerId) {
        game.drawCard(playerId);
    }

    @Override
    public AbstractCard peekTopCard() {
        return game.peekTopCard();
    }

    @Override
    public boolean isGameOver() {
        return game.isOver();
    }

    @Override
    public ImmutablePlayer getWinner() {
        return game.getWinner();
    }
}
