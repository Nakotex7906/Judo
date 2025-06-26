package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.dto.JudokaRegistroDTO;
import org.example.model.logger.LoggerManager;
import org.example.model.user.Judoka;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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

    // MODIFICADO: Nueva ruta para ver el perfil PÚBLICO de un judoka.
    @GetMapping("/judoka/publico/{id}")
    public String verPerfilPublicoJudoka(@PathVariable Long id, Model model) {
        Optional<Judoka> judokaOpt = judokaService.buscarPorId(id);
        if (judokaOpt.isEmpty()) {
            return "redirect:/judokas"; // Si no se encuentra, vuelve a la lista.
        }
        model.addAttribute("judoka", judokaOpt.get());
        return "Judoka/judoka_home"; // Devuelve la vista de solo lectura.
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
        // Esta ruta ahora la usaremos para los perfiles públicos.
        // La redirección después del login va a /index.
        // El perfil personal se maneja con /perfil en AuthController.
        // Podemos mantenerla por si se necesita en el futuro o eliminarla. Por ahora la dejamos.
        if (!esJudoka(session)) return "redirect:/login";
        String username = (String) session.getAttribute("username");

        Judoka judoka = judokaService.findByUsername(username).orElse(null);
        model.addAttribute("judoka", judoka);
        return "Judoka/judoka_home";
    }

    // -- REGISTRO DE JUDOKA --

    @GetMapping("/registro-judoka")
    public String showRegistroJudoka(Model model) {
        model.addAttribute("categorias", getCategoriasDePeso());
        model.addAttribute("judokaRegistroDTO", new JudokaRegistroDTO());
        return REGISTRO_JUDOKA;
    }

    @PostMapping("/registro-judoka")
    public String doRegistroJudoka(@ModelAttribute JudokaRegistroDTO dto, Model model) {
        model.addAttribute("categorias", getCategoriasDePeso());

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

        // Puedes elegir:
        // 1. Mostrar mensaje de éxito en el mismo formulario (usuario sigue en el registro)

        model.addAttribute("success", "¡Judoka registrado correctamente! Ahora puedes iniciar sesión.");
        model.addAttribute("judokaRegistroDTO", new JudokaRegistroDTO());
        return "redirect:/login";

        // 2. O redirigir directamente al login (en ese caso el mensaje no se mostrará)
        // return "redirect:/login";
    }

    private List<String> getCategoriasDePeso() {
        return Arrays.asList("-60 kg", "-66 kg", "-73 kg", "-81 kg", "-90 kg", "-100 kg", "+100 kg");
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
