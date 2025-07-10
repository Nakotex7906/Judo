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

/**
 * Controlador de autenticación y navegación principal.
 * <p>
 * Administra el acceso a las páginas principales de la aplicación, incluyendo login,
 * logout, perfil de usuario y páginas informativas como términos y condiciones.
 * </p>
 *
 * <p>Usa servicios como {@link JudokaService}, {@link ClubService}, {@link TorneoService}
 * y {@link RankingService} para obtener datos de la base de datos.</p>
 *
 * @author Ignacio Essus, Alonso Romero, Benjamin Beroiza
 */
@RequiredArgsConstructor
@Controller
public class AuthController {

    private static final String REDIRECT_LOGIN_USER_NOT_FOUND = "redirect:/login?error=user_not_found";
    private static final String DIRIGIR_LOGIN = "Model/login";

    private final JudokaService judokaService;
    private final ClubService clubService;
    private final TorneoService torneoService;
    private final RankingService rankingService;

    /**
     * Redirige la raíz del sitio hacia la página principal.
     *
     * @return redirección a "/index"
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    /**
     * Muestra la página de inicio del sistema con el top 3 del ranking y los torneos próximos.
     *
     * @param model el modelo de datos para la vista
     * @return nombre de la vista de inicio
     */
    @GetMapping("/index")
    public String index(Model model) {
        List<Judoka> top3Ranking = rankingService.obtenerRankingJudokas()
                .stream()
                .limit(3)
                .toList();
        model.addAttribute("top3Ranking", top3Ranking);

        List<Torneo> proximosTorneos = torneoService.listarTorneos();
        model.addAttribute("proximosTorneos", proximosTorneos);

        return "Model/index";
    }

    /**
     * Muestra la página de login.
     *
     * @return nombre de la vista de login
     */
    @GetMapping("/login")
    public String showLogin() {
        return DIRIGIR_LOGIN;
    }

    /**
     * Cierra la sesión del usuario actual.
     *
     * @param session sesión actual del usuario
     * @return redirección a login con mensaje de logout
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login?logout";
    }

    /**
     * Muestra la página de registro.
     *
     * @return nombre de la vista de registro
     */
    @GetMapping("/registro")
    public String showRegistro() {
        return "Model/registro";
    }

    /**
     * Redirige al perfil correspondiente del usuario dependiendo de su tipo (judoka o club).
     *
     * @param session sesión actual del usuario
     * @param model   modelo de datos para la vista
     * @return nombre de la vista del perfil o redirección si el usuario no se encuentra
     */
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
     * Muestra la vista de términos y condiciones.
     *
     * @return nombre de la vista de términos
     */
    @GetMapping("/terminos")
    public String mostrarTerminosYCondiciones() {
        return "Model/terminos_y_condiciones";
    }

    /**
     * Maneja la lógica para mostrar el perfil de un judoka.
     *
     * @param username nombre de usuario del judoka
     * @param model    modelo para la vista
     * @return vista del perfil o redirección si no se encuentra el judoka
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
     * Prepara el modelo con la información del judoka y su posición en el ranking.
     *
     * @param model  modelo para la vista
     * @param judoka objeto judoka con los datos del usuario
     */
    private void prepareModelForJudokaProfile(Model model, Judoka judoka) {
        model.addAttribute("judoka", judoka);
        model.addAttribute("victoriasPodio", judoka.getVictorias());
        model.addAttribute("derrotas", judoka.getDerrotas());

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
     * @param username nombre de usuario del club
     * @param model    modelo para la vista
     * @return vista del perfil del club o redirección si no se encuentra el club
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
}
