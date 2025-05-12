package org.example.controllerWeb;

import jakarta.servlet.http.HttpSession;
import lombok.AllArgsConstructor;
import org.example.model.club.Club;
import org.example.service.ClubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@AllArgsConstructor
@Controller
public class ClubController {

    private final ClubService clubService;

    /**
     * Devuelve la vista de inicio del club solo si la sesión pertenece a un club.
     */
    @GetMapping("/club/home")
    public String clubHome(HttpSession session, Model model) {
        if (!esClub(session)) {
            return "redirect:/login";
        }
        String username = (String) session.getAttribute("username");
        // Buscar el club por username
        Club club = clubService.findByUsername(username).orElse(null);
        if (club != null) {
            model.addAttribute("nombre", club.getNombre());
        } else {
            model.addAttribute("nombre", username); // fallback
        }
        return "club_home";
    }

    /**
     * Muestra una lista de clubes registrados.
     */
    @GetMapping("/lista")
    public String listarClubes(Model model) {
        List<Club> clubes = clubService.getAllClubs();
        model.addAttribute("clubes", clubes);
        return "club_lista";
    }

    @GetMapping("/registro-club")
    public String showRegistroClub() {
        return "registro_club";
    }

    @PostMapping("/registro-club")
    public String doRegistroClub(
            @RequestParam String username,
            @RequestParam String password,
            @RequestParam String nombre,
            Model model
    ) {
        if(username == null || username.isBlank() ||
                password == null || password.isBlank() ||
                nombre == null || nombre.isBlank()) {
            model.addAttribute("error", "Todos los campos son obligatorios.");
            return "registro_club";
        }

        if (clubService.findByUsername(username).isPresent()) {
            model.addAttribute("error", "El correo ya está registrado para un club.");
            return "registro_club";
        }

        Club nuevoClub = new Club();
        nuevoClub.setUsername(username);
        nuevoClub.setPassword(password);
        nuevoClub.setNombre(nombre);

        clubService.guardarClub(nuevoClub);
        model.addAttribute("success", "¡Club registrado correctamente! Ahora puedes iniciar sesión.");
        return "registro_club";
    }

    /**
     * Comprueba si la sesión pertenece a un club logueado.
     */
    private boolean esClub(HttpSession session) {
        return session.getAttribute("username") != null && "club".equals(session.getAttribute("tipo"));
    }
}