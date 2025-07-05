package org.example.controllerweb;

import jakarta.persistence.EntityNotFoundException;
import org.example.service.RankingService;
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
    private static final String JUDOKA = "judoka";
    private static final String USERNAME = "username";
    private static final String LOGIN = "redirect:/login";
    private final JudokaService judokaService;
    private final RankingService rankingService; // <-- AÑADE RANKING SERVICE


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
        Judoka judoka = judokaOpt.get();
        model.addAttribute(JUDOKA, judoka);
        // --- VVV AÑADE ESTA MISMA LÓGICA QUE EN AUTHCONTROLLER VVV ---
        model.addAttribute("victoriasPodio", judoka.getVictorias());
        model.addAttribute("derrotas", judoka.getDerrotas());

        List<Judoka> rankingCompleto = rankingService.obtenerRankingJudokas();
        int puesto = -1;
        for (int i = 0; i < rankingCompleto.size(); i++) {
            if (rankingCompleto.get(i).getId() == judoka.getId()) {
                puesto = i + 1;
                break;
            }
        }
        if (puesto != -1) {
            model.addAttribute("rankingPuesto", puesto);
        }
        return "Judoka/judoka_home";
    }

    @PostMapping("/judokas")
    public String mostrarJudokas(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        model.addAttribute("judokas", judokas);
        return JUDOKA_VIEW;
    }

    private boolean esJudoka(HttpSession s) {
        return s.getAttribute(USERNAME) != null && JUDOKA.equals(s.getAttribute("tipo"));
    }

    @GetMapping("/judoka/home")
    public String judokaHome(HttpSession session, Model model) {
        // Esta ruta ahora la usaremos para los perfiles públicos.
        // La redirección después del login va a /index.
        // El perfil personal se maneja con /perfil en AuthController.
        // Podemos mantenerla por si se necesita en el futuro o eliminarla. Por ahora la dejamos.
        if (!esJudoka(session)) return LOGIN;
        String username = (String) session.getAttribute(USERNAME);

        Judoka judoka = judokaService.findByUsername(username).orElse(null);
        model.addAttribute(JUDOKA, judoka);
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
        return LOGIN;


    }

    //Editar Perfil.

    @PostMapping("/perfil/guardar")
    public String guardarPerfilJudoka(@ModelAttribute Judoka judokaActualizado, HttpSession session) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) {
            return LOGIN;
        }

        // Buscamos al judoka original en la BBDD para no perder datos
        Optional<Judoka> judokaOpt = judokaService.findByUsername(username);
        if (judokaOpt.isPresent()) {
            Judoka judokaOriginal = judokaOpt.get();

            // Actualizamos solo los campos que vienen del formulario
            judokaOriginal.setCategoria(judokaActualizado.getCategoria());
            judokaOriginal.setDescripcion(judokaActualizado.getDescripcion());
            judokaOriginal.setAniosEntrenamiento(judokaActualizado.getAniosEntrenamiento());
            judokaOriginal.setOficio(judokaActualizado.getOficio());

            judokaService.guardarJudoka(judokaOriginal); // Guardamos el objeto completo
        }

        return "redirect:/perfil"; // Redirigimos de vuelta al perfil
    }

    @GetMapping("/perfil/eliminar")
    public String mostrarConfirmacionEliminarCuenta(HttpSession session, Model model) {
        if (!esJudoka(session)) {
            return LOGIN;
        }
        return "Judoka/confirmar_eliminacion";
    }

    @PostMapping("/perfil/eliminar")
    public String eliminarCuenta(HttpSession session) {
        if (!esJudoka(session)) {
            return LOGIN;
        }

        String username = (String) session.getAttribute(USERNAME);
        try {
            judokaService.eliminarCuentaJudoka(username);
            session.invalidate();
            return LOGIN + "?eliminado=true";
        } catch (EntityNotFoundException _) {
            return "redirect:/error";
        }
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
