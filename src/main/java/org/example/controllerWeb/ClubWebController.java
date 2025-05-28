package org.example.controllerWeb;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.service.ClubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@Controller
public class ClubWebController {

    private final ClubService clubService;

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

    @GetMapping("/registro-club")
    public String showRegistroClub() {
        return "Club/registro_club";
    }

    @PostMapping("/registro-club")
    public String doRegistroClub(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nombre,
            Model model
    ) {
        if (username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                nombre == null || nombre.isBlank()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "Club/registro_club";
        }

        if (clubService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado para un club.");
            return "Club/registro_club";
        }

        Club nuevoClub = new Club();
        nuevoClub.setUsername(username);
        nuevoClub.setPassword(password);
        nuevoClub.setNombre(nombre);

        clubService.guardarClub(nuevoClub);
        model.addAttribute("success", "¡Club registrado correctamente! Ahora puedes iniciar sesión.");
        return "Club/registro_club";
    }

    private boolean esClub(HttpSession session) {
        return session.getAttribute("username") != null && "club".equals(session.getAttribute("tipo"));
    }
}