package org.example.controllerweb;

import lombok.RequiredArgsConstructor;
import org.example.model.user.Judoka;
import org.example.service.RankingService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controlador web para mostrar el ranking de judokas.
 */
@Controller
@RequiredArgsConstructor
public class RankingWebController {

    private static final Logger logger = Logger.getLogger(RankingWebController.class.getName());
    private static final String VISTA_RANKING = "Judoka/ranking";

    private final RankingService rankingService;

    /**
     * Muestra el ranking filtrado por categoría (o completo si no se selecciona).
     */
    @GetMapping("/ranking")
    public String mostrarRanking(
            @RequestParam(value = "categoria", required = false) String categoria,
            Model model
    ) {
        List<Judoka> ranking;
        if (categoria != null && !categoria.isBlank()) {
            ranking = rankingService.obtenerRankingPorCategoria(categoria);
        } else {
            ranking = rankingService.obtenerRankingJudokas();
        }

        if (ranking.isEmpty()) {
            logger.log(Level.INFO, "No hay judokas para mostrar en el ranking.");
        }

        model.addAttribute("ranking", ranking);
        model.addAttribute("categoriaSeleccionada", categoria);
        model.addAttribute("categorias", getCategoriasDePeso());
        return VISTA_RANKING;
    }

    private List<String> getCategoriasDePeso() {
        return Arrays.asList("-60 kg", "-66 kg", "-73 kg", "-81 kg", "-90 kg", "-100 kg", "+100 kg");
    }
}
