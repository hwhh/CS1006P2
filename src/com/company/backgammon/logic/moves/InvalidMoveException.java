package com.company.backgammon.logic.moves;

public class InvalidMoveException extends Exception {
    private final Move cause;

    public InvalidMoveException(Move bearOffMove) {
        this.cause = bearOffMove;
    }

    @Override
    public String getMessage() {
        return "Invalid " + cause.getClass().getSimpleName() + ": " + cause.toString();
    }
}
