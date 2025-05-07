package org.example.model.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The type Color formatter.
 */
public class ColorFormatter extends Formatter {

    /**
     * The constant RESET.
     */
    public static final String RESET = "\u001B[0m";
    /**
     * The constant RED.
     */
    public static final String RED = "\u001B[31m";
    /**
     * The constant YELLOW.
     */
    public static final String YELLOW = "\u001B[33m";
    /**
     * The constant GREEN.
     */
    public static final String GREEN = "\u001B[32m";
    /**
     * The constant CYAN.
     */
    public static final String CYAN = "\u001B[36m";

    @Override
    public String format(LogRecord logRecord) {
        String color = switch (logRecord.getLevel().getName()) {
            case "SEVERE" -> RED;
            case "WARNING" -> YELLOW;
            case "INFO" -> CYAN;
            default -> GREEN;
        };

        return color + "[" + logRecord.getLevel() + "] " + formatMessage(logRecord) + RESET + "\n";
    }
}
