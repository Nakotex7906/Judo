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
        String tipo = "";

        // Revisamos la autoridad (rol) del usuario para determinar su tipo
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_CLUB")) {
                tipo = "club";
                break;
            } else if (authority.getAuthority().equals("ROLE_JUDOKA")) {
                tipo = "judoka";
                break;
            }
        }

        // Guardamos los datos correctos en la sesión
        session.setAttribute("username", username);
        session.setAttribute("tipo", tipo);

        response.sendRedirect("/index");
    }
}