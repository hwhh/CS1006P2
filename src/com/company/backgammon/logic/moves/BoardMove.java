package com.company.backgammon.logic.moves;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;

public class BoardMove extends Move{

    private final int requiredMove;
    private final int from;
    private final int to;

    public BoardMove(int from, int to, int requiredMove, Board.CounterType playerType) {
        super(requiredMove,playerType);
        this.from = from;
        this.to = to;
        this.requiredMove = requiredMove;
    }


    public int getFrom() {
        return from;
    }

    public int getTo() {
        return to;
    }

    @Override
    public void applyOn(Game game) throws InvalidMoveException {
        Logger.log(this,"By " + playerType + " from " + from + " to " + to);
        game.move(playerType,from,to);
    }

    @Override
    public void applyOn(Board board) {
        board.moveCounter(from,to);
    }

    @Override
    public String toNetworkMessage() {
        return "(" + (from+1) + "|" + (to+1) + ")";
    }

    @Override
    public boolean isValidOn(Board board) {
        return board.isMoveValid(playerType,from,to,true);
    }

}
