package org.example.model.logger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * {@code FileFormatter} es un formateador personalizado de logs para archivos.
 * <p>
 * Este formateador produce mensajes detallados que incluyen:
 * fecha y hora, nivel del log, clase y método de origen, y el mensaje.
 * </p>
 *
 * @author Alonso Romero, Benjamin Beroiza, Ignacio Essus
 */
public class FileFormatter extends Formatter {

    /** Formato de fecha utilizado para cada entrada del log */
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    /**
     * Aplica el formato detallado para escribir en archivos de log.
     *
     * @param logRecord el registro de log que se va a formatear
     * @return una línea formateada lista para escribirse en archivo
     */
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
