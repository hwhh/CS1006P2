package com.company.backgammon.logic;

import java.util.Random;

public class Dice {

    public int value;
    public static Random randomNumber = new Random();

    public Dice() {
        roll();
    }

    public void setValue(int spots) {
        value = spots;
    }

    public void roll() {
        value=randomNumber.nextInt(6) + 1;
    }

    public int getValue() {
        return value;
    }

}
