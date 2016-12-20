package com.company.backgammon.logic.events.player;

import com.company.backgammon.logic.IPlayer;

public abstract class PlayerEvent {
    public abstract void applyOn(IPlayer player);
}