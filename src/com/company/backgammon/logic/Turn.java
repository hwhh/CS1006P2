package com.company.backgammon.logic;

import com.company.backgammon.Logger;
import com.company.backgammon.logic.moves.*;

import java.util.ArrayList;
import java.util.List;

public class Turn {
    List<Move> moves;
    List<Integer> dices;

    public Turn() {
        this.moves = new ArrayList<>();
        this.dices = new ArrayList<>();
    }

    public Turn(Turn turn) {
        this.moves = new ArrayList<>(turn.getMoves());
        this.dices = new ArrayList<>(turn.getDices());
    }

    public static Turn parseMessage(String message, Board.CounterType playerType) {
        Turn result = new Turn();
        String[] intermediaryResult = message.split(":");
        String dices = intermediaryResult[0];
        String moves = intermediaryResult[1];

        intermediaryResult = dices.split("-");
        result.dices.add(Integer.parseInt(intermediaryResult[0]));
        result.dices.add(Integer.parseInt(intermediaryResult[1]));

        moves = moves.replaceAll("\\s*", "");
        intermediaryResult = moves.split("[,;]");

        for(int i = 0; i < intermediaryResult.length; ++i) {
            String move = intermediaryResult[i].substring(1,intermediaryResult[i].length()-1);
            String[] moveComps = move.split("\\|");
            int from = Integer.parseInt(moveComps[0]);
            int to = Integer.parseInt(moveComps[1]);

            if(playerType == Board.CounterType.RED) {
                if(from == 0) {
                    result.addMove(new ReenterMove(to-1,-1,playerType)); // TODO compute proper distance
                }
                else if(to == 25) {
                    result.addMove(new BearOffMove(from-1,-1,playerType));
                }
                else if(to != -1 && from != -1) {
                    result.addMove(new BoardMove(from-1,to-1, -1, playerType));
                }
            }
            else if(playerType == Board.CounterType.WHITE) {
                if(from == 25) {
                    result.addMove(new ReenterMove(to-1,-1,playerType));
                }
                else if(to == 0) {
                    result.addMove(new BearOffMove(from-1,-1,playerType));
                }
                else if(to != -1 && from != -1) {
                    result.addMove(new BoardMove(from-1,to-1, -1, playerType));
                }
                else {
                    result.addMove(new NullMove(playerType));
                }
            }
        }
        Logger.log(Turn.class,message + " parsed into " + result.toNetworkString());
        return result;
    }

    public List<Move> getMoves() {
        return moves;
    }

    public List<Integer> getDices() {
        return dices;
    }

    public void setDices(List<Integer> dices) {
        this.dices = dices;
    }

    public void addMove(Move move) {
        this.moves.add(move);
    }

    public String toNetworkString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getDices().get(0));
        builder.append("-").append(getDices().get(1));
        builder.append(":");
        for(Move move:getMoves()) {
            builder.append(move.toNetworkMessage()).append(",");
        }
        builder.delete(builder.length()-1,builder.length());
        builder.append(";");
        return builder.toString();
    }
}
