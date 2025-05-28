package org.example.controllerWeb;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.model.user.Judoka;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.model.logger.LoggerManager;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
@Controller
public class JudokaWebController {
    private static final Logger logger = LoggerManager.getLogger(JudokaWebController.class);
    private final JudokaService judokaService;

    @GetMapping("/judokas")
    public String listarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        if (judokas.isEmpty()) {
            logger.log(Level.INFO, "No hay judokas registradas");
        }
        model.addAttribute("judokas", judokas);
        return "Judoka/judokas";
    }

    @PostMapping("/judokas")
    public String mostrarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        model.addAttribute("judokas", judokas);
        return "Judoka/judokas";
    }

    private boolean esJudoka(HttpSession s) {
        return s.getAttribute("username") != null && "judoka".equals(s.getAttribute("tipo"));
    }

    @GetMapping("/judoka/home")
    public String judokaHome(HttpSession session, Model model) {
        if (!esJudoka(session)) return "redirect:/login";
        String username = (String) session.getAttribute("username");

        Judoka judoka = judokaService.findByUsername(username).orElse(null);
        if (judoka != null) {
            model.addAttribute("nombre", judoka.getNombre());
        } else {
            model.addAttribute("nombre", username);
        }
        return "Judoka/judoka_home";
    }

    @GetMapping("/registro-judoka")
    public String showRegistroJudoka() {
        return "Judoka/registro_judoka";
    }

    @PostMapping("/registro-judoka")
    public String doRegistroJudoka(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nombre,
            @RequestParam String apellido,
            @RequestParam String categoria,
            @RequestParam String fechaNacimiento,
            Model model
    ) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                nombre == null || nombre.isBlank() ||
                apellido == null || apellido.isBlank() ||
                categoria == null || categoria.isBlank() ||
                fechaNacimiento == null || fechaNacimiento.isBlank()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "Judoka/registro_judoka";
        }

        if (judokaService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado.");
            return "Judoka/registro_judoka";
        }

        Judoka nuevo = new Judoka();
        nuevo.setUsername(username);
        nuevo.setPassword(password);
        nuevo.setNombre(nombre);
        nuevo.setApellido(apellido);
        nuevo.setCategoria(categoria);
        nuevo.setFechaNacimiento(fechaNacimiento);

        judokaService.guardarJudoka(nuevo);
        model.addAttribute("success", "¡Judoka registrado correctamente! Ahora puedes iniciar sesión.");
        return "Judoka/registro_judoka";
    }
}
