package org.example.model.example;

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
        StringBuilder builder = new StringBuilder();

        builder.append(dateFormat.format(new Date(logRecord.getMillis())))
                .append(" - [")
                .append(logRecord.getLevel().getName())
                .append("] - ")
                .append(logRecord.getSourceClassName())
                .append(".")
                .append(logRecord.getSourceMethodName())
                .append(" - ")
                .append(formatMessage(logRecord))
                .append(System.lineSeparator());
        return builder.toString();
    }
}
