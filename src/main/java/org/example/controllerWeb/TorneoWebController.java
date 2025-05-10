package org.example.controllerWeb;

import org.example.model.competencia.Torneo;
import org.example.model.user.Judoka;
import org.example.service.TorneoService;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * Controlador web para gestionar torneos.
 */
@Controller
public class TorneoWebController {

    private final TorneoService torneoService;
    private final JudokaService judokaService;

    /**
     * Constructor del controlador de torneos.
     *
     * @param torneoService el servicio de torneos
     * @param judokaService el servicio de judokas
     */
    public TorneoWebController(TorneoService torneoService, JudokaService judokaService) {
        this.torneoService = torneoService;
        this.judokaService = judokaService;
    }

    /**
     * Lista todos los torneos.
     *
     * @param model el modelo de la vista
     * @return el nombre del template
     */
    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        List<Torneo> torneos = torneoService.listarTorneos();
        model.addAttribute("torneos", torneos);
        return "torneos";
    }

    /**
     * Muestra el detalle de un torneo.
     *
     * @param id    el id del torneo
     * @param model el modelo de la vista
     * @return el nombre del template
     */
    @GetMapping("/torneos/detalle/{id}")
    public String detalleTorneo(@PathVariable Long id, Model model) {
        Torneo torneo = torneoService.buscarPorId(id)
                .orElse(null);
        model.addAttribute("torneo", torneo);
        return "detalle_torneo";
    }

    /**
     * Muestra el formulario para crear un nuevo torneo.
     *
     * @param model el modelo
     * @return el nombre del template
     */
    @GetMapping("/torneos/crear")
    public String mostrarCrearTorneo(Model model) {
        List<Judoka> judokas = judokaService.listarJudokas();
        model.addAttribute("judokas", judokas);
        return "crear_torneo";
    }

    /**
     * Crea un nuevo torneo con los datos recibidos.
     *
     * @param nombre        el nombre del torneo
     * @param fecha         la fecha del torneo
     * @param participantes los IDs de los judokas participantes
     * @return redirección a la lista de torneos
     */
    @PostMapping("/torneos/crear")
    public String crearTorneo(@RequestParam String nombre,
                              @RequestParam String fecha,
                              @RequestParam List<Long> participantes) {
        List<Judoka> judokasSeleccionados = judokaService.buscarPorIds(participantes);
        Torneo nuevo = new Torneo(nombre, fecha, judokasSeleccionados);
        torneoService.guardarTorneo(nuevo);
        return "redirect:/torneos";
    }

}
