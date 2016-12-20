package com.company.backgammon.analytics;

import com.company.backgammon.Logger;
import com.company.backgammon.ai.AlphaBetaPruningAI;
import com.company.backgammon.ai.ExpectiMiniMaxAI;
import com.company.backgammon.ai.RandomAI;
import com.company.backgammon.ai.evaluators.StaticEvaluator;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.GameWithSpectator;
import com.company.backgammon.logic.moves.BearOffMove;
import com.company.backgammon.logic.moves.BoardMove;
import com.company.backgammon.logic.moves.NullMove;
import com.company.backgammon.logic.moves.ReenterMove;
import com.company.backgammon.ui.BackGammonGUI;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class DifferenceInDepthTest {

    public static void main(String[] args) throws InterruptedException {

        Logger.muteClass(BearOffMove.class);
        Logger.muteClass(ReenterMove.class);
        Logger.muteClass(BoardMove.class);
        Logger.muteClass(NullMove.class);
        Logger.muteClass(Game.class);

        ExecutorService executorService = Executors.newFixedThreadPool(12);

        for(int i = 0; i < 10; ++i) {
            ExpectiMiniMaxAI ai1 = new ExpectiMiniMaxAI(new StaticEvaluator(100, 10, 30, 5), 3);
            ExpectiMiniMaxAI ai2 = new ExpectiMiniMaxAI(new StaticEvaluator(100, 10, 30, 5), 1);
            executorService.submit(new RunGameWithAIs(ai1,ai2));
        }

        executorService.shutdown();


        while(!executorService.isTerminated()){Thread.sleep(100);}

        int firstAIWins = 0;
        int secondAIWins = 0;
        int errorWins = 0;

        for(Board.CounterType type:RunGameWithAIs.results) {
            if(type == Board.CounterType.RED){
                firstAIWins ++;
            }
            else if(type == Board.CounterType.WHITE){
                secondAIWins ++;
            }
            else {
                errorWins++;
            }
        }

        System.out.println("AI1 won: " + firstAIWins);
        System.out.println("AI2 won: " + secondAIWins);
        System.out.println("Bad games : " + errorWins);
    }
}
