package org.example.service.auth;

import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Servicio encargado de gestionar la autenticación de usuarios según el tipo especificado.
 * <p>
 * Utiliza el patrón Strategy para delegar la lógica de autenticación a distintas estrategias
 * según el tipo de usuario (por ejemplo, Club o Judoka).
 * </p>
 *
 * @author Ignacio Essus
 */
@Service
public class AuthenticationService {

    private final List<AuthenticationStrategy> strategies;

    /**
     * Constructor que recibe la lista de estrategias de autenticación disponibles.
     *
     * @param strategies lista de implementaciones de {@link AuthenticationStrategy}
     */
    public AuthenticationService(List<AuthenticationStrategy> strategies) {
        this.strategies = strategies;
    }

    /**
     * Intenta autenticar a un usuario con las credenciales proporcionadas y según su tipo.
     *
     * @param tipo     el tipo de usuario (por ejemplo, "JUDOKA", "CLUB")
     * @param username el nombre de usuario (correo)
     * @param password la contraseña
     * @return true si las credenciales son válidas; false en caso contrario
     */
    public boolean authenticate(String tipo, String username, String password) {
        return strategies.stream()
                .filter(s -> s.getTipo().equalsIgnoreCase(tipo))
                .findFirst()
                .map(s -> s.authenticate(username, password))
                .orElse(false);
    }

    /**
     * Verifica si el tipo de usuario proporcionado está soportado por el sistema.
     *
     * @param tipo el tipo de usuario a verificar
     * @return true si el tipo es válido; false si no existe estrategia para ese tipo
     */
    public boolean tipoValido(String tipo) {
        return strategies.stream().anyMatch(s -> s.getTipo().equalsIgnoreCase(tipo));
    }
}
