package com.company.backgammon.logic.events.game;

import com.company.backgammon.logic.Game;

public abstract class GameEvent {
    public abstract void applyOn(Game game);
}
