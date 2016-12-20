package com.company.backgammon.logic;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.IPlayer;
import com.company.backgammon.logic.events.player.PlayerEvent;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class PlayerEventHandler extends Thread {

    private final IPlayer player;
    private final BlockingQueue<PlayerEvent> eventsToProcess= new LinkedBlockingQueue<>();

    public void sendEvent(PlayerEvent event) {
        eventsToProcess.offer(event);
    }

    public PlayerEventHandler(IPlayer player) {
        this.player = player;
    }

    @Override
    public void run() {

        try {
            while(true) {
                PlayerEvent ev = eventsToProcess.take();
                ev.applyOn(player);
            }
        } catch (InterruptedException e) {
            Logger.error(this,"Interrupted while taking events from queue.");
        }
    }
}