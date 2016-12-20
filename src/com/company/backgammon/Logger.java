package com.company.backgammon;

import java.util.ArrayList;
import java.util.List;

public class Logger {

    private static List<Class> mutedClasses = new ArrayList<>();

    public static void muteClass(Class classToMute) {
        mutedClasses.add(classToMute);
    }

    public static void error(Object cause, String message) {
        error(cause.getClass(),message);
    }

    public static void error(Class clas, String message) {
        if(!mutedClasses.contains(clas)) {
            System.out.println("[ERROR][" + clas.getSimpleName() + "]: " + message);
        }
    }

    public static void log(Object cause, String message) {
        log(cause.getClass(),message);
    }

    public static void log(Class clas, String message) {
        if(!mutedClasses.contains(clas)) {
            System.out.println("[LOG][" + clas.getSimpleName() + "]: " + message);
        }
    }
}
