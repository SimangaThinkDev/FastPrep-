package com.fastprep.backend.utils;

/**
 * Utility class for applying ANSI foreground and background colors to strings.
 *
 * This class single-handedly allows us to print colors in our program
 * It makes it easy as we can just say red( text ), then it returns for us
 * the string in red
 * RESET makes sure the color pallete does not stay the same
 *       after printing what we want
 * @author Simangaliso Innocent Phakwe
 */
public final class Ansi {
    private static final String RESET = "\u001B[0m";

    // Foreground Colors

    /**
     * Applies ANSI BLACK foreground color and resets formatting.
     *
     * @param text The text to colorize.
     * @return The text wrapped in ANSI BLACK color.
     */
    public static String black(String text) {
        return "\u001B[30m" + text + RESET;
    }

    /**
     * Applies ANSI RED foreground color and resets formatting.
     *
     * @param text The text to colorize.
     * @return The text wrapped in ANSI RED color.
     */
    public static String red(String text) {
        return "\u001B[31m" + text + RESET;
    }

    /**
     * Applies ANSI GREEN foreground color and resets formatting.
     *
     * @param text The text to colorize.
     * @return The text wrapped in ANSI GREEN color.
     */
    public static String green(String text) {
        return "\u001B[32m" + text + RESET;
    }

    /**
     * Applies ANSI YELLOW foreground color and resets formatting.
     *
     * @param text The text to colorize.
     * @return The text wrapped in ANSI YELLOW color.
     */
    public static String yellow(String text) {
        return "\u001B[33m" + text + RESET;
    }

    /**
     * Applies ANSI BLUE foreground color and resets formatting.
     *
     * @param text The text to colorize.
     * @return The text wrapped in ANSI BLUE color.
     */
    public static String blue(String text) {
        return "\u001B[34m" + text + RESET;
    }

    /**
     * Applies ANSI PURPLE foreground color and resets formatting.
     *
     * @param text The text to colorize.
     * @return The text wrapped in ANSI PURPLE color.
     */
    public static String purple(String text) {
        return "\u001B[35m" + text + RESET;
    }

    /**
     * Applies ANSI CYAN foreground color and resets formatting.
     *
     * @param text The text to colorize.
     * @return The text wrapped in ANSI CYAN color.
     */
    public static String cyan(String text) {
        return "\u001B[36m" + text + RESET;
    }

    /**
     * Applies ANSI WHITE foreground color and resets formatting.
     *
     * @param text The text to colorize.
     * @return The text wrapped in ANSI WHITE color.
     */
    public static String white(String text) {
        return "\u001B[37m" + text + RESET;
    }

    // Background Colors

    /**
     * Applies ANSI BLACK background color and resets formatting.
     *
     * @param text The text to highlight.
     * @return The text wrapped in ANSI BLACK background.
     */
    public static String onBlack(String text) {
        return "\u001B[40m" + text + RESET;
    }

    /**
     * Applies ANSI RED background color and resets formatting.
     *
     * @param text The text to highlight.
     * @return The text wrapped in ANSI RED background.
     */
    public static String onRed(String text) {
        return "\u001B[41m" + text + RESET;
    }

    /**
     * Applies ANSI GREEN background color and resets formatting.
     *
     * @param text The text to highlight.
     * @return The text wrapped in ANSI GREEN background.
     */
    public static String onGreen(String text) {
        return "\u001B[42m" + text + RESET;
    }

    /**
     * Applies ANSI YELLOW background color and resets formatting.
     *
     * @param text The text to highlight.
     * @return The text wrapped in ANSI YELLOW background.
     */
    public static String onYellow(String text) {
        return "\u001B[43m" + text + RESET;
    }

    /**
     * Applies ANSI BLUE background color and resets formatting.
     *
     * @param text The text to highlight.
     * @return The text wrapped in ANSI BLUE background.
     */
    public static String onBlue(String text) {
        return "\u001B[44m" + text + RESET;
    }

    /**
     * Applies ANSI PURPLE background color and resets formatting.
     *
     * @param text The text to highlight.
     * @return The text wrapped in ANSI PURPLE background.
     */
    public static String onPurple(String text) {
        return "\u001B[45m" + text + RESET;
    }

    /**
     * Applies ANSI CYAN background color and resets formatting.
     *
     * @param text The text to highlight.
     * @return The text wrapped in ANSI CYAN background.
     */
    public static String onCyan(String text) {
        return "\u001B[46m" + text + RESET;
    }

    /**
     * Applies ANSI WHITE background color and resets formatting.
     *
     * @param text The text to highlight.
     * @return The text wrapped in ANSI WHITE background.
     */
    public static String onWhite(String text) {
        return "\u001B[47m" + text + RESET;
    }

    // Prevent instantiation
    private Ansi() {}
}