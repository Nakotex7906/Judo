package org.example.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.service.ClubService;
import org.springframework.stereotype.Component;

/**
 * Estrategia de autenticación para usuarios de tipo Club.
 * <p>
 * Implementa la interfaz {@link AuthenticationStrategy} y delega la validación
 * al servicio {@link ClubService}.
 * </p>
 *
 * Esta clase es parte del patrón Strategy que permite manejar múltiples tipos de autenticación.
 *
 * @author Ignacio Essus
 */
@Component
@RequiredArgsConstructor
public class ClubAuthStrategy implements AuthenticationStrategy {

    private final ClubService clubService;

    /**
     * Autentica un usuario tipo Club mediante su nombre de usuario y contraseña.
     *
     * @param username el nombre de usuario (correo)
     * @param password la contraseña
     * @return true si las credenciales son válidas; false en caso contrario
     */
    @Override
    public boolean authenticate(String username, String password) {
        return clubService.validarContrasena(username, password);
    }

    /**
     * Devuelve el tipo de usuario que maneja esta estrategia: "club".
     *
     * @return el tipo "club"
     */
    @Override
    public String getTipo() {
        return "club";
    }

}
