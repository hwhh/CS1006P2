package com.company.backgammon.logic.events.player;

import com.company.backgammon.logic.IPlayer;

public class GameOverEvent extends PlayerEvent{

    private final int winningPlayerID;

    public GameOverEvent(int winningPlayerID) {
        this.winningPlayerID = winningPlayerID;
    }

    @Override
    public void applyOn(IPlayer player) {
        player.gameOver(winningPlayerID);
    }
}
