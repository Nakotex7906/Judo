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
 * Controlador web para mostrar el ranking de judokas en la aplicación.
 * <p>
 * Permite listar el ranking completo o filtrarlo por categorías de peso.
 * Utiliza el {@link RankingService} para obtener la información correspondiente.
 * </p>
 *
 * @author Benjamin Beroiza
 */
@Controller
@RequiredArgsConstructor
public class RankingWebController {

    /** Logger para registro de eventos informativos o de diagnóstico */
    private static final Logger logger = Logger.getLogger(RankingWebController.class.getName());

    /** Ruta a la vista Thymeleaf del ranking */
    private static final String VISTA_RANKING = "Judoka/ranking";

    /** Servicio de lógica que obtiene los datos de ranking */
    private final RankingService rankingService;

    /**
     * Muestra el ranking de judokas. Si se especifica una categoría, filtra por ella;
     * en caso contrario, muestra el ranking completo.
     *
     * @param categoria categoría de peso seleccionada (puede ser nula o vacía)
     * @param model     modelo que se pasa a la vista
     * @return nombre de la vista Thymeleaf
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

    /**
     * Devuelve una lista con las categorías oficiales de peso utilizadas para los rankings.
     *
     * @return lista de cadenas con categorías de peso
     */
    private List<String> getCategoriasDePeso() {
        return Arrays.asList("-60 kg", "-66 kg", "-73 kg", "-81 kg", "-90 kg", "-100 kg", "+100 kg");
    }
}
