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

@Controller
public class TorneoWebController {

    private final TorneoService torneoService;
    private final JudokaService judokaService;
    private static final Logger logger = LoggerManager.getLogger(TorneoWebController.class);

    public TorneoWebController(TorneoService torneoService, JudokaService judokaService) {
        this.torneoService = torneoService;
        this.judokaService = judokaService;
    }

    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        List<Torneo> torneos = torneoService.listarTorneos();
        model.addAttribute("torneos", torneos);
        return "Torneo/torneos";
    }

    @ModelAttribute("judokas")
    public List<Judoka> cargarJudokas() {
        return judokaService.listarJudokas();
    }

    @GetMapping("/torneos/crear")
    public String mostrarCrearTorneo() {
        return "Torneo/crear_torneo";
    }

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

    @GetMapping("/torneos/{id}")
    public String verTorneo(@PathVariable Long id, Model model) {
        return torneoService.buscarPorId(id)
                .map(t -> {
                    model.addAttribute("torneo", t);
                    return "Torneo/torneo_home";
                })
                .orElse("redirect:/torneos");
    }

    @PostMapping("/torneos/{id}/eliminar-participante/{judokaId}")
    public String eliminarParticipante(@PathVariable Long id, @PathVariable Long judokaId) {
        torneoService.eliminarParticipante(id, judokaId);
        return "redirect:/torneos/" + id;
    }
}
