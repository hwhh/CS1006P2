package com.company.backgammon.logic;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.events.game.GameEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class GameEventHandler extends Thread {

    private final Game game;
    private final BlockingQueue<GameEvent> eventsToProcess= new LinkedBlockingQueue<>();

    public void sendEvent(GameEvent event) {
        Logger.log(this, event.getClass().getSimpleName() + " was offered to game");
        eventsToProcess.offer(event);
    }

    public GameEventHandler(Game game) {
        this.game = game;
    }

    @Override
    public void run() {
        try {
            while(true) {
                GameEvent ev = eventsToProcess.take();
                ev.applyOn(game);
            }
        } catch (InterruptedException e) {
            Logger.error(this,"Interrupted while taking events from queue.");
        }
    }
}