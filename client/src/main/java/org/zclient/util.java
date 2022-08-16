package org.zclient;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static final String BG = "\033[1;38;5;28m";
    public static final String BR = "\033[1;38;5;9m";
    public static final String BKG = "\033[48;5;61m";

    // Message colors
    public static final String DMU = "\033[1;38;5;26m";
    public static final String DMM = "\033[38;5;39m";
    public static final String BW = "\033[1;38;5;231m";


    // Server response colors
    public static final String SM = "\033[38;5;240m";
    public static final String SG = "\033[1;38;5;22m";
    public static final String SR = "\033[1;38;5;52m";
    /* warning colors
    * presence message updates
    * show updates
    * deleted contacts
    */
    public static final String SW = "\033[1;38;5;11m";

    // for subscription requests
    public static final String SF = "\033[1;38;5;202m";
    // unsubscribed users
    public static final String SU = "\033[1;38;5;172m";
    // subscribed users
    public static final String SS = "\033[1;38;5;166m";


}
