package org.zclient;

public class util {

    // ANSI Terminal codes
    static final String ESC = "\033";

    static String clearScreen() {
        return "\033[H\033[2J";
    }

    static String cursorHome() {
        return "\033[H";
    }

    static String cursorTo(int row, int column) {
        return String.format("\033[%d;%dH", row, column);
    }

    static String cursorSave() {
        return "\033[s";
    }

    static String cursorRestore() {
        return "\033[u";
    }

    static String scrollScreen() {
        return "\033[r";
    }

    static String scrollSet(int top, int bottom) {
        return String.format("\033[%d;%dr", top, bottom);
    }

    static String scrollUp() {
        return "\033D";
    }

    static String scrollDown() {
        return "\033D";
    }

    public static final String BG = "\033[1;32m";
    public static final String BKG = "\033[48;5;61m";


    // Server response colors
    public static final String SG = "\033[1;38;5;22m";
    public static final String SR = "\033[1;38;5;88m";


}
