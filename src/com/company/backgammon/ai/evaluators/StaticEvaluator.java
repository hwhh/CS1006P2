package com.company.backgammon.ai.evaluators;

import com.company.backgammon.ai.evaluators.Evaluator;
import com.company.backgammon.logic.Board;

public class StaticEvaluator implements Evaluator {
    private final double captureWeight;
    private final double bearedOffWeight;
    private final double uncoveredWeight;
    private double distanceToTravelWeight;

    public StaticEvaluator(double captureWeight, double bearedOffWeight, double uncoveredWeight, double distanceToTravelWeight) {
        this.captureWeight = captureWeight;
        this.bearedOffWeight = bearedOffWeight;
        this.uncoveredWeight = uncoveredWeight;
        this.distanceToTravelWeight = distanceToTravelWeight;
    }

    //DONE: sdistance from home
    //TODO:probability of bear off
    //DONE: dent evaluate cover pieces if can no longer get captured
    //

    @Override
    public double evaluate(Board board, Board.CounterType myType) {
        double totalScore = 0;

        totalScore -= captureWeight*board.getCaptured(myType);
        totalScore += captureWeight*board.getCaptured(Board.otherType(myType));

        totalScore += bearedOffWeight*board.getBearOff(myType);
        totalScore -= bearedOffWeight*board.getBearOff(Board.otherType(myType));


        if(myType == Board.CounterType.RED) {
            for (int i = 0; i < 24; ++i) {
                if (board.getCounterTypeAt(i) == myType) {
                    totalScore -= board.getCountersAt(i) * Math.max((i+1-6),0) *distanceToTravelWeight;
                }
                else if(board.getCounterTypeAt(i) == Board.otherType(myType)){
                    totalScore += board.getCountersAt(i) * Math.max((24-i-6),0) *distanceToTravelWeight;
                }
            }
        }
        else if(myType == Board.CounterType.WHITE) {
            for (int i = 0; i < 24; ++i) {
                if (board.getCounterTypeAt(i) == myType) {
                    totalScore -= board.getCountersAt(i) * Math.max((24-i-6),0) * distanceToTravelWeight;
                }
                else if(board.getCounterTypeAt(i) == Board.otherType(myType)){
                    totalScore += board.getCountersAt(i) * Math.max((i+1-6),0)* distanceToTravelWeight;
                }
            }
        }

        if(isCapturePossible(board)) {
            for(int i = 0; i < 24; ++i) {
                if(board.getCounterTypeAt(i) == myType && board.getCountersAt(i) == 1) {
                    totalScore -= uncoveredWeight;
                }
            }
            for(int i = 0; i < 24; ++i) {
                if(board.getCounterTypeAt(i) == Board.otherType(myType) && board.getCountersAt(i) == 1) {
                    totalScore += uncoveredWeight;
                }
            }
        }
        return totalScore;
    }

    private boolean isCapturePossible(Board board) {
        if(board.getRedCaptured() != 0 || board.getWhiteCaptured() != 0) return true;

        int redPieceCount = board.getRedBornOff();

        for(int i = 0; i < 24; ++i) {
            if(board.getCounterTypeAt(i) == Board.CounterType.RED) {
                redPieceCount += board.getCountersAt(i);
            }
            else if(board.getCounterTypeAt(i) == Board.CounterType.WHITE) {
                return redPieceCount < 15;
            }
        }
        return true;
    }
}
