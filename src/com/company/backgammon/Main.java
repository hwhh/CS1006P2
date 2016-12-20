package com.company.backgammon;

import com.company.backgammon.ui.menus.MainMenu;

import java.awt.event.MouseEvent;
import java.util.Calendar;

public class Main {
    public static long getCurrentUnixTime() {
        return Calendar.getInstance().getTimeInMillis();
    }
    public static void main(String[] args) {
        Logger.muteClass(MouseEvent.class);
        MainMenu.initialize();
    }
}
