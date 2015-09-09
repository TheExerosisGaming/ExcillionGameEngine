package me.exerosis.game.engine.util;

public class Debugger {
    public static void printCaller() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (int x = 2; x < stackTrace.length && x < 7; x++) {
            System.err.println(stackTrace[x].getClassName());
            System.err.println(stackTrace[x].getMethodName());
        }
    }
}
