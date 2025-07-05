package org.example.controllerweb;


import java.util.List;
import org.example.model.competencia.Torneo;
import org.example.model.user.Judoka;
import org.example.service.ClubService;
import org.example.service.JudokaService;
import org.example.service.RankingService;
import org.example.service.TorneoService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@Controller
public class AuthController {

    private static final String REDIRECT_LOGIN_USER_NOT_FOUND = "redirect:/login?error=user_not_found";

    private final JudokaService judokaService;
    private final ClubService clubService;
    private final TorneoService torneoService;
    private final RankingService rankingService;

    private static final String DIRIGIR_LOGIN = "Model/login";

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) {
        // 1. Cargar el Top 3 del Ranking
        List<Judoka> top3Ranking = rankingService.obtenerRankingJudokas()
                .stream()
                .limit(3) // Tomamos solo los primeros 3
                .toList();
        model.addAttribute("top3Ranking", top3Ranking);

        // 2. Cargar los próximos torneos
        List<Torneo> proximosTorneos = torneoService.listarTorneos();
        model.addAttribute("proximosTorneos", proximosTorneos);

        return "Model/index";
    }

    @GetMapping("/login")
    public String showLogin() {
        // La lógica de redirección si ya está logueado se puede simplificar o manejar en un filtro.
        // Por ahora la dejamos así, pero el CustomAuthenticationSuccessHandler previene el re-login.
        return DIRIGIR_LOGIN;
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }


    @GetMapping("/registro")
    public String showRegistro() {
        return "Model/registro";
    }

    // Nuevo método para redirigir a la vista de perfil correspondiente

    @GetMapping("/perfil")
    public String verPerfil(HttpSession session, Model model) {
        String username = (String) session.getAttribute("username");
        if (username == null) {
            return "redirect:/login";
        }

        String tipo = (String) session.getAttribute("tipo");

        if ("judoka".equalsIgnoreCase(tipo)) {
            return handleJudokaProfile(username, model);
        } else if ("club".equalsIgnoreCase(tipo)) {
            return handleClubProfile(username, model);
        }

        session.invalidate();
        return REDIRECT_LOGIN_USER_NOT_FOUND;

    }

    /**
     * Maneja la lógica para mostrar el perfil de un judoka.
     * Utiliza Optional para un manejo más limpio de la posible ausencia del judoka.
     *
     * @param username El nombre de usuario del judoka.
     * @param model    El modelo para pasar datos a la vista.
     * @return El nombre de la vista o una redirección si el judoka no se encuentra.
     */
    private String handleJudokaProfile(String username, Model model) {
        return judokaService.findByUsername(username)
                .map(judoka -> {
                    prepareModelForJudokaProfile(model, judoka);
                    return "Judoka/Perfil_Judoka";
                })
                .orElse(REDIRECT_LOGIN_USER_NOT_FOUND);
    }

    /**
     * Prepara el modelo con todos los datos necesarios para la vista del perfil del judoka.
     *
     * @param model  El modelo a preparar.
     * @param judoka El judoka cuyos datos se usarán.
     */
    private void prepareModelForJudokaProfile(Model model, Judoka judoka) {
        model.addAttribute("judoka", judoka);
        model.addAttribute("victoriasPodio", judoka.getVictorias());
        model.addAttribute("derrotas", judoka.getDerrotas());

        // Calcula y añade el puesto en el ranking de forma más funcional
        rankingService.obtenerRankingJudokas().stream()
                .filter(j -> j.getId() == judoka.getId())
                .findFirst()
                .ifPresent(j -> {
                    int puesto = rankingService.obtenerRankingJudokas().indexOf(j) + 1;
                    model.addAttribute("rankingPuesto", puesto);
                });
    }

    /**
     * Maneja la lógica para mostrar el perfil de un club.
     *
     * @param username El nombre de usuario del club.
     * @param model    El modelo para pasar datos a la vista.
     * @return El nombre de la vista o una redirección si el club no se encuentra.
     */
    private String handleClubProfile(String username, Model model) {
        return clubService.findByUsernameWithJudokas(username)
                .map(club -> {
                    model.addAttribute("club", club);
                    model.addAttribute("torneos", torneoService.listarTorneos());
                    return "Club/Perfil_Club";
                })
                .orElse(REDIRECT_LOGIN_USER_NOT_FOUND);
    }


    //Metodo para acceder a terminar y condiciones
    @GetMapping("/terminos")
    public String mostrarTerminosYCondiciones() {
        return "Model/terminos_y_condiciones";
    }

    @PostMapping("/judoka/eliminar")
    public String eliminarCuentaJudoka(HttpSession session) {
        String username = (String) session.getAttribute("username");
        if (username != null && "judoka".equals(session.getAttribute("tipo"))) {
            judokaService.eliminarPorUsername(username);
            session.invalidate();
            return "redirect:/login?deleted";
        }
        return "redirect:/login";
    }

}
