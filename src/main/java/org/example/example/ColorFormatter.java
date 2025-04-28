package org.example.example;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ColorFormatter extends Formatter {

    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
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
