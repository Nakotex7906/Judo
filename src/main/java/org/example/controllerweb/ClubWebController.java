package org.example.controllerweb;

import jakarta.persistence.EntityNotFoundException;
import org.example.model.user.Judoka;
import org.example.service.JudokaService;
import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.service.ClubService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.example.dto.ClubRegistroDTO;

/**
 * Controlador web para gestionar las vistas y operaciones relacionadas con clubes.
 * <p>
 * Permite registrar nuevos clubes, listar y editar su información, agregar judokas,
 * y mostrar tanto vistas privadas como públicas del club.
 * </p>
 *
 * Usa los servicios {@link ClubService} y {@link JudokaService} para acceder a la lógica de negocio.
 *
 * @author Ignacio Essus, Alonso Romero
 */
@AllArgsConstructor
@Controller
public class ClubWebController {

    private final ClubService clubService;
    private final JudokaService judokaService;

    private static final String REGISTRO_CLUB = "Club/registro_club";
    private static final String USERNAME = "username";
    private static final String REDIRECT_LOGIN = "redirect:/login";
    private static final String REDIRECT_PERFIL = "redirect:/perfil";

    /**
     * Vista principal del club si ha iniciado sesión.
     */
    @GetMapping("/club/home")
    public String clubHome(HttpSession session, Model model) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) return REDIRECT_LOGIN;

        return clubService.findByUsername(username)
                .map(club -> {
                    model.addAttribute("club", club);
                    return "Club/club_home";
                })
                .orElse(REDIRECT_LOGIN);
    }

    /**
     * Muestra el perfil público de un club por su ID.
     */
    @GetMapping("/club/publico/{id}")
    public String verPerfilPublicoClub(@PathVariable Long id, Model model) {
        Optional<Club> clubOpt = clubService.buscarPorIdConJudokas(id);
        if (clubOpt.isEmpty()) return "redirect:/lista";

        model.addAttribute("club", clubOpt.get());
        return "Club/club_home";
    }

    /**
     * Lista clubes, permite búsqueda por nombre o filtrado por letra inicial.
     */
    @GetMapping("/lista")
    public String listarClubes(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "letra", required = false) String letra,
            Model model) {

        List<Club> clubes = (nombre != null && !nombre.isBlank())
                ? clubService.buscarPorNombre(nombre)
                : clubService.getAllClubs(Sort.by("nombre"));

        if (letra != null && letra.length() == 1) {
            clubes = clubes.stream()
                    .filter(club -> club.getNombre().toUpperCase().startsWith(letra.toUpperCase()))
                    .toList();
        }

        model.addAttribute("clubes", clubes);
        model.addAttribute("nombre", nombre);
        model.addAttribute("letra", letra);
        return "Club/club_lista";
    }

    /**
     * Procesa el formulario de registro de un nuevo club.
     */
    @PostMapping("/registro-club")
    public String doRegistroClub(
            @Valid @ModelAttribute("clubForm") ClubRegistroDTO clubForm,
            BindingResult bindingResult,
            Model model) {

        if (clubService.findByUsername(clubForm.getUsername()).isPresent()) {
            bindingResult.rejectValue(USERNAME, "error.clubForm", "El correo ya está registrado para un club.");
        }

        if (bindingResult.hasErrors()) return REGISTRO_CLUB;

        Club nuevoClub = new Club();
        nuevoClub.setNombre(clubForm.getNombre());
        nuevoClub.setUsername(clubForm.getUsername());
        nuevoClub.setPassword(clubForm.getPassword());
        nuevoClub.setSensei(clubForm.getSensei());
        nuevoClub.setAnoFundacion(clubForm.getAnoFundacion());
        nuevoClub.setDireccion(clubForm.getDireccion());
        nuevoClub.setHorarios(clubForm.getHorarios());

        clubService.guardarClub(nuevoClub);
        model.addAttribute("success", "¡Club registrado correctamente! Ahora puedes iniciar sesión.");
        return REDIRECT_LOGIN;
    }

    /**
     * Muestra el formulario de registro de clubes.
     */
    @GetMapping("/registro-club")
    public String showRegistroClub(Model model) {
        if (!model.containsAttribute("clubForm")) {
            model.addAttribute("clubForm", new ClubRegistroDTO());
        }
        return REGISTRO_CLUB;
    }

    /**
     * Muestra el formulario para agregar judokas al club.
     */
    @GetMapping("/club/agregar-judoka-form")
    public String mostrarFormularioAgregarJudoka(Model model) {
        List<Judoka> judokasDisponibles = judokaService.listarJudokasSinClub();
        model.addAttribute("judokasDisponibles", judokasDisponibles);
        return "Club/add_judokas_form";
    }

    /**
     * Procesa la selección de judokas para asociarlos al club.
     */
    @PostMapping("/club/agregar-judoka")
    public String procesarAgregarJudoka(@RequestParam(name = "judokaIds", required = false) List<Long> judokaIds,
                                        HttpSession session, Model model) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) return REDIRECT_LOGIN;
        if (judokaIds == null || judokaIds.isEmpty()) return REDIRECT_PERFIL;

        Club club = clubService.findByUsername(username).orElse(null);
        if (club == null) return REDIRECT_LOGIN;

        List<Judoka> judokasParaAgregar = judokaService.buscarPorIds(judokaIds);
        for (Judoka judoka : judokasParaAgregar) {
            judoka.setClub(club);
            judokaService.guardarJudoka(judoka);
        }

        return REDIRECT_PERFIL;
    }

    /**
     * Actualiza la descripción del club.
     */
    @PostMapping("/club/actualizar-descripcion")
    public String actualizarDescripcion(@RequestParam("descripcion") String descripcion, HttpSession session) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) return REDIRECT_LOGIN;

        return clubService.findByUsername(username)
                .map(club -> {
                    club.setDescripcion(descripcion);
                    clubService.guardarClub(club);
                    return REDIRECT_PERFIL;
                })
                .orElse(REDIRECT_LOGIN);
    }

    /**
     * Actualiza los horarios del club.
     */
    @PostMapping("/club/actualizar-horarios")
    public String actualizarHorarios(@RequestParam String horarios, HttpSession session) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) return REDIRECT_LOGIN;

        return clubService.findByUsername(username)
                .map(club -> {
                    club.setHorarios(horarios);
                    clubService.guardarClub(club);
                    return REDIRECT_PERFIL;
                })
                .orElse(REDIRECT_LOGIN);
    }

    /**
     * Muestra la vista de confirmación para eliminar la cuenta del club.
     */
    @GetMapping("/perfil/eliminar_club")
    public String mostrarConfirmacionEliminarCuenta(HttpSession session, Model model) {
        return "Club/confirmar_eliminacion";
    }

    /**
     * Elimina la cuenta del club actualmente autenticado.
     */
    @PostMapping("/perfil/eliminar_club")
    public String eliminarCuentaClub(HttpSession session) {
        String username = (String) session.getAttribute(USERNAME);
        try {
            clubService.eliminarCuentaClub(username);
            session.invalidate();
            return REDIRECT_LOGIN + "?eliminado=true";
        } catch (EntityNotFoundException _) {
            return "redirect:/error?mensaje=Club no encontrado";
        }
    }
}
