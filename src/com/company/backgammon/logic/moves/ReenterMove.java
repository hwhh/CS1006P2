package com.company.backgammon.logic.moves;

import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;

public class ReenterMove extends Move {

    private final int to;

    public ReenterMove(int to, int requiredMove, Board.CounterType playerType) {
        super(requiredMove, playerType);
        this.to = to;
    }

    public int getTo() {
        return to;
    }

    @Override
    public void applyOn(Game game) {
        game.reEnter(playerType,to);
    }

    @Override
    public void applyOn(Board board) {
        board.reEnter(playerType,to);
    }

    @Override
    public String toNetworkMessage() {
        if(playerType == Board.CounterType.RED) {
            return "(0|" + (to+1) + ")";
        }
        else {
            return "(25|" + (to+1) + ")";
        }
    }

    @Override
    public boolean isValidOn(Board board) {
        return board.isValidReentry(playerType,to);
    }
}
