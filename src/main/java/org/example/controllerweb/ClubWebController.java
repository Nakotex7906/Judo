package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.dto.ClubRegistroDTO;
import org.example.model.user.Club;
import org.example.model.user.Judoka;
import org.example.service.ClubService;
import org.example.service.JudokaService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;

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
    public String listarClubes(Model model) {
        // MODIFICADO: Obtenemos los clubes ordenados por nombre.
        List<Club> clubes = clubService.getAllClubs(Sort.by("nombre"));
        model.addAttribute("clubes", clubes);
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
}