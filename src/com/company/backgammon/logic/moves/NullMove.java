package com.company.backgammon.logic.moves;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;

public class NullMove extends Move {
    public NullMove(Board.CounterType playerType) {
        super(-1, playerType);
    }

    @Override
    public void applyOn(Game game) throws InvalidMoveException {
        Logger.log(this,"Nothing");
        // do nothing
    }

    @Override
    public void applyOn(Board board) {
        Logger.log(this, "Nothing on board");
    }

    @Override
    public boolean isValidOn(Board board) {
        return true;//always valid, I think. Doesn't really matter
    }

    @Override
    public String toNetworkMessage() {
        return "(-1|-1)";
    }
}
