package com.company.backgammon.logic;



public class GameAITest extends Game {
    private Turn lastTurn = new Turn();


    public GameAITest(IPlayer player1, IPlayer player2) {
        super(player1,player2);

    }

    public void computeAvailableMoves(int diceValue1, int diceValue2) {
        super.getBoard().computeAvailableMoves(diceValue1,diceValue2);

    }
}
