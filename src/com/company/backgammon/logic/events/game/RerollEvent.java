package com.company.backgammon.logic.events.game;

import com.company.backgammon.logic.Game;

public class RerollEvent extends GameEvent {

    @Override
    public void applyOn(Game game) {
        game.rollDices();
    }
}
