package com.company.backgammon.logic;


/*
* Todo: Make player interfaces care about what player they are.
* e.g: flip board so that each player has the home corner at the bottom of the screen / at positions 0 to 5.
*/

import com.company.backgammon.logic.events.player.PlayerEvent;

public interface IPlayer {



    void setGame(Game game, int playerID, Board.CounterType type);

    void updateBoard(Board board);

    void startTurn(Turn opponentTurn);

    void endTurn();

    void opponentSurrendered();

    void gameOver(int winningPlayerID);

    void sendEvent(PlayerEvent event);
}
