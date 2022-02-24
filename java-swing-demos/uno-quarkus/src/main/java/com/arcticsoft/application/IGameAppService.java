package com.arcticsoft.application;

import com.arcticsoft.domain.card.AbstractCard;
import com.arcticsoft.domain.player.ImmutablePlayer;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

public interface IGameAppService extends Serializable {

    List<PlayerInfoDTO> getPlayerInfos();

    PlayerInfoDTO getCurrentPlayer();

    Stream<AbstractCard> getHandCards(UUID playerId);

    void playCard(UUID playerId, AbstractCard card, boolean hasSaidUno);

    void drawCard(UUID playerId);

    AbstractCard peekTopCard();

    boolean isGameOver();

    ImmutablePlayer getWinner();
}
