package org.example.model.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * The type File formatter.
 */
public class FileFormatter extends Formatter {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord logRecord) {
    return dateFormat.format(new Date(logRecord.getMillis())) +
            " - [" +
            logRecord.getLevel().getName() +
            "] - " +
            logRecord.getSourceClassName() +
            "." +
            logRecord.getSourceMethodName() +
            " - " +
            formatMessage(logRecord) +
            System.lineSeparator();
    }
}