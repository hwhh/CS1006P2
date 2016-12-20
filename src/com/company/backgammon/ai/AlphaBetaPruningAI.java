package com.company.backgammon.ai;

import com.company.backgammon.ai.evaluators.Evaluator;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Turn;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlphaBetaPruningAI extends BackgammonAI {
    public AlphaBetaPruningAI(Evaluator evaluator, int movesAhead) {
        super(evaluator, movesAhead);
    }

    private double alphabeta(TurnNode node, int depth, double alpha, double beta, Board.CounterType type) {
        if(depth == 0) {
            return evaluateNode(node);
        }
        if(counterType == type) {
            double bestValue = Double.NEGATIVE_INFINITY;
            for(TurnNode child:getPossibleTurnsForAllPossibleDices(node.getBoard(),type)) {
                bestValue = Math.max(bestValue, alphabeta(child,depth-1,alpha,beta,Board.otherType(type)));
                alpha=Math.max(alpha,bestValue);
                if (beta <= alpha){
                    break;
                }
            }
            return bestValue;
        }
        else {
            double bestValue = Double.POSITIVE_INFINITY;
            for(TurnNode child:getPossibleTurnsForAllPossibleDices(node.getBoard(),type)) {
                bestValue = Math.min(bestValue, alphabeta(child,depth-1,alpha,beta,Board.otherType(type)));
                beta=Math.min(beta,bestValue);
                if (beta <= alpha){
                    break;
                }
            }
            return bestValue;
        }
    }

    public List<TurnNode> getPossibleTurnsForAllPossibleDices(Board board, Board.CounterType type) {
        List<TurnNode> possibleMoves = new ArrayList<>();
        for(int i = 1; i <= 6; ++i) {
            for(int j = i; j < 6; ++j) {
                Turn newTurn = new Turn();
                newTurn.setDices(Arrays.asList(i, j));
                Board newBoard = new Board(board);
                newBoard.computeAvailableMoves(i, j);
                possibleMoves.addAll(getPossibleTurns(new TurnNode(newTurn, newBoard), type));
            }
        }
        return possibleMoves;
    }

    @Override
    public Turn getBestTurn(Board board) {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double bestValue = Double.NEGATIVE_INFINITY;
        Turn bestTurn = null;
        for(TurnNode node:getPossibleTurns(board, counterType)) {

            double currentValue = alphabeta(node,movesAhead-1,alpha,beta,Board.otherType(counterType));
            if(currentValue > bestValue) {
                bestValue = currentValue;
                bestTurn = node.getTurn();
            }
            alpha=Math.max(alpha,bestValue);
            if (beta <= alpha){
                break;
            }
        }
        //Logger.log(this,"AI computed value for " + counterType + ": " + bestValue);
        return bestTurn;
    }
}
