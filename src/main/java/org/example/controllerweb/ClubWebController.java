package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.service.ClubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import jakarta.validation.Valid;
import org.springframework.validation.BindingResult;
import org.example.dto.ClubRegistroDTO;

@AllArgsConstructor
@Controller
public class ClubWebController {

    private final ClubService clubService;

    private static final String REGISTRO_CLUB = "Club/registro_club";

    @GetMapping("/club/home")
    public String clubHome(HttpSession session, Model model) {
        if (!esClub(session)) {
            return "redirect:/login";
        }
        String username = (String) session.getAttribute("username");
        Club club = clubService.findByUsername(username).orElse(null);
        if (club != null) {
            model.addAttribute("nombre", club.getNombre());
        } else {
            model.addAttribute("nombre", username);
        }
        return "Club/club_home";
    }

    @GetMapping("/lista")
    public String listarClubes(Model model) {
        List<Club> clubes = clubService.getAllClubs();
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
            bindingResult.rejectValue("username", "error.clubForm", "El correo " +
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
        model.addAttribute("success", "¡Club registrado correctamente! Ahora puedes iniciar sesión.");
        return REGISTRO_CLUB;
    }

    @GetMapping("/registro-club")
    public String showRegistroClub(Model model) {
        if (!model.containsAttribute("clubForm")) {
            model.addAttribute("clubForm", new ClubRegistroDTO());
        }
        return REGISTRO_CLUB;
    }

    private boolean esClub(HttpSession session) {
        return session.getAttribute("username") != null && "club".equals(session.getAttribute("tipo"));
    }
}