package org.example.controllerweb;

import org.example.model.user.Club;
import org.example.model.user.Judoka;
import org.example.service.ClubService;
import org.example.service.JudokaService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RequiredArgsConstructor
@Controller
public class AuthController {

    // MODIFICADO: Inyectamos los servicios para buscar los datos del usuario.
    private final JudokaService judokaService;
    private final ClubService clubService;

    // MODIFICADO: Se eliminó la dependencia a AuthenticationService que ya no es necesaria aquí.
    private static final String DIRIGIR_JUDOKA_HOME = "redirect:/judoka/home";
    private static final String DIRIGIR_LOGIN = "Model/login";
    private static final String JUDOKA = "judoka";
    private static final String ERROR = "error";

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "Model/index";
    }

    @GetMapping("/login")
    public String showLogin() {
        // La lógica de redirección si ya está logueado se puede simplificar o manejar en un filtro.
        // Por ahora la dejamos así, pero el CustomAuthenticationSuccessHandler previene el re-login.
        return DIRIGIR_LOGIN;
    }

    private boolean isUsuarioLogueado(HttpSession session) {
        return session.getAttribute("username") != null;
    }

    private String destinoSegunTipoUsuario(HttpSession session) {
        String tipo = (String) session.getAttribute("tipo");
        if (JUDOKA.equals(tipo)) {
            return DIRIGIR_JUDOKA_HOME;
        } else if ("club".equals(tipo)) {
            return "redirect:/club/home";
        }
        return DIRIGIR_LOGIN;
    }

    // MODIFICADO: Se eliminó por completo el método doLogin (@PostMapping). Spring Security ahora lo maneja.



    private String validarCamposObligatorios(String username, String password) {
        if (username == null || username.isEmpty()) {
            return "Usuario vacío";
        }
        if (password == null || password.isEmpty()) {
            return "Contraseña vacía";
        }
        return null;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    @GetMapping("/registro")
    public String showRegistro() {
        return "Model/registro";
    }

    // Nuevo método para redirigir a la vista de perfil correspondiente

    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        String tipo = (String) session.getAttribute("tipo");

        if (username == null) {
            return "redirect:/login";
        }

        if ("judoka".equalsIgnoreCase(tipo)) {
            Optional<Judoka> judokaOpt = judokaService.findByUsername(username);
            if (judokaOpt.isPresent()) {
                model.addAttribute("judoka", judokaOpt.get());
                return "Judoka/Perfil_Judoka";
            }
        } else if ("club".equalsIgnoreCase(tipo)) {
            Optional<Club> clubOpt = clubService.findByUsernameWithJudokas(username);
            if (clubOpt.isPresent()) {
                model.addAttribute("club", clubOpt.get());
                return "Club/Perfil_Club";
            }
        }

        session.invalidate();
        return "redirect:/login?error=user_not_found";
    }

}
