package com.company.backgammon.logic;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.moves.BearOffMove;
import com.company.backgammon.logic.moves.BoardMove;
import com.company.backgammon.logic.moves.Move;
import com.company.backgammon.logic.moves.ReenterMove;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Board {
    private static final int[] INITIAL_BOARD = {-2, 0, 0, 0, 0, 5, 0, 3, 0, 0, 0,-5, 5, 0, 0, 0,-3, 0,-5, 0, 0, 0, 0, 2};
    private static final int[] TEST_BOARD = {3, 3, 3, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -2, -2, -2, -3, -3, -3};

    // Holding negative values for red counters and positive values
    // for white counters
    private int[] points = TEST_BOARD;

    private int redBearOff = 0;
    private int whiteBearOff = 0;

    private int whiteCaptured = 0;
    private int redCaptured = 0;

    private List<Integer> availableMoves = new ArrayList<>();

    public Board(){}

    public Board(Board board) {
        this.points=board.points.clone();
        this.redBearOff=board.redBearOff;
        this.whiteBearOff=board.whiteBearOff;
        this.whiteCaptured=board.whiteCaptured;
        this.redCaptured=board.redCaptured;
        this.availableMoves=new ArrayList<>(board.availableMoves);
    }

    int neededDiceForReentryAt(int pos, Board.CounterType playerCounter) {
        if(playerCounter == Board.CounterType.WHITE) {
            return 6 - (pos - 18);
        }
        else if(playerCounter == Board.CounterType.RED) {
            return pos + 1;
        }
        return 10;
    }

    int neededDiceForBearingOff(int from, Board.CounterType playerCounter) {
        if(playerCounter == Board.CounterType.RED) {
            return (6 - (from - 18));
        }
        else if(playerCounter == Board.CounterType.WHITE) {
            return (from + 1);
        }
        return 10;
    }

    public void computeAvailableMoves(int diceValue1, int diceValue2) {
        //add the values of the dice to list
        availableMoves.clear();
        if(diceValue1 == diceValue2) {
            for(int i = 0; i < 4; ++i) {
                availableMoves.add(diceValue1);
            }
        }
        else {
            availableMoves.add(diceValue1);
            availableMoves.add(diceValue2);
        }
    }



    public void reEnter(CounterType playerType, int endPos) {
        //update attributes that store info on captured counters if counter is reenetered
        if(isCaptureMove(playerType,endPos)) {
            capturePiece(endPos);
        }
        if(playerType == CounterType.RED) {
            redCaptured--;
            points[endPos]--;
        }
        else if(playerType == CounterType.WHITE) {
            whiteCaptured--;
            points[endPos]++;
        }
    }

    boolean isCaptureMove(Board.CounterType pieceType, int to) {
        //Check to see if the user has canted an opponents piece
        Board.CounterType targetCounterType = getCounterTypeAt(to);
        if(targetCounterType == Board.CounterType.NONE){
            return false;
        }

        if(targetCounterType != pieceType) {
            if(getCountersAt(to) == 1) {
                return true;
            }
        }
        return false;
    }


    //Move validation checks
    public boolean isValidReentry(CounterType playerType, int to) {
        int neededDice = neededDiceForReentryAt(to,playerType);
        if(availableMoves.contains(neededDice)) {
            if(isCaptureMove(playerType,to)) {
                return true;
            }
            if(getCounterTypeAt(to) == Board.CounterType.NONE ||
                    getCounterTypeAt(to) == playerType) {
                return true;
            }
        }
        return false;
    }

    public boolean isMoveValid(CounterType playerCounterType, int from, int to, boolean verbose) {

        CounterType pieceCounterType = getCounterTypeAt(from);
        if(from == to) {
            if(verbose) {
                Logger.error(this, "Move failed because it moved to the same place");
            }
            return false;
        }
        if(pieceCounterType != playerCounterType) {
            if(verbose) {
                Logger.error(this, "Move failed because the piece to be moved is different than the player's\nPlayer: " + playerCounterType + " Piece: " + pieceCounterType);
                for(int i = 2; i < 5; ++i) {
                    Logger.error(this, Thread.currentThread().getStackTrace()[i].toString());
                }
            }
            return false;
        }
        if(hasPiecesCaptured(playerCounterType)) {
            if(verbose) {
                Logger.error(this, "The player has captured pieces.");
            }
            return false;
        }

        if(pieceCounterType == Board.CounterType.RED) {
            if(to < from) {
                if(verbose) {
                    Logger.error(this, "The player tried to move in the opposite direction.");
                }
                return false;
            }

            if(getCounterTypeAt(to) == Board.CounterType.WHITE) {
                if(getCountersAt(to) != 1) {
                    if(verbose) {
                        Logger.error(this, "The player tried to move to an occupied spot.");
                    }
                    return false;
                }
            }
        }
        else if(pieceCounterType == Board.CounterType.WHITE) {
            if(to > from) {
                if(verbose) {
                    Logger.error(this, "The player tried to move in the opposite direction.");
                }
                return false;
            }

            if(getCounterTypeAt(to) == Board.CounterType.RED) {
                if(getCountersAt(to) != 1) {
                    if(verbose) {
                        Logger.error(this, "The player tried to move to an occupied spot.");
                    }
                    return false;
                }
            }
        }
        int distance = Math.abs(from-to);
        if(!availableMoves.contains(distance)) {
            if(verbose) {
                Logger.error(this, "The player doesn't have enough moves.");
            }
            return false;
        }

        return true;
    }

    public void removeMove(Integer integer) {
        availableMoves.remove(integer);
    }

    public List<Integer> getAvailableMoves() {
        return new ArrayList<>(availableMoves);
    }

    public int getCaptured(CounterType myType) {
        if(myType == CounterType.RED) {
            return getRedCaptured();
        }
        else if(myType == CounterType.WHITE){
            return getWhiteCaptured();
        }
        return 0;
    }

    public enum CounterType {
        RED,
        WHITE,
        NONE
    }

    public static Board.CounterType otherType(Board.CounterType counterType) {
        if(counterType == Board.CounterType.RED)return Board.CounterType.WHITE;
        return Board.CounterType.RED;
    }

    public int getWhiteBornOff() {
        return whiteBearOff;
    }

    public int getRedBornOff() {
        return redBearOff;
    }

    public List<Integer> getHomeIndicesOf(CounterType type) {
        if(type == CounterType.RED) {
            return Arrays.asList(23,22,21,20,19,18);
        }
        else if(type == CounterType.WHITE) {
            return Arrays.asList(0,1,2,3,4,5);
        }
        return Collections.emptyList();
    }

    public void capturePiece(int from) {
        if(getCounterTypeAt(from) == CounterType.RED) {
            points[from]++;
            redCaptured++;
        }
        else if(getCounterTypeAt(from) == CounterType.WHITE) {
            points[from]--;
            whiteCaptured++;
        }
    }

    public int getRedCaptured() {
        return redCaptured;
    }

    public int getWhiteCaptured() {
        return whiteCaptured;
    }


    // Get the number of counters at a specified position.
    public int getCountersAt(int pos) {
        return Math.abs(points[pos]);
    }


    // Get the type of the counters at a specified position.
    public CounterType getCounterTypeAt(int pos) {
        synchronized (points) {
            if (points[pos] < 0) {
                return CounterType.RED;
            } else if (points[pos] > 0) {
                return CounterType.WHITE;
            }
            return CounterType.NONE;
        }
    }

    // Move counter from one position to another.
    // Note: this should only be called from Game after move validation
    // has been completed.
    public void moveCounter(int from, int to) {

        synchronized (points) {
            CounterType moveType = getCounterTypeAt(from);
            if (moveType.equals(CounterType.RED)) {
                points[from]++;
                points[to]--;
            } else {
                points[from]--;
                points[to]++;
            }
        }
    }

    // Get the amount of pieces removed for each counter type.
    public int getBearOff(CounterType type) {
        if(type.equals(CounterType.RED)) {
            return redBearOff;
        }
        else {
            return whiteBearOff;
        }
    }

    public void bearOff(int from) {
        synchronized (points) {
            CounterType type = getCounterTypeAt(from);
            if (type.equals(CounterType.RED)) {
                points[from]++;
                redBearOff++;
            } else {
                points[from]--;
                whiteBearOff++;
            }
        }
    }

    boolean hasPiecesCaptured(CounterType playerType) {
        if (playerType == Board.CounterType.WHITE) {
            return getWhiteCaptured() > 0;
        } else if (playerType == Board.CounterType.RED) {
            return getRedCaptured() > 0;
        }
        return false;
    }

    public boolean isValidBearingOff(CounterType playerType, int from) {
        if(hasPiecesCaptured(playerType)) {
            return false;
        }

        if(getCounterTypeAt(from) != playerType) {
            return false;
        }

        int neededDice = neededDiceForBearingOff(from,playerType);
        if(canBearPiecesOff(playerType) && availableMoves.contains(neededDice)) {
            return true;
        }
        return false;
    }

    boolean canBearPiecesOff(CounterType playerType) {

        if(playerType == Board.CounterType.RED) {
            for(int i = 0; i < 18; ++i) {
                if(getCounterTypeAt(i) == playerType) {
                    return false;
                }
            }
        }
        else if(playerType == Board.CounterType.WHITE) {
            for(int i = 6; i < 24; ++i) {
                if(getCounterTypeAt(i) == playerType) {
                    return false;
                }
            }
        }
        return true;
    }

    int getNumberOfAvailableMoves() {
        return availableMoves.size();
    }


    //Method that find all the possible moves
    public List<Move> getPossibleMoves(CounterType type){
        List<Move> possibleMoves = new ArrayList<>();
        if(type == Board.CounterType.RED) {

            if(!hasPiecesCaptured(type)) {
                if(allTheSame(availableMoves)) {
                    int moveDistance = availableMoves.get(0);
                    for (int i = 0; i < 24 - moveDistance; ++i) {
                        if (isMoveValid(type, i, i + moveDistance,false)) {
                            possibleMoves.add(new BoardMove(i,i+moveDistance, moveDistance, type));
                        }
                    }
                }
                else {
                    for (int moveDistance : availableMoves) {
                        for (int i = 0; i < 24 - moveDistance; ++i) {
                            if (isMoveValid(type, i, i + moveDistance, false)) {
                                possibleMoves.add(new BoardMove(i, i + moveDistance, moveDistance, type));
                            }
                        }
                    }
                }
                if(canBearPiecesOff(type)) {
                    for(int i = 18; i < 24; ++i) {
                        if(isValidBearingOff(type,i)) {
                            possibleMoves.add(new BearOffMove(i,24-i,type));
                        }
                    }
                }
            }
            else {
                for(int i = 0; i < 6; ++i) {
                    if(isValidReentry(type,i)) {
                        possibleMoves.add(new ReenterMove(i,i+1,type));
                    }
                }
            }
        }
        else if(type == Board.CounterType.WHITE) {
            if(!hasPiecesCaptured(type)) {

                if(allTheSame(availableMoves)) {
                    int moveDistance = availableMoves.get(0);
                    for (int i = moveDistance; i < 24; ++i) {
                        if (isMoveValid(type, i, i - moveDistance,false)) {
                            possibleMoves.add(new BoardMove(i,i-moveDistance, moveDistance, type));
                        }
                    }
                }
                else {
                    for (int moveDistance : availableMoves) {
                        for (int i = moveDistance; i < 24; ++i) {
                            if (isMoveValid(type, i, i - moveDistance,false)) {
                                possibleMoves.add(new BoardMove(i,i-moveDistance, moveDistance, type));
                            }
                        }
                    }
                }

                if(canBearPiecesOff(type)) {
                    for(int i = 0; i < 6; ++i) {
                        if(isValidBearingOff(type,i)) {
                            possibleMoves.add(new BearOffMove(i,i+1,type));
                        }
                    }
                }
            }
            else {
                for(int i = 18; i < 24; ++i) {
                    if(isValidReentry(type,i)) {
                        possibleMoves.add(new ReenterMove(i,24-i,type));
                    }
                }
            }
        }
        return possibleMoves;
    }

    public List<Move> getPossibleMovesWithCounter(int counterIdx, CounterType type) {
        List<Move> possibleMoves = new ArrayList<>();
        if(type == Board.CounterType.RED) {
            if (!hasPiecesCaptured(type)) {
                if (allTheSame(availableMoves)) {
                    int moveDistance = availableMoves.get(0);
                    if (counterIdx+moveDistance<=23 ) {
                        if (isMoveValid(type, counterIdx, counterIdx + moveDistance, false)) {
                            possibleMoves.add(new BoardMove(counterIdx, counterIdx + moveDistance, moveDistance, type));
                        }
                    }
                } else {
                    for (int moveDistance : availableMoves) {
                        if (counterIdx+moveDistance<=23 ) {
                            if (isMoveValid(type, counterIdx, counterIdx + moveDistance, false)) {
                                possibleMoves.add(new BoardMove(counterIdx, counterIdx + moveDistance, moveDistance, type));
                            }
                        }
                    }
                }
                if(canBearPiecesOff(type)) {
                    if (isValidBearingOff(type, counterIdx)) {
                        possibleMoves.add(new BearOffMove(counterIdx,24-counterIdx,type));
                    }
                }
            }
            else {
                for(int i = 0; i < 6; ++i) {
                    if(isValidReentry(type,i)) {
                        possibleMoves.add(new ReenterMove(i,i+1,type));
                    }
                }
            }
        }
        else if(type == CounterType.WHITE){
            if (!hasPiecesCaptured(type)) {
                if (allTheSame(availableMoves)) {
                    int moveDistance = availableMoves.get(0);
                    if (counterIdx-moveDistance>=0 ) {
                        if (isMoveValid(type, counterIdx, counterIdx - moveDistance, false)) {
                            possibleMoves.add(new BoardMove(counterIdx, counterIdx - moveDistance, moveDistance, type));
                        }
                    }
                } else {
                    for (int moveDistance : availableMoves) {
                        if (counterIdx-moveDistance>=0 ) {
                            if (isMoveValid(type, counterIdx, counterIdx - moveDistance, false)) {
                                possibleMoves.add(new BoardMove(counterIdx, counterIdx - moveDistance, moveDistance, type));
                            }
                        }
                    }
                }
                if(canBearPiecesOff(type)) {
                    if (isValidBearingOff(type, counterIdx)) {
                        possibleMoves.add(new BearOffMove(counterIdx,counterIdx+1,type));
                    }
                }
            }
            else {
                for(int i = 18; i < 24; ++i) {
                    if(isValidReentry(type,i)) {
                        possibleMoves.add(new ReenterMove(i,24-i,type));
                    }
                }
            }
        }

        return possibleMoves;
    }


    private boolean allTheSame(List<Integer> availableMoves) {
        if(availableMoves.size() == 0) return false;

        int value = availableMoves.get(0);
        for(int val:availableMoves) {
            if(val != value) return false;
        }
        return true;
    }

    boolean playerHasPossibleMoves(CounterType type) {
        return getPossibleMoves(type).size() > 0;
    }


}
