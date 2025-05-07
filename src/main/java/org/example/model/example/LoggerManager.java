package org.example.model.example;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Logger manager.
 */
public final class LoggerManager {  //

    private LoggerManager() {
    }

    /**
     * Gets logger.
     *
     * @param clazz the clazz
     * @return the logger
     */
    public static Logger getLogger(Class<?> clazz) {
        Logger logger = Logger.getLogger(clazz.getName());
        logger.setUseParentHandlers(false);

        if (logger.getHandlers().length == 0) {
            try {
                FileHandler fileHandler = new FileHandler("competencias.log", true);
                fileHandler.setFormatter(new FileFormatter());
                logger.addHandler(fileHandler);

                ConsoleHandler consoleHandler = new ConsoleHandler();
                consoleHandler.setFormatter(new ColorFormatter());
                logger.addHandler(consoleHandler);

                logger.setLevel(Level.ALL);
            } catch (Exception e) {
                logger.log(Level.SEVERE, String.format("Error al configurar el logger: %s", e.getMessage()), e);
            }
        }
        return logger;
    }
}
