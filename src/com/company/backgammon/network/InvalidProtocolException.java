package com.company.backgammon.network;

public class InvalidProtocolException extends Exception {
    private final String expected;
    private final String received;

    public InvalidProtocolException(String expected, String received) {
        this.expected = expected;
        this.received = received;
    }

    @Override
    public String getMessage() {
        return "Expected " + expected.length() + " " + expected + " but got " + received.length() + " "+ received;
    }
}
