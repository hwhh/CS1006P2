package com.company.backgammon.logic.events.player;

import com.company.backgammon.logic.IPlayer;

public class OpponentSurrendedEvent extends PlayerEvent {
    @Override
    public void applyOn(IPlayer player) {
        player.opponentSurrendered();
    }
}
