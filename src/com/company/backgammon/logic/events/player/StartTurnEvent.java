package com.company.backgammon.logic.events.player;

import com.company.backgammon.logic.IPlayer;
import com.company.backgammon.logic.Turn;

public class StartTurnEvent extends PlayerEvent {

    private final Turn opponentsTurn;

    public StartTurnEvent(Turn opponentsTurn) {
        this.opponentsTurn = opponentsTurn;
    }

    @Override
    public void applyOn(IPlayer player) {
        player.startTurn(opponentsTurn);
    }
}
