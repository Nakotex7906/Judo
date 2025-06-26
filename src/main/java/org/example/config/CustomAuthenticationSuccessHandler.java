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

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        HttpSession session = request.getSession();
        String username = authentication.getName();

        // Refactorizado: Usamos la API de Streams para encontrar el rol de forma más limpia y eficiente.
        String tipo = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority) // Convierte cada authority a su nombre en String (ej: "ROLE_CLUB")
                .filter(authority -> authority.equals("ROLE_CLUB") || authority.equals("ROLE_JUDOKA")) // Filtra solo los roles que nos interesan
                .findFirst() // Busca la primera coincidencia (esto reemplaza la necesidad del break)
                .map(authority -> authority.equals("ROLE_CLUB") ? "club" : "judoka") // Convierte el rol encontrado al tipo correspondiente
                .orElse(""); // Si no se encuentra ningún rol, el valor por defecto es una cadena vacía

        // Guardamos los datos correctos en la sesión
        session.setAttribute("username", username);
        session.setAttribute("tipo", tipo);

        response.sendRedirect("/index");
    }
}