package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.dto.JudokaRegistroDTO;
import org.example.model.user.Judoka;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.example.model.logger.LoggerManager;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@AllArgsConstructor
@Controller
public class JudokaWebController {
    private static final Logger logger = LoggerManager.getLogger(JudokaWebController.class);
    private static final String JUDOKA_VIEW = "judoka/judokas";
    private static final String REGISTRO_JUDOKA = "Judoka/registro_judoka";
    private final JudokaService judokaService;

    @GetMapping("/judokas")
    public String listarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        if (judokas.isEmpty()) {
            logger.log(Level.INFO, "No hay judokas registradas");
        }
        model.addAttribute("judokas", judokas);
        return JUDOKA_VIEW;
    }

    @PostMapping("/judokas")
    public String mostrarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        model.addAttribute("judokas", judokas);
        return JUDOKA_VIEW;
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
        return REGISTRO_JUDOKA;
    }

    @PostMapping("/registro-judoka")
    public String doRegistroJudoka(@ModelAttribute JudokaRegistroDTO dto, Model model) {
        String errores = validarDatosRegistro(dto);
        if (errores != null) {
            model.addAttribute("error", errores);
            return REGISTRO_JUDOKA;
        }
        if (!validarCredencialesDisponibles(dto.getUsername())) {
            model.addAttribute("error", "El correo ya está registrado.");
            return REGISTRO_JUDOKA;
        }
        Judoka nuevo = mapearDtoAJudoka(dto);
        judokaService.guardarJudoka(nuevo);
        // Redirigir al login tras el registro exitoso
        model.addAttribute("success", "¡Judoka registrado correctamente! Ahora puedes iniciar sesión.");
        return "redirect:/login";
    }

    private String validarDatosRegistro(JudokaRegistroDTO dto) {
        if (isNuloOVacio(dto.getUsername()) ||
            isNuloOVacio(dto.getPassword()) ||
            isNuloOVacio(dto.getNombre()) ||
            isNuloOVacio(dto.getApellido()) ||
            isNuloOVacio(dto.getCategoria()) ||
            isNuloOVacio(dto.getFechaNacimiento())) {
            return "Todos los campos son obligatorios.";
        }
        return null;
    }

    private boolean validarCredencialesDisponibles(String username) {
        return judokaService.findByUsername(username).isEmpty();
    }

    private boolean isNuloOVacio(String valor) {
        return valor == null || valor.isBlank();
    }

    private Judoka mapearDtoAJudoka(JudokaRegistroDTO dto) {
        Judoka nuevo = new Judoka();
        nuevo.setUsername(dto.getUsername());
        nuevo.setPassword(dto.getPassword());
        nuevo.setNombre(dto.getNombre());
        nuevo.setApellido(dto.getApellido());
        nuevo.setCategoria(dto.getCategoria());
        nuevo.setFechaNacimiento(dto.getFechaNacimiento());
        return nuevo;
    }

}