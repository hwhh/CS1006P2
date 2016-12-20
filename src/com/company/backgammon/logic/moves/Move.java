package com.company.backgammon.logic.moves;

import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;

public abstract class Move {

    protected final int requiredMove;
    protected final Board.CounterType playerType;

    Move(int requiredMove, Board.CounterType playerType) {
        this.requiredMove = requiredMove;
        this.playerType = playerType;
    }

    public abstract void applyOn(Game game) throws InvalidMoveException;
    public abstract void applyOn(Board board);
    public abstract boolean isValidOn(Board game);
    public abstract String toNetworkMessage();

    public int getRequiredMove() {
        return requiredMove;
    }
}
