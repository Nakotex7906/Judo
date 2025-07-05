package org.example.controllerweb;

import org.example.model.competencia.Torneo;
import org.example.model.user.Judoka;
import org.example.service.TorneoService;
import org.example.service.JudokaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class TorneoWebController {

    private static final String REDIRECT_TORNEOS = "redirect:/torneos";

    private final TorneoService torneoService;
    private final JudokaService judokaService;

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

        return REDIRECT_TORNEOS;
    }

    @PostMapping("/torneos/{torneoId}/eliminar-participantes")
    public String eliminarParticipantes(@PathVariable Long torneoId, @RequestParam List<Long> participantesIds) {
        torneoService.eliminarParticipantesDeTorneo(torneoId, participantesIds);
        return REDIRECT_TORNEOS;
    }

    @GetMapping("/torneos/{id}")
    public String verTorneo(@PathVariable Long id, Model model) {
        Torneo torneo = torneoService.buscarPorId(id).orElse(null);
        if (torneo == null) {
            return "redirect:/torneos?error=TorneoNoEncontrado";
        }
        model.addAttribute("torneo", torneo);
        return "Torneo/torneo_home";
    }

    @PostMapping("/torneos/{id}/editar")
    public String editarTorneo(@PathVariable Long id, @RequestParam String nombre, @RequestParam String fecha) {
        Optional<Torneo> torneoOpt = torneoService.buscarPorId(id);
        if (torneoOpt.isPresent()) {
            Torneo torneo = torneoOpt.get();
            torneo.setNombre(nombre);
            torneo.setFecha(fecha);
            torneoService.guardarTorneo(torneo);
        }
        return "redirect:/torneos/" + id;
    }

    @PostMapping("/torneos/{id}/eliminar")
    public String eliminarTorneo(@PathVariable Long id) {
        torneoService.eliminarTorneo(id);
        return REDIRECT_TORNEOS;
    }
}
