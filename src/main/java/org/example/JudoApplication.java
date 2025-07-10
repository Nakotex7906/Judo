package org.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Clase principal que inicia la aplicación Spring Boot para el sistema de gestión de judokas y clubes.
 */
@SpringBootApplication
public class JudoApplication {

    /**
     * Método principal que lanza la aplicación.
     *
     * @param args argumentos de la línea de comandos
     */
    public static void main(String[] args) {
        SpringApplication.run(JudoApplication.class, args);
    }
}
