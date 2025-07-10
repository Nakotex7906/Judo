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

/**
 * Controlador web que gestiona las operaciones relacionadas con torneos.
 * <p>
 * Permite listar, crear, editar, ver y eliminar torneos,
 * así como gestionar sus participantes.
 * </p>
 *
 * Utiliza {@link TorneoService} y {@link JudokaService} para la lógica de negocio.
 *
 * Ruta base: <code>/torneos</code>
 *
 * @author Alonso Romero, Ignacio Essus
 */
@Controller
public class TorneoWebController {

    private static final String REDIRECT_TORNEOS = "redirect:/torneos";

    private final TorneoService torneoService;
    private final JudokaService judokaService;

    /**
     * Constructor con inyección de dependencias.
     *
     * @param torneoService servicio para manejar torneos
     * @param judokaService servicio para manejar judokas
     */
    public TorneoWebController(TorneoService torneoService, JudokaService judokaService) {
        this.torneoService = torneoService;
        this.judokaService = judokaService;
    }

    /**
     * Lista todos los torneos existentes y los muestra en la vista.
     *
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista de listado de torneos
     */
    @GetMapping("/torneos")
    public String listarTorneos(Model model) {
        List<Torneo> torneos = torneoService.listarTorneos();
        model.addAttribute("torneos", torneos);
        return "Torneo/torneos";
    }

    /**
     * Carga todos los judokas en el modelo de forma automática para formularios relacionados.
     *
     * @return lista de judokas registrados
     */
    @ModelAttribute("judokas")
    public List<Judoka> cargarJudokas() {
        return judokaService.listarJudokas();
    }

    /**
     * Muestra el formulario para crear un nuevo torneo.
     *
     * @return nombre de la vista del formulario
     */
    @GetMapping("/torneos/crear")
    public String mostrarCrearTorneo() {
        return "Torneo/crear_torneo";
    }

    /**
     * Procesa la creación de un nuevo torneo con sus participantes.
     *
     * @param nombre nombre del torneo
     * @param fecha fecha del torneo
     * @param participantes lista de IDs de judokas participantes
     * @return redirección al listado de torneos o a la vista de creación con error
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

        return REDIRECT_TORNEOS;
    }

    /**
     * Elimina participantes específicos de un torneo.
     *
     * @param torneoId ID del torneo
     * @param participantesIds lista de IDs de los judokas a eliminar
     * @return redirección al listado de torneos
     */
    @PostMapping("/torneos/{torneoId}/eliminar-participantes")
    public String eliminarParticipantes(@PathVariable Long torneoId, @RequestParam List<Long> participantesIds) {
        torneoService.eliminarParticipantesDeTorneo(torneoId, participantesIds);
        return REDIRECT_TORNEOS;
    }

    /**
     * Muestra los detalles de un torneo específico.
     *
     * @param id ID del torneo
     * @param model modelo para pasar datos a la vista
     * @return nombre de la vista del torneo o redirección con error
     */
    @GetMapping("/torneos/{id}")
    public String verTorneo(@PathVariable Long id, Model model) {
        Torneo torneo = torneoService.buscarPorId(id).orElse(null);
        if (torneo == null) {
            return "redirect:/torneos?error=TorneoNoEncontrado";
        }
        model.addAttribute("torneo", torneo);
        return "Torneo/torneo_home";
    }

    /**
     * Edita el nombre y la fecha de un torneo.
     *
     * @param id ID del torneo
     * @param nombre nuevo nombre del torneo
     * @param fecha nueva fecha del torneo
     * @return redirección a la vista del torneo editado
     */
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

    /**
     * Elimina completamente un torneo.
     *
     * @param id ID del torneo a eliminar
     * @return redirección al listado de torneos
     */
    @PostMapping("/torneos/{id}/eliminar")
    public String eliminarTorneo(@PathVariable Long id) {
        torneoService.eliminarTorneo(id);
        return REDIRECT_TORNEOS;
    }
}
