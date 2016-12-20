package com.company.backgammon.network;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.IPlayer;
import com.company.backgammon.logic.Turn;
import com.company.backgammon.logic.events.player.PlayerEvent;
import com.company.backgammon.logic.moves.InvalidMoveException;
import com.company.backgammon.logic.moves.Move;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public abstract class NetworkPlayer implements IPlayer {

    public static final String USER_NAME = "nemesis";
    public static final String HELLO_MSG = "hello";
    public static final String YOU_WIN_MSG = "you-win";
    public static final String BYE_MSG = "bye";
    public static final String NEW_GAME_MSG = "newgame";
    public static final String READY_MSG = "ready";
    public static final String PASS_MSG = "pass";
    public static final String REJECT_MSG = "reject";

    private Socket socket;
    private InputStream input;
    private OutputStream output;

    protected Game game;
    protected Board.CounterType counterType;
    protected int playerId;

    public NetworkPlayer(Socket socket) throws IOException {
        this.socket = socket;
        this.input = socket.getInputStream();
        this.output = socket.getOutputStream();
    }

    String receiveNetworkMessage() throws IOException {
        byte[] buff = new byte[100];
        input.read(buff);
        return new String(buff).trim();
    }

    void sendNetworkMessage(String message) throws IOException {
        output.write(message.getBytes(),0,message.length());
    }

    protected void closeConnection() throws IOException {
        socket.close();
        game.disconnect(playerId);
    }

    protected void processNetworkTurn(String message) throws IOException {
        if(message.startsWith(YOU_WIN_MSG)) {
            Logger.log(this,"We won!");
            closeConnection();
        }
        else {
            Turn turn = Turn.parseMessage(message, counterType);
            game.updateDices(turn.getDices().get(0), turn.getDices().get(1));
            try {
                for (Move move : turn.getMoves()) {
                    game.sendMove(move);
                }
            } catch (InvalidMoveException e) {
                Logger.error(this, e.getMessage());
            }
        }
    }

    @Override
    public void opponentSurrendered() {
        try {
            closeConnection();
        } catch (IOException e) {
            Logger.error(this,"Something happened while closing the connection. Don't care.");
        }
    }

    @Override
    public void endTurn() {}


    @Override
    public void gameOver(int winningPlayerID) {
        if(winningPlayerID != playerId) {
            try {
                sendNetworkMessage(YOU_WIN_MSG + "; " + BYE_MSG);
                closeConnection();
            } catch (IOException e) {
                Logger.error(this,"Failed at closing the connection after loss");
            }
        }
    }

    @Override
    public void sendEvent(PlayerEvent event) {event.applyOn(this);}

    @Override
    public void updateBoard(Board board) {}
}
