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
        if (session.getAttribute("username") != null) {
            String tipo = (String) session.getAttribute("tipo");
            if (JUDOKA.equals(tipo)) return DIRIGIR_JUDOKA_HOME;
            if ("club".equals(tipo)) return "redirect:/club/home";
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
        if (username == null || username.isEmpty()) return "Usuario vacío";
        if (password == null || password.isEmpty()) return "Contraseña vacía";
        if (!authenticationService.tipoValido(tipo)) return "Tipo inválido";
        if (!authenticationService.authenticate(tipo, username, password)) return "Usuario o contraseña incorrectos";
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