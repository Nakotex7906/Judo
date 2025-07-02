package org.example.controllerweb;


import java.util.List;
import org.example.model.competencia.Torneo;
import org.example.model.user.Club;
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
import java.util.List;

import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Controller
public class AuthController {

    // MODIFICADO: Inyectamos los servicios para buscar los datos del usuario.
    private final JudokaService judokaService;
    private final ClubService clubService;
    private final TorneoService torneoService; // <-- AÑADE ESTA LÍNEA
    private final RankingService rankingService; // <-- AÑADE RANKING SERVICE



    // MODIFICADO: Se eliminó la dependencia a AuthenticationService que ya no es necesaria aquí.
    private static final String DIRIGIR_LOGIN = "Model/login";

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index(Model model) { // <-- AÑADE EL MODELO COMO PARÁMETRO
        // 1. Cargar el Top 3 del Ranking
        List<Judoka> top3Ranking = rankingService.obtenerRankingJudokas()
                .stream()
                .limit(3) // Tomamos solo los primeros 3
                .collect(Collectors.toList());
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
        String tipo = (String) session.getAttribute("tipo");

        if (username == null) {
            return "redirect:/login";
        }

        if ("judoka".equalsIgnoreCase(tipo)) {
            Optional<Judoka> judokaOpt = judokaService.findByUsername(username);
            if (judokaOpt.isPresent()) {
                Judoka judoka = judokaOpt.get();
                model.addAttribute("judoka", judoka);
                // --- VVV LÓGICA NUEVA PARA DATOS ADICIONALES VVV ---
                // 1. Añadir datos para el gráfico
                model.addAttribute("victoriasPodio", judoka.getVictorias());
                model.addAttribute("derrotas", judoka.getDerrotas());

                // 2. Calcular y añadir el puesto en el ranking
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

                // Nota: La sección de "Historial de Competencias" requiere una estructura de datos
                // que tu modelo actual no tiene (ej. la tabla de resultados).
                // Por ahora, la dejaremos vacía en la vista.

                // --- ^^^ FIN DE LA LÓGICA NUEVA ^^^ ---
                return "Judoka/Perfil_Judoka";
            }
        } else if ("club".equalsIgnoreCase(tipo)) {
            Optional<Club> clubOpt = clubService.findByUsernameWithJudokas(username);
            if (clubOpt.isPresent()) {
                model.  addAttribute("club", clubOpt.get());
                List<Torneo> todosLosTorneos = torneoService.listarTorneos();
                model.addAttribute("torneos", todosLosTorneos);
                return "Club/Perfil_Club";

            }
        }

        session.invalidate();
        return "redirect:/login?error=user_not_found";
    }

    //Metodo para acceder a terminar y condiciones
    @GetMapping("/terminos")
    public String mostrarTerminosYCondiciones() {
        return "Model/terminos_y_condiciones";
    }

}
