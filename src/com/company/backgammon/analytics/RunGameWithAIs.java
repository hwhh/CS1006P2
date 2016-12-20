package com.company.backgammon.analytics;

import com.company.backgammon.ai.BackgammonAI;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;

import java.util.Vector;

public class RunGameWithAIs implements Runnable {

    public static Vector<Board.CounterType> results = new Vector<>();

    private final BackgammonAI ai1;
    private final BackgammonAI ai2;

    RunGameWithAIs(BackgammonAI ai1, BackgammonAI ai2) {
        this.ai1 = ai1;
        this.ai2 = ai2;
    }

    @Override
    public void run() {
        Game game = new Game(ai1,ai2);
        game.start();
        results.add(game.getWinner());
    }
}
