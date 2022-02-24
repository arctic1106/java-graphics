package com.arcticsoft.domain.game.events;

import com.arcticsoft.domain.common.DomainEvent;
import com.arcticsoft.domain.player.ImmutablePlayer;

public class GameOver extends DomainEvent {
    private final ImmutablePlayer winner;

    public GameOver(ImmutablePlayer winner) {
        this.winner = winner;
    }

    public ImmutablePlayer getWinner() {
        return winner;
    }
}
