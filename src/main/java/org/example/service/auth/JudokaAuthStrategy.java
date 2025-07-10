package org.example.service.auth;

import lombok.RequiredArgsConstructor;
import org.example.service.JudokaService;
import org.springframework.stereotype.Component;

/**
 * Estrategia de autenticación para usuarios de tipo Judoka.
 * <p>
 * Implementa la interfaz {@link AuthenticationStrategy} y utiliza {@link JudokaService}
 * para validar las credenciales.
 * </p>
 *
 * Esta clase forma parte del patrón Strategy aplicado a la autenticación de diferentes tipos de usuarios.
 *
 * @author Ignacio Essus
 */
@Component
@RequiredArgsConstructor
public class JudokaAuthStrategy implements AuthenticationStrategy {

    private final JudokaService judokaService;

    /**
     * Autentica un usuario tipo Judoka mediante su nombre de usuario y contraseña.
     *
     * @param username el nombre de usuario (correo)
     * @param password la contraseña
     * @return true si las credenciales son válidas; false en caso contrario
     */
    @Override
    public boolean authenticate(String username, String password) {
        return judokaService.validarContrasena(username, password);
    }

    /**
     * Devuelve el tipo de usuario que maneja esta estrategia: "judoka".
     *
     * @return el tipo "judoka"
     */
    @Override
    public String getTipo() {
        return "judoka";
    }
}
