package org.example.controllerweb;

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

/**
 * The type Judoka web controller.
 */
@AllArgsConstructor
@Controller
public class JudokaWebController {

    private static final String JUDOKA = "judokas";
    private static final String REGISTRO_JUDOKA = "registro_judoka";

    private static final Logger logger = LoggerManager.getLogger(JudokaWebController.class);
    private final JudokaService judokaService;

    /**
     * Listar judokas string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/judokas")
    public String listarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        //Mensaje por si no hay judokas
        if(judokas.isEmpty()){
            logger.log(Level.INFO,"No hay judokas registradas");
        }
        model.addAttribute(JUDOKA, judokas);
        return JUDOKA;
    }

    @PostMapping("/judokas")
    public String mostrarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        model.addAttribute(JUDOKA, judokas);
        return JUDOKA;
    }

    private boolean esJudoka(HttpSession s) {
        return s.getAttribute("username") != null && "judoka".equals(s.getAttribute("tipo"));
    }

    /**
     * Judoka home string.
     *
     * @param session the session
     * @param model   the model
     * @return the string
     */
    @GetMapping("/judoka/home")
    public String judokaHome(HttpSession session, Model model) {
        if (!esJudoka(session)) return "redirect:/login";
        String username = (String) session.getAttribute("username");

        Judoka judoka = judokaService.findByUsername(username).orElse(null);
        if (judoka != null) {
            model.addAttribute("nombre", judoka.getNombre());
        }else {
            model.addAttribute("nombre", username);
        }
        return "judoka_home";
    }

    // Formulario para registrar judoka
    @GetMapping("/registro-judoka")
    public String showRegistroJudoka() {
        return REGISTRO_JUDOKA;
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
            return REGISTRO_JUDOKA;
        }

        if (judokaService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado.");
            return REGISTRO_JUDOKA;
        }

        Judoka nuevo = new Judoka();
        nuevo.setUsername(username);
        nuevo.setPassword(password);
        nuevo.setNombre(nombre);
        nuevo.setApellido(apellido);
        nuevo.setCategoria(categoria);
        nuevo.setFechaNacimiento(fechaNacimiento);

        judokaService.guardarJudoka(nuevo);
        // Redirige al login indicando exito, evitando así siempre devolver el mismo valor, que era el error de sonar
        return "redirect:/login?registrado=1";
    }

}