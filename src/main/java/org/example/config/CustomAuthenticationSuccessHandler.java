package org.example.config;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Manejador personalizado que se ejecuta cuando un usuario se autentica exitosamente.
 * <p>
 * Este componente determina el tipo de usuario autenticado (club o judoka),
 * guarda esa información en la sesión y redirige al usuario al índice principal.
 * </p>
 *
 * @author Alonso Romero
 */
@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    /**
     * Método invocado automáticamente por Spring Security después de una autenticación exitosa.
     * Guarda el nombre de usuario y tipo de rol en la sesión y redirige al índice principal.
     *
     * @param request        la solicitud HTTP entrante
     * @param response       la respuesta HTTP saliente
     * @param authentication contiene los detalles de autenticación del usuario
     * @throws IOException      si ocurre un error de entrada/salida
     * @throws ServletException si ocurre un error relacionado con el servlet
     */
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = authentication.getName();

        // Se identifica si el usuario es un club o judoka según su rol
        String tipo = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Extrae el nombre del rol
                .filter(authority -> authority.equals("ROLE_CLUB") || authority.equals("ROLE_JUDOKA"))
                .findFirst()
                .map(authority -> authority.equals("ROLE_CLUB") ? "club" : "judoka")
                .orElse(""); // Por defecto, tipo vacío si no se encuentra un rol válido

        // Se almacenan los datos en la sesión
        session.setAttribute("username", username);
        session.setAttribute("tipo", tipo);

        // Redirección a la página principal
        response.sendRedirect("/index");
    }
}
