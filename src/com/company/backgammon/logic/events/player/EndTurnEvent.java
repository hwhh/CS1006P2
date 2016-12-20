package com.company.backgammon.logic.events.player;

import com.company.backgammon.logic.IPlayer;

public class EndTurnEvent extends PlayerEvent{
    @Override
    public void applyOn(IPlayer player) {
        player.endTurn();
    }
}
