package com.company.backgammon.ai.evaluators;

import com.company.backgammon.logic.Board;

public interface Evaluator {

    double evaluate(Board board, Board.CounterType myType);

    static Evaluator getBestEvaluator() {
        return new StaticEvaluator(100,10,30,5);
    }
}
