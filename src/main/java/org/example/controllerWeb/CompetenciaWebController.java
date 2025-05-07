package org.example.controllerWeb;

import org.example.model.competencia.Competencia;
import org.example.model.judoka.Judoka;
import org.example.service.CompetenciaService;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * The type Competencia web controller.
 */
@Controller
public class CompetenciaWebController {

    private final CompetenciaService competenciaService;
    private final JudokaService judokaService;

    /**
     * Instantiates a new Competencia web controller.
     *
     * @param competenciaService the competencia service
     * @param judokaService      the judoka service
     */
    public CompetenciaWebController(CompetenciaService competenciaService, JudokaService judokaService) {
        this.competenciaService = competenciaService;
        this.judokaService = judokaService;
    }

    /**
     * Listar competencias string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/competencias")
    public String listarCompetencias(Model model) {
        List<Competencia> competencias = competenciaService.listarCompetencias();
        model.addAttribute("competencias", competencias);
        return "competencias";
    }

    /**
     * Detalle competencia string.
     *
     * @param id    the id
     * @param model the model
     * @return the string
     */
    @GetMapping("/competencias/detalle/{id}")
    public String detalleCompetencia(@PathVariable Long id, Model model) {
        Competencia competencia = competenciaService.buscarPorId(id)
                .orElse(null); // Manejar si no existe
        model.addAttribute("competencia", competencia);
        return "detalle_competencia"; // Nombre del template HTML
    }

    /**
     * Mostrar crear competencia string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/competencias/crear")
    public String mostrarCrearCompetencia(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        model.addAttribute("judokas", judokas);
        return "crear_competencia";
    }

    /**
     * Crear competencia string.
     *
     * @param nombre        the nombre
     * @param fecha         the fecha
     * @param participantes the participantes
     * @return the string
     */
    @PostMapping("/competencias/crear")
    public String crearCompetencia(@RequestParam String nombre,
                                   @RequestParam String fecha,
                                   @RequestParam List<Long> participantes) {
        List<Judoka> judokasSeleccionados = judokaService.buscarPorIds(participantes);
        Competencia nueva = new Competencia(nombre, fecha, judokasSeleccionados);
        competenciaService.guardarCompetencia(nueva);
        return "redirect:/competencias";
    }

}
