package com.company.backgammon.logic.events.game;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.moves.InvalidMoveException;
import com.company.backgammon.logic.moves.Move;

public class MoveEvent extends GameEvent {

    private final Move move;

    public MoveEvent(Move move) {
        this.move = move;
    }

    @Override
    public void applyOn(Game game) {
        try {
            Logger.log(this,"Available moves before move: " + game.getAvailableMoves().size());
            move.applyOn(game);
            Logger.log(this,"Available moves after move: " + game.getAvailableMoves().size());
        } catch (InvalidMoveException e) {
            Logger.error(e,e.getMessage());
        }
    }
}
