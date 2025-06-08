package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.example.service.auth.AuthenticationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
@RequiredArgsConstructor
@Controller
public class AuthController {

    private final AuthenticationService authenticationService;
    private static final String DIRIGIR_JUDOKA_HOME = "redirect:/judoka/home";
    private static final String DIRIGIR_LOGIN = "Model/login";
    private static final String JUDOKA = "judoka";
    private static final String ERROR = "error";

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {return "Model/index";}

    @GetMapping("/login")
    public String showLogin(HttpSession session) {
        if (isUsuarioLogueado(session)) {
            return destinoSegunTipoUsuario(session);
        }
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


    @PostMapping("/login")
    public String doLogin(
            @RequestParam("username") String username,
            @RequestParam("password") String password,
            @RequestParam("tipo") String tipo,
            Model model,
            HttpSession session
    ) {
        String error = validarLogin(username, password, tipo);
        if (error != null) {
            model.addAttribute(ERROR, error);
            return DIRIGIR_LOGIN;
        }

        session.setAttribute("username", username);
        session.setAttribute("tipo", tipo);

        return JUDOKA.equalsIgnoreCase(tipo) ? DIRIGIR_JUDOKA_HOME : "redirect:/club/home";
    }

    private String validarLogin(String username, String password, String tipo) {
        String mensajeValidacion = validarCamposObligatorios(username, password);
        if (mensajeValidacion != null) return mensajeValidacion;

        mensajeValidacion = validarTipoUsuario(tipo);
        if (mensajeValidacion != null) return mensajeValidacion;

        return validarCredenciales(username, password, tipo);

    }

    private String validarCamposObligatorios(String username, String password) {
        if (username == null || username.isEmpty()) {
            return "Usuario vacío";
        }
        if (password == null || password.isEmpty()) {
            return "Contraseña vacía";
        }
        return null;
    }

    private String validarTipoUsuario(String tipo) {
        if (!authenticationService.tipoValido(tipo)) {
            return "Tipo inválido";
        }
        return null;
    }

    private String validarCredenciales(String username, String password, String tipo) {
        if (!authenticationService.authenticate(tipo, username, password)) {
            return "Usuario o contraseña incorrectos";
        }
        return null;
    }


    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }

    @GetMapping("/registro")
    public String showRegistro() {
        return "Model/registro";
    }
}