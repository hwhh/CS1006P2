package com.company.backgammon.network;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.IPlayer;
import com.company.backgammon.logic.Turn;
import com.company.backgammon.logic.moves.InvalidMoveException;
import com.company.backgammon.logic.moves.Move;
import com.company.backgammon.logic.events.player.PlayerEvent;
import sun.nio.ch.Net;

import java.io.*;
import java.net.Socket;
import java.util.Calendar;

public class NetworkClient extends NetworkPlayer{
    private boolean isMyTurn;

    public NetworkClient(Socket socket) throws IOException, InvalidProtocolException {
        super(socket);
    }

    private boolean serverAccepts() throws IOException, InvalidProtocolException {
        sendNetworkMessage(USER_NAME);
        receiveNetworkMessage();
        sendNetworkMessage(HELLO_MSG);
        String response = receiveNetworkMessage();
        if(!response.equals(HELLO_MSG)) {
            throw new InvalidProtocolException(HELLO_MSG,response);
        }
        sendNetworkMessage(NEW_GAME_MSG);
        response = receiveNetworkMessage();
        if(response.equals(REJECT_MSG)) {
            return false;
        }
        else if(response.equals(READY_MSG)) {
            return true;
        }
        throw new InvalidProtocolException(READY_MSG + " or " + REJECT_MSG,response);
    }

    private void start() throws IOException, InvalidProtocolException {
        if(serverAccepts()) {
            if(isMyTurn) {
                sendNetworkMessage(PASS_MSG);
            }
        }
        else {
            closeConnection();
        }
    }




    @Override
    public void setGame(Game game, int playerID, Board.CounterType type) {
        this.game = game;
        this.playerId = playerID;
        this.counterType = type;
        try {
            start();
        } catch (IOException e) {
            Logger.log(e,"Connection broke for some reason");
        } catch (InvalidProtocolException e) {
            Logger.log(e,"Other side used bad network" + e.getMessage());
        }
    }

    @Override
    public void endTurn() {
        isMyTurn = false;
    }


    @Override
    public void startTurn(Turn opponentTurn) {

        try {
            isMyTurn = true;
            if(opponentTurn == null) {
                sendNetworkMessage(PASS_MSG);
            } else {
                sendNetworkMessage(opponentTurn.toNetworkString());
            }
            String message = receiveNetworkMessage();

            Logger.log(this,"Got message: " + message);

            if(message != null) {
                processNetworkTurn(message);
            }
            else {
                Logger.error(this,"Got null message, assuming broken connection.");
            }
        } catch (IOException e) {
            Logger.error(e,"Connection broke.");
        }
    }
}
