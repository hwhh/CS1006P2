package com.company.backgammon.ai;

import com.company.backgammon.Logger;
import com.company.backgammon.Main;
import com.company.backgammon.ai.evaluators.Evaluator;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.IPlayer;
import com.company.backgammon.logic.Turn;
import com.company.backgammon.logic.events.player.PlayerEvent;
import com.company.backgammon.logic.moves.InvalidMoveException;
import com.company.backgammon.logic.moves.Move;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class BackgammonAI implements IPlayer {
    protected Board.CounterType counterType;
    private Game game;
    private Evaluator evaluator;
    protected int movesAhead;

    public BackgammonAI(Evaluator evaluator, int movesAhead){
        this.evaluator = evaluator;
        this.movesAhead = movesAhead;
    }

    @Override
    public void setGame(Game game, int playerID, Board.CounterType type) {
        this.game = game;
        this.counterType = type;
    }

    @Override
    public void startTurn(Turn opponentTurn) {
        game.rollDices();
        Board board = game.getBoard();
        Turn turn = getBestTurn(board);
        for(Move move:turn.getMoves()) {
            try {
                game.sendMove(move);
            } catch (InvalidMoveException e) {
                Logger.error(this,"AI computed an invalid move: " + e.getMessage());
                Logger.log(this,board.toString());
            }
        }
    }

    public abstract Turn getBestTurn(Board board);

    protected List<TurnNode> getPossibleTurns(TurnNode node, Board.CounterType playerCounter) {
        if(node.getBoard().getAvailableMoves().size() == 0) {
            return Collections.singletonList(node);
        }
        else {
            List<TurnNode> possibleTurns = new ArrayList<>();

            List<Move> possibleMoves = node.getBoard().getPossibleMoves(playerCounter);
            if(possibleMoves.size() > 0) {
                for (Move move : possibleMoves) {
                    possibleTurns.addAll(getPossibleTurns(node.advance(move), playerCounter));
                }
                return possibleTurns;
            }
            else {
                return Collections.singletonList(node);
            }
        }
    }

    protected List<TurnNode> getPossibleTurns(Board board, Board.CounterType type) {
        return getPossibleTurns(new TurnNode(new Turn(), new Board(board)), type);
    }

    // This should always tend towards +oo for moves profitable to our player.
    protected double evaluateNode(TurnNode node){
        return evaluator.evaluate(node.getBoard(),counterType);
    }


    @Override
    public void endTurn() {

    }

    @Override
    public void opponentSurrendered() {

    }

    @Override
    public void gameOver(int winningPlayerID) {

    }

    @Override
    public void sendEvent(PlayerEvent event) {
        event.applyOn(this);
    }

    @Override
    public void updateBoard(Board board) {
    }
}
