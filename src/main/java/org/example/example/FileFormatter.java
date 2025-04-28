package org.example.example;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

public class FileFormatter extends Formatter {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder();

        builder.append(dateFormat.format(new Date(record.getMillis())))
                .append(" - [")
                .append(record.getLevel().getName())
                .append("] - ")
                .append(record.getSourceClassName())
                .append(".")
                .append(record.getSourceMethodName())
                .append(" - ")
                .append(formatMessage(record))
                .append(System.lineSeparator());
        return builder.toString();
    }
}
