package com.company.backgammon.ai;

import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Turn;
import com.company.backgammon.logic.moves.Move;

import java.util.Arrays;

public class TurnNode {

    private Turn turn;
    private Board board;

    public TurnNode(Turn turn, Board board) {
        this.turn = turn;
        this.board = board;
    }

    public Turn getTurn() {
        return turn;
    }

    public Board getBoard() {
        return board;
    }

    public TurnNode advance(int dice1, int dice2) {
        Turn newTurn = new Turn(turn);
        Board newBoard = new Board(board);
        newTurn.setDices(Arrays.asList(dice1,dice2));
        board.computeAvailableMoves(dice1,dice2);
        return new TurnNode(newTurn,newBoard);
    }

    public TurnNode advance(Move move) {
        Turn newTurn = new Turn(turn);
        Board newBoard = new Board(board);
        newTurn.addMove(move);
        move.applyOn(newBoard);
        newBoard.removeMove(move.getRequiredMove());
        return new TurnNode(newTurn,newBoard);
    }

}
