package org.example.controllerWeb;

import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.service.ClubService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.List;

/**
 * The type Club web controller.
 */
@AllArgsConstructor
@Controller
public class ClubWebController {

    private final ClubService clubService;

    /**
     * Listar clubs string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/clubs")
    public String listarClubs(Model model) {
        List<Club> clubs = clubService.listarClubs();
        model.addAttribute("clubs", clubs);
        return "clubs"; // Asegúrate de tener una vista llamada "clubs.html"
    }

    /**
     * Agregar club string.
     *
     * @param nombre        the nombre
     * @param sensei        the sensei
     * @param ubicacion     the ubicacion
     * @param horarios      the horarios
     * @param fechaCreacion the fecha creacion
     * @return the string
     */
    @PostMapping("/clubs/agregar")
    public String agregarClub(@RequestParam String nombre,
                              @RequestParam String sensei,
                              @RequestParam String ubicacion,
                              @RequestParam String horarios,
                              @RequestParam String fechaCreacion) {
        LocalDate fecha = LocalDate.parse(fechaCreacion); // formato: yyyy-MM-dd
        Club nuevo = new Club(nombre, sensei, ubicacion, horarios, fecha);
        clubService.guardarClub(nuevo);
        return "redirect:/clubs";
    }
}
