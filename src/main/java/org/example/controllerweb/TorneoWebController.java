package org.example.controllerweb;

import org.example.model.competencia.Torneo;
import org.example.model.user.Judoka;
import org.example.model.logger.LoggerManager;
import org.example.service.TorneoService;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.logging.Logger;

/**
 * The type Torneo web controller.
 */
@Controller
public class TorneoWebController {

    private final TorneoService torneoService;
    private final JudokaService judokaService;
    private static final Logger logger = LoggerManager.getLogger(TorneoWebController.class);

    /**
     * Instantiates a new Torneo web controller.
     *
     * @param torneoService the torneo service
     * @param judokaService the judoka service
     */
    public TorneoWebController(TorneoService torneoService, JudokaService judokaService) {
        this.torneoService = torneoService;
        this.judokaService = judokaService;
    }

    /**
     * Listar torneos string.
     *
     * @param model the model
     * @return the string
     */
    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        List<Torneo> torneos = torneoService.listarTorneos();
        model.addAttribute("torneos", torneos);
        return "torneos";
    }

    /**
     * Cargar judokas list.
     *
     * @return the list
     */
    @ModelAttribute("judokas")
    public List<Judoka> cargarJudokas() {
        return judokaService.listarJudokas();
    }

    /**
     * Mostrar crear torneo string.
     *
     * @return the string
     */
    @GetMapping("/torneos/crear")
    public String mostrarCrearTorneo() {
        return "crear_torneo";
    }

    /**
     * Crear torneo string.
     *
     * @param nombre        the nombre
     * @param fecha         the fecha
     * @param participantes the participantes
     * @return the string
     */
    @PostMapping("/torneos/crear")
    public String crearTorneo(@RequestParam String nombre,
                              @RequestParam String fecha,
                              @RequestParam List<Long> participantes) {
        if (nombre == null || nombre.isEmpty() || fecha == null || fecha.isEmpty()) {
            return "redirect:/torneos/crear?error=DatosInvalidos";
        }
        if (participantes == null || participantes.isEmpty()) {
            return "redirect:/torneos/crear?error=NoParticipantes";
        }
        List<Judoka> judokasSeleccionados = judokaService.buscarPorIds(participantes);
        if (judokasSeleccionados.isEmpty()) {
            return "redirect:/torneos/crear?error=ParticipantesNoValidos";
        }

        Torneo nuevoTorneo = new Torneo(nombre, fecha, judokasSeleccionados);
        torneoService.guardarTorneo(nuevoTorneo);

        return "redirect:/torneos";
    }

}
