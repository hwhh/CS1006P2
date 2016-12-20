package com.company.backgammon.logic;


import com.company.backgammon.Logger;
import com.company.backgammon.logic.moves.InvalidMoveException;
import com.company.backgammon.logic.moves.Move;
import com.company.backgammon.logic.moves.NullMove;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Game {
    private static final Board.CounterType[] playerCounter = {Board.CounterType.RED, Board.CounterType.WHITE};
    private Turn lastTurn = new Turn();

    public Board getBoard() {
        return new Board(board);
    }

    private final Board board;

    private final IPlayer[] players;
    private Dice[] dices = new Dice[2];

    private int currentPlayerIdx;

    private final Random randomSource = new Random();

    private boolean currentPlayerRolledAlready;

    private int coinToss() {
        return randomSource.nextInt(2);
    }
    private Board.CounterType winner = Board.CounterType.NONE;

    public Game(IPlayer player1, IPlayer player2) {

        dices[0] = new Dice();
        dices[1] = new Dice();

        this.board = new Board();

        player1.setGame(this,0,getCounterTypeOfPlayer(0));
        player2.setGame(this,1,getCounterTypeOfPlayer(1));

        this.players = new IPlayer[]{player1,player2};
    }

    public void start() {
        start(coinToss());
    }

    public void start(int firstPlayer) {
        currentPlayerIdx = firstPlayer;
        boardUpdated();
        lastTurn = new Turn();
        players[currentPlayerIdx].startTurn(null);
    }

    private boolean didPlayerWin( Board.CounterType counterType) {
        return board.getBearOff(counterType) == 15;
    }

    private void switchPlayers() {
        Board.CounterType playerType = getCounterTypeOfPlayer(currentPlayerIdx);
        players[currentPlayerIdx].endTurn();
        for(int i = 0; i < board.getNumberOfAvailableMoves(); ++i) {
            lastTurn.addMove(new NullMove(playerType));
        }


        if(!didPlayerWin(playerType)) {
            currentPlayerIdx = 1-currentPlayerIdx;
            Logger.log(this,getCounterTypeOfPlayer(currentPlayerIdx) + "'s turn\n");
            currentPlayerRolledAlready = false;
            Turn turnToSend = new Turn(lastTurn);
            lastTurn = new Turn();
            players[currentPlayerIdx].startTurn(turnToSend);
        }
        else {
            winner = getCounterTypeOfPlayer(currentPlayerIdx);
            players[currentPlayerIdx].gameOver(currentPlayerIdx);
            players[1-currentPlayerIdx].gameOver(currentPlayerIdx);
        }
    }



    public void bearOff(int from, Board.CounterType playerType) {
        synchronized (board) {
            board.bearOff(from);
            consumeMove(board.neededDiceForBearingOff(from, playerType));
            boardUpdated();
        }
    }

    public void reEnter(Board.CounterType playerType, int endPos) {
        synchronized (board) {
            int neededMove = board.neededDiceForReentryAt(endPos, playerType);
            board.reEnter(playerType, endPos);
            consumeMove(neededMove);
        }
        boardUpdated();
    }

    public boolean isMoveValid() {
        return currentPlayerRolledAlready;
    }



    private void consumeMove(int move) {
        synchronized (board) {
            board.removeMove(move);
            boardUpdated();
            if (board.getNumberOfAvailableMoves() == 0 || !curPlayerHasPossibleMoves()) {
                switchPlayers();
            }
        }
    }

    public boolean canRoll(int playerID) {
        return playerID == currentPlayerIdx && !currentPlayerRolledAlready;
    }

    public void rollDices() {
        dices[0].roll();
        dices[1].roll();
        updateDices(dices[0].getValue(),dices[1].getValue());
    }

    public void updateDices(int dice1, int dice2) {
        lastTurn.setDices(Arrays.asList(dice1,dice2));
        board.computeAvailableMoves(dice1,dice2);

        currentPlayerRolledAlready = true;

        boardUpdated();
        if(!curPlayerHasPossibleMoves()) {
            switchPlayers();
        }
    }

    private boolean curPlayerHasPossibleMoves() {
        return board.playerHasPossibleMoves(getCounterTypeOfPlayer(currentPlayerIdx));
    }

    public synchronized void move(Board.CounterType playerID, int startPos, int endPos) {

        synchronized (board) {
            Board.CounterType pieceToBeMovedType = board.getCounterTypeAt(startPos);
            if (isMoveValid()) {
                if (board.isCaptureMove(pieceToBeMovedType, endPos)) {
                    Logger.log(this, "Capture move by " + playerID + " at " + endPos);
                    board.capturePiece(endPos);
                }
                board.moveCounter(startPos, endPos);
                consumeMove(Math.abs(endPos - startPos));
                boardUpdated();
            }
        }
    }

    public void boardUpdated() {
        players[0].updateBoard(board);
        players[1].updateBoard(board);
    }

    public Board.CounterType getCounterTypeOfPlayer(int playerIdx) {
        return playerCounter[playerIdx];
    }

    public List<Integer> getAvailableMoves() {
        return board.getAvailableMoves();
    }

    public synchronized void sendMove(Move move) throws InvalidMoveException {
        synchronized (board) {
            if (move.isValidOn(board)) {
                lastTurn.addMove(move);
                move.applyOn(this);
            } else {
                throw new InvalidMoveException(move);
            }
        }
    }

    public void disconnect(int playerID) {
        Logger.log(this,getCounterTypeOfPlayer(playerID) + " disconnected!");
        players[1-playerID].opponentSurrendered();
    }

    public Board.CounterType getWinner() {
        return winner;
    }
}
