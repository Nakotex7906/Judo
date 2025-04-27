package org.example.Example;

import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LoggerManager {

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
                logger.log(Level.SEVERE, "Error al configurar el logger", e);
            }
        }
        return logger;
    }
}
