package com.company.backgammon.ai;


import com.company.backgammon.Logger;
import com.company.backgammon.ai.evaluators.Evaluator;
import com.company.backgammon.logic.*;
import com.company.backgammon.logic.moves.InvalidMoveException;
import com.company.backgammon.logic.moves.Move;

public class ExpectiMiniMaxAI extends BackgammonAI {

    private double[] Probability = new double[]{11,12,14,15,15,17,6,6,5,3,2,1,1,1,1,1};

    public ExpectiMiniMaxAI(Evaluator evaluator, int movesAhead){
        super(evaluator,movesAhead);
    }

    public Turn getBestTurn(Board board) {
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;
        double bestValue = Double.NEGATIVE_INFINITY;
        Turn bestTurn = null;
        for(TurnNode node:getPossibleTurns(board, counterType)) {

            double currentValue = expectiMiniMax(node,movesAhead-1,Board.otherType(counterType),true, alpha,beta);
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


    public double expectiMiniMax(TurnNode node, int depth, Board.CounterType type, boolean isChance, double alpha, double beta) {
        if (depth == 0) {
            return evaluateNode(node);
        }
        /* *Note that for random nodes, there must be a known probability of reaching each child.
        * (For most games of chance, child nodes will be equally-weighted, which means the return
        * * value can simply be the average of all child values*/
        if(isChance) {
            double bestValue = 0;
            for(int i = 1; i <= 6; ++i) {
                for(int j = 1; j <= 6; ++j) {
                    bestValue += (Probability[(i-1)+(j-1)] * expectiMiniMax(node.advance(i,j), depth - 1, Board.otherType(type), false, bestValue, beta));
                }
            }
            return bestValue;
        }
        else {
            if (counterType == type) {
                double bestValue = Double.NEGATIVE_INFINITY;
                for (TurnNode child : getPossibleTurns(node.getBoard(), type)) {
                    bestValue = Math.max(bestValue, expectiMiniMax(child, depth - 1, type, true, alpha, beta));
                    alpha=Math.max(alpha,bestValue);
                    if (beta <= alpha){
                        break;
                    }
                }
                return bestValue;
            } else {
                double bestValue = Double.POSITIVE_INFINITY;
                for (TurnNode child : getPossibleTurns(node.getBoard(), type)) {
                    bestValue = Math.min(bestValue, expectiMiniMax(child, depth - 1, type, true, alpha, beta));
                    beta=Math.min(beta,bestValue);
                    if (beta <= alpha){
                        break;
                    }
                }
                return bestValue;
            }
        }
    }

//    public List<Integer> getChanceNode(){
//        Integer []probabilityNodes = {11,12,14,15,15,16,6,6,5,3,2,3,1,1,1,1,1};
//        List<Integer> chanceNodes = new ArrayList<Integer>(Arrays.asList(probabilityNodes));
//        return chanceNodes;
//    }
}


