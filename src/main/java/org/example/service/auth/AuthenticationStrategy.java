package org.example.service.auth;

/**
 * Estrategia de autenticación para distintos tipos de usuarios (por ejemplo, Club, Judoka).
 * <p>
 * Esta interfaz define el contrato que deben seguir todas las implementaciones de estrategias
 * de autenticación específicas. Utiliza el patrón Strategy para desacoplar la lógica de autenticación
 * según el tipo de usuario.
 * </p>
 *
 * @author Ignacio Essus
 */
public interface AuthenticationStrategy {

    /**
     * Realiza la autenticación con el nombre de usuario y la contraseña proporcionados.
     *
     * @param username el nombre de usuario (correo)
     * @param password la contraseña
     * @return true si las credenciales son válidas; false en caso contrario
     */
    boolean authenticate(String username, String password);

    /**
     * Devuelve el tipo de usuario que maneja esta estrategia (por ejemplo, "CLUB" o "JUDOKA").
     *
     * @return el tipo de usuario como String
     */
    String getTipo();
}
