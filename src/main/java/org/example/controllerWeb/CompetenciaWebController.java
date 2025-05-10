package org.example.controllerWeb;

import org.example.model.competencia.Competencia;
import org.example.model.judoka.Judoka;
import org.example.model.logger.LoggerManager;
import org.example.service.CompetenciaService;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Competencia web controller.
 */
@Controller
public class CompetenciaWebController {

    private final CompetenciaService competenciaService;
    private final JudokaService judokaService;
    private static final Logger logger = LoggerManager.getLogger(CompetenciaWebController.class);

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
    @GetMapping("/competencias/detalle/{id}") //Se agrego un mensaje de error
    public String detalleCompetencia(@PathVariable Long id, Model model) {
        logger.log(Level.INFO, "Intentando mostrar detalles de la competencia con id: " + id);
        Competencia competencia = competenciaService.buscarPorId(id)
                .orElseThrow(()-> new RuntimeException("Competencia no encontrada"));
        return "detalle_competencia"; // Nombre del template HTML
    }

    /**
     * Cargar judokas list.
     *
     * @return the list
     */
    @ModelAttribute("judokas") //con este metodo la lista de judokas se agrega automaticamente, ejemplo metodo mostrarCrearCompetencia
    public List<Judoka>cargarJudokas(){
        return judokaService.listarJudokas();
    }

    /**
     * Mostrar crear competencia string.
     *
     * @return the string
     */
    @GetMapping("/competencias/crear")
    public String mostrarCrearCompetencia() {
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
    @PostMapping("/competencias/crear") //Se cambio el metodo para validar los parametros de entrada, linea 88 a 98
    public String crearCompetencia(@RequestParam String nombre,
                                   @RequestParam String fecha,
                                   @RequestParam List<Long> participantes) {
        if(nombre == null || nombre.isEmpty() || fecha == null || fecha.isEmpty()) {
            return "redirect:/competencias/crear?error=DatosInvalidos";
        }
        if (participantes == null || participantes.isEmpty()){
            return "redirect:/competencias/crear?error=NoParticipantes\n";
        }
        List<Judoka> judokasSeleccionados = judokaService.buscarPorIds(participantes);
        if (judokasSeleccionados.isEmpty()){
            return "redirect:/competencias/crear?error=ParticipantesNoValidos";

        }
        Competencia nueva = new Competencia(nombre, fecha, judokasSeleccionados);
        competenciaService.guardarCompetencia(nueva);

        return "redirect:/competencias";
    }

}
