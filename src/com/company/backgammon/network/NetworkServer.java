package com.company.backgammon.network;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.Board;
import com.company.backgammon.logic.Game;
import com.company.backgammon.logic.Turn;

import java.io.*;
import java.net.Socket;

public class NetworkServer extends NetworkPlayer {



    public NetworkServer(Socket socket) throws IOException {
        super(socket);
    }


//    private static void rejectClient(Socket socket) throws InvalidProtocolException, IOException {
//        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//        PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
//        String response = input.readLine();
//        if(!response.equals("hello")) {
//            throw new InvalidProtocolException();
//        }
//        output.println("reject");
//        socket.close();
//    }

    public void acceptClient() throws IOException, InvalidProtocolException {

        String response = receiveNetworkMessage();
        sendNetworkMessage(USER_NAME);
        response = receiveNetworkMessage();
        if(!response.equals(HELLO_MSG)) {
            throw new InvalidProtocolException(HELLO_MSG,response);
        }
        sendNetworkMessage(HELLO_MSG);
        response = receiveNetworkMessage();
        if(!response.equals(NEW_GAME_MSG)) {
            throw new InvalidProtocolException(NEW_GAME_MSG,response);
        }
        sendNetworkMessage(READY_MSG);
    }

    private String savedMessageFromStart;

    public void start() throws IOException, InvalidProtocolException {
        acceptClient();
        String message = receiveNetworkMessage();
        if(message.equals(PASS_MSG)) {
            game.start(1-playerId);
        }
        else {
            savedMessageFromStart = message;
            game.start(playerId);
        }
    }


    @Override
    public void setGame(Game game, int playerID, Board.CounterType type) {
        this.game = game;
        this.playerId = playerID;
        this.counterType = type;
    }

    @Override
    public void startTurn(Turn opponentTurn) {

        try {
            String message;
            if(opponentTurn != null) {
                sendNetworkMessage(opponentTurn.toNetworkString());
                message = receiveNetworkMessage();
            }
            else {
                message = savedMessageFromStart;
            }
            Logger.log(this,"Got message: " + message);
            if(message != null) {
                processNetworkTurn(message);
            }
            else {
                Logger.error(this,"Got null message, assuming broken connection.");
                game.disconnect(playerId);
            }
        } catch (IOException e) {
            Logger.error(e,"Connection broke.");
        }
    }

}
