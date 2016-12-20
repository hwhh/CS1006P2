package com.company.backgammon.logic.moves;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;

public class BearOffMove extends Move{

    private final int from;

    public BearOffMove(int from, int requiredMove, Board.CounterType type) {
        super(requiredMove, type);
        this.from = from;
    }

    @Override
    public void applyOn(Game game){
            Logger.log(this, "By " + playerType + " from " + from);
            game.bearOff(from, playerType);
            game.boardUpdated();
    }

    @Override
    public void applyOn(Board board) {
        board.bearOff(from);
    }

    @Override
    public String toNetworkMessage() {
        if(playerType == Board.CounterType.RED) {
            return "(" + (from+1) + "|25)";
        }
        else {
            return "(" + (from+1) + "|0)";
        }
    }

    @Override
    public String toString() {
        return playerType + " " + toNetworkMessage();
    }

    @Override
    public boolean isValidOn(Board board) {
        return board.isValidBearingOff(playerType,from);
    }
}
