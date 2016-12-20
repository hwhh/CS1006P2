package com.company.backgammon.ui;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RollDiceActionListener implements ActionListener {


    private BackGammonGUI backGammonGUI;

    public RollDiceActionListener(BackGammonGUI backGammonGUI) {
        this.backGammonGUI = backGammonGUI;
    }


    private void roll() {
        backGammonGUI.triggerRoll();
    }

    public void actionPerformed(ActionEvent e) {
        if(backGammonGUI.canRoll()) {
            roll();
        }
    }


}
