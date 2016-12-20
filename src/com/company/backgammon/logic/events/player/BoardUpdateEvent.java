package com.company.backgammon.logic.events.player;

import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.IPlayer;

public class BoardUpdateEvent extends PlayerEvent{
    private final Board board;

    public BoardUpdateEvent(Board board) {
        this.board = board;
    }

    @Override
    public void applyOn(IPlayer player) {
        player.updateBoard(this.board);
    }
}
