package org.example.controllerweb;

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

@AllArgsConstructor
@Controller
public class ClubWebController {

    private final ClubService clubService;
    private final JudokaService judokaService; // Inyectamos JudokaService para usarlo.


    private static final String REGISTRO_CLUB = "Club/registro_club";
    private static final String USERNAME = "username";
    private static final String REDIRECT_LOGIN = "redirect:/login";

    @GetMapping("/club/home")
    public String clubHome(HttpSession session, Model model) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) {
            return REDIRECT_LOGIN;
        }

        Optional<Club> clubOpt = clubService.findByUsername(username);

        if (clubOpt.isPresent()) {
            model.addAttribute("club", clubOpt.get());
            return "Club/club_home";
        } else {
            return REDIRECT_LOGIN;
        }
    }

    //Nueva ruta para ver el perfil público de un club.
    @GetMapping("/club/publico/{id}")
    public String verPerfilPublicoClub(@PathVariable Long id, Model model) {
        // MODIFICADO: Usamos el nuevo método que soluciona el problema de LazyInitializationException.
        Optional<Club> clubOpt = clubService.buscarPorIdConJudokas(id);
        if (clubOpt.isEmpty()) {
            return "redirect:/lista";
        }
        model.addAttribute("club", clubOpt.get());
        return "Club/club_home";
    }

    @GetMapping("/lista")
    public String listarClubes(
            @RequestParam(value = "nombre", required = false) String nombre,
            @RequestParam(value = "letra", required = false) String letra,
            Model model) {
        List<Club> clubes;
        if (nombre != null && !nombre.trim().isEmpty()) {
            // Si hay un término de búsqueda, se usa
            clubes = clubService.buscarPorNombre(nombre);
        } else {
            // Si no, se obtienen todos los clubes
            clubes = clubService.getAllClubs(Sort.by("nombre"));
        }
        // Si se ha seleccionado una letra, filtramos la lista resultante
        if (letra != null && !letra.trim().isEmpty() && letra.length() == 1) {
            clubes = clubes.stream()
                    .filter(club -> club.getNombre().toUpperCase().startsWith(letra.toUpperCase()))
                    .toList();
        }
        model.addAttribute("clubes", clubes);
        model.addAttribute("nombre", nombre); // Para mantener el valor en el buscador
        model.addAttribute("letra", letra);   // Para resaltar la letra activa
        return "Club/club_lista";
    }

    @PostMapping("/registro-club")
    public String doRegistroClub(
            @Valid @ModelAttribute("clubForm") ClubRegistroDTO clubForm,
            BindingResult bindingResult,
            Model model
    ) {
        if (clubService.findByUsername(clubForm.getUsername()).isPresent()) {
            bindingResult.rejectValue(USERNAME, "error.clubForm", "El correo " +
                    "ya está registrado para un club.");
        }

        if (bindingResult.hasErrors()) {
            return REGISTRO_CLUB;
        }

        Club nuevoClub = new Club();
        nuevoClub.setNombre(clubForm.getNombre());
        nuevoClub.setUsername(clubForm.getUsername());
        nuevoClub.setPassword(clubForm.getPassword());
        nuevoClub.setSensei(clubForm.getSensei());
        nuevoClub.setAnoFundacion(clubForm.getAnoFundacion());
        nuevoClub.setDireccion(clubForm.getDireccion());
        nuevoClub.setHorarios(clubForm.getHorarios());
        nuevoClub.setDescripcion(clubForm.getDescripcion());

        clubService.guardarClub(nuevoClub);
        model.addAttribute("success", "¡Club registrado correctamente! " +
                "Ahora puedes iniciar sesión.");
        return REDIRECT_LOGIN;
    }

    @GetMapping("/registro-club")
    public String showRegistroClub(Model model) {
        if (!model.containsAttribute("clubForm")) {
            model.addAttribute("clubForm", new ClubRegistroDTO());
        }
        return REGISTRO_CLUB;
    }

    // MODIFICADO: Nueva ruta para MOSTRAR el formulario de agregar judokas.
    @GetMapping("/club/agregar-judoka-form")
    public String mostrarFormularioAgregarJudoka(Model model) {
        List<Judoka> judokasDisponibles = judokaService.listarJudokasSinClub();
        model.addAttribute("judokasDisponibles", judokasDisponibles);
        return "Club/add_judokas_form";
    }

    // MODIFICADO: Nueva ruta para PROCESAR la adición de judokas al club.
    @PostMapping("/club/agregar-judoka")
    public String procesarAgregarJudoka(@RequestParam(name = "judokaIds", required = false) List<Long> judokaIds,
                                        HttpSession session, Model model) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) {
            return REDIRECT_LOGIN; // Si no hay sesión, fuera.
        }

        // Si no se seleccionó ningún judoka, simplemente volvemos al perfil.
        if (judokaIds == null || judokaIds.isEmpty()) {
            return "redirect:/perfil";
        }

        Club club = clubService.findByUsername(username).orElse(null);
        if (club == null) {
            return REDIRECT_LOGIN; // El usuario de la sesión no corresponde a un club.
        }

        // Buscamos a todos los judokas seleccionados por sus IDs.
        List<Judoka> judokasParaAgregar = judokaService.buscarPorIds(judokaIds);

        for (Judoka judoka : judokasParaAgregar) {
            judoka.setClub(club); // Asignamos este club al judoka.
            judokaService.guardarJudoka(judoka); // Guardamos el judoka actualizado.
        }

        return "redirect:/perfil"; // Redirigimos de vuelta al perfil del club.
    }

    // Permite eliminar un integrante del club
    @PostMapping("/club/eliminar-judoka/{judokaId}")
    public String eliminarJudoka(@PathVariable Long judokaId, HttpSession session) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) {
            return REDIRECT_LOGIN;
        }

        Optional<Judoka> judokaOpt = judokaService.buscarPorId(judokaId);
        Optional<Club> clubOpt = clubService.findByUsername(username);

        if (judokaOpt.isPresent() && clubOpt.isPresent()) {
            Judoka judoka = judokaOpt.get();
            Club club = clubOpt.get();
            if (judoka.getClub() != null && judoka.getClub().getId().equals(club.getId())) {
                judoka.setClub(null);
                judokaService.guardarJudoka(judoka);
            }
        }
        return "redirect:/perfil";
    }

    // Actualiza horarios y descripcion del club
    @PostMapping("/club/actualizar")
    public String actualizarClub(@ModelAttribute Club clubActualizado, HttpSession session) {
        String username = (String) session.getAttribute(USERNAME);
        if (username == null) {
            return REDIRECT_LOGIN;
        }
        clubService.findByUsername(username).ifPresent(club -> {
            club.setHorarios(clubActualizado.getHorarios());
            club.setDescripcion(clubActualizado.getDescripcion());
            clubService.guardarClub(club);
        });
        return "redirect:/perfil";
    }
}