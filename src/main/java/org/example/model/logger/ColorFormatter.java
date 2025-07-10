package org.example.model.logger;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * {@code ColorFormatter} es un formateador de logs personalizado que agrega colores ANSI
 * a los mensajes de log para mejorar la legibilidad en la consola.
 *
 * <p>Asigna diferentes colores según el nivel del mensaje:</p>
 * <ul>
 *     <li>{@code SEVERE} → rojo</li>
 *     <li>{@code WARNING} → amarillo</li>
 *     <li>{@code INFO} → cian</li>
 *     <li>otros niveles → verde</li>
 * </ul>
 *
 * <p>Requiere que la consola o terminal soporte códigos ANSI.</p>
 *
 * @author Benjamin Beroiza, Ignacio Essus
 */
public class ColorFormatter extends Formatter {

    /** Código ANSI para reiniciar el color al valor por defecto */
    public static final String RESET = "\u001B[0m";

    /** Código ANSI para color rojo (usado en logs SEVERE) */
    public static final String RED = "\u001B[31m";

    /** Código ANSI para color amarillo (usado en logs WARNING) */
    public static final String YELLOW = "\u001B[33m";

    /** Código ANSI para color verde (usado como valor por defecto) */
    public static final String GREEN = "\u001B[32m";

    /** Código ANSI para color cian (usado en logs INFO) */
    public static final String CYAN = "\u001B[36m";

    /**
     * Aplica el formato de color al mensaje de log según su nivel.
     *
     * @param logRecord el registro de log recibido
     * @return una cadena formateada con color y mensaje
     */
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
