package org.example.Example;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class ColorFormatter extends Formatter {


    public static final String RESET = "\u001B[0m";
    public static final String RED = "\u001B[31m";
    public static final String YELLOW = "\u001B[33m";
    public static final String GREEN = "\u001B[32m";
    public static final String CYAN = "\u001B[36m";

    @Override
    public String format(LogRecord record) {
        String color;


        switch (record.getLevel().getName()) {
            case "SEVERE":
                color = RED;
                break;
            case "WARNING":
                color = YELLOW;
                break;
            case "INFO":
                color = CYAN;
                break;
            default:
                color = GREEN;
                break;
        }

        return color + "[" + record.getLevel() + "] " + formatMessage(record) + RESET + "\n";
    }
}
