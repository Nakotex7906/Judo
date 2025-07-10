package org.example.model.logger;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Clase utilitaria para obtener instancias de {@link Logger} con configuración personalizada.
 * <p>
 * Configura un logger con salida a consola (formato con colores) y archivo (formato detallado),
 * aplicando los formateadores {@link ColorFormatter} y {@link FileFormatter}.
 * </p>
 * <p>
 * El archivo de logs generado es {@code competencias.log}, y el nivel de log es {@code Level.ALL}.
 * Esta clase sigue el patrón de utilidad (constructor privado + métodos estáticos).
 * </p>
 *
 * Ejemplo de uso:
 * <pre>{@code
 * private static final Logger logger = LoggerManager.getLogger(MiClase.class);
 * }</pre>
 *
 * @author Benjamin Beroiza, Alonso Romero, Ignacio Essus
 */
public final class LoggerManager {

    /**
     * Constructor privado para evitar instanciación.
     */
    private LoggerManager() {
    }

    /**
     * Devuelve una instancia de {@link Logger} asociada a la clase indicada.
     * <p>
     * Si el logger aún no tiene handlers, se le agregan:
     * <ul>
     *   <li>Un {@link FileHandler} con {@link FileFormatter} para persistir en archivo.</li>
     *   <li>Un {@link ConsoleHandler} con {@link ColorFormatter} para imprimir en consola.</li>
     * </ul>
     * </p>
     *
     * @param clazz la clase desde la cual se solicita el logger
     * @return el logger configurado para la clase dada
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);

        if (logger.getHandlers().length == 0) {
            try {
                FileHandler fileHandler = new FileHandler("competencias.log", true);
                fileHandler.setFormatter(new org.example.model.logger.FileFormatter());
                logger.addHandler(fileHandler);

                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(new org.example.model.logger.ColorFormatter());
                logger.addHandler(consoleHandler);

                logger.setLevel(Level.ALL);
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Error al configurar el logger: %s", e.getMessage()), e);
            }
        }
        return logger;
    }
}
