package com.company.backgammon.logic;

import com.company.backgammon.ui.BackGammonGUI;

public class GameWithSpectator extends Game{
    private final BackGammonGUI spectator;

    public GameWithSpectator(IPlayer player1, IPlayer player2, BackGammonGUI spectator) {
        super(player1, player2);
        this.spectator = spectator;
        spectator.setGame(this,-1, Board.CounterType.NONE);
    }

    @Override
    public void boardUpdated() {
        spectator.updateBoard(getBoard());
        super.boardUpdated();
    }
}
