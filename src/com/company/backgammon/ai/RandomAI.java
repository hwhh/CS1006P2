package com.company.backgammon.ai;

import com.company.backgammon.ai.evaluators.Evaluator;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Turn;

import java.util.List;
import java.util.Random;

public class RandomAI extends BackgammonAI {

    private Random randomSource = new Random();
    public RandomAI(Evaluator evaluator, int movesAhead) {
        super(evaluator, movesAhead);
    }

    @Override
    public Turn getBestTurn(Board board) {
        List<TurnNode> turns = getPossibleTurns(board,counterType);

        int choice = randomSource.nextInt(turns.size());
        return turns.get(choice).getTurn();
    }
}
