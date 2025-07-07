package org.example.controllerweb;

import org.example.model.competencia.Torneo;
import org.example.model.user.Judoka;
import org.example.service.TorneoService;
import org.example.service.JudokaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class TorneoWebControllerTest {

    private TorneoWebController torneoWebController;

    @Mock
    private TorneoService torneoService;

    @Mock
    private JudokaService judokaService;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        torneoWebController = new TorneoWebController(torneoService, judokaService);
    }

    /**
     * Prueba que listarTorneos carga correctamente los torneos en el modelo y devuelve la vista esperada.
     */
    @Test
    void testListarTorneos() {
        List<Torneo> torneos = List.of(new Torneo("Torneo 1", "2025-10-01", List.of()),
                new Torneo("Torneo 2", "2025-12-15", List.of()));

        when(torneoService.listarTorneos()).thenReturn(torneos);

        String result = torneoWebController.listarTorneos(model);

        assertEquals("Torneo/torneos", result);
        verify(model).addAttribute("torneos", torneos);
    }

    /**
     * Prueba que cargarJudokas devuelve la lista de judokas correctamente.
     */
    @Test
    void testCargarJudokas() {
        List<Judoka> judokas =
                List.of(new Judoka(1L, "Benjamin", "Beroiza", "73kg", "2003-09-21"),
                new Judoka(2L, "Ignacio", "Essus", "66kg", "2004-05-12"));

        when(judokaService.listarJudokas()).thenReturn(judokas);

        List<Judoka> result = torneoWebController.cargarJudokas();

        assertEquals(judokas, result);
        verify(judokaService).listarJudokas();
    }

    /**
     * Prueba que mostrarCrearTorneo devuelve la vista correcta de creación de torneos.
     */
    @Test
    void testMostrarCrearTorneo() {
        String result = torneoWebController.mostrarCrearTorneo();

        assertEquals("Torneo/crear_torneo", result);
    }

    /**
     * Prueba la creación de un torneo con datos válidos.
     */
    @Test
    void testCrearTorneoConDatosValidos() {
        List<Long> participantesIds = List.of(1L, 2L);
        List<Judoka> participantes = List.of(new Judoka(1L, "Benjamin", "Beroiza", "73kg", "2003-09-21"),
                new Judoka(2L, "Ignacio", "Essus", "66kg", "2004-05-12"));

        when(judokaService.buscarPorIds(participantesIds)).thenReturn(participantes);

        String result = torneoWebController.crearTorneo("Torneo de Prueba", "2025-10-20", participantesIds);

        assertEquals("redirect:/torneos", result);
        verify(judokaService).buscarPorIds(participantesIds);
        verify(torneoService).guardarTorneo(any(Torneo.class));

    }

    /**
     * Prueba la creación de un torneo con campos vacíos.
     */
    @Test
    void testCrearTorneoConCamposInvalidos() {
        String result = torneoWebController.crearTorneo("", "", null);

        assertEquals("redirect:/torneos/crear?error=DatosInvalidos", result);
        verifyNoInteractions(judokaService, torneoService);
    }

    /**
     * Prueba la creación de un torneo sin participantes.
     */
    @Test
    void testCrearTorneoSinParticipantes() {
        String result = torneoWebController.crearTorneo("Torneo Test", "2025-10-20", List.of());

        assertEquals("redirect:/torneos/crear?error=NoParticipantes", result);
        verifyNoInteractions(judokaService, torneoService);
    }

    /**
     * Prueba la creación de un torneo con identificadores de participantes no válidos.
     */
    @Test
    void testCrearTorneoConParticipantesNoValidos() {
        List<Long> participantesIds = List.of(10L, 20L);

        when(judokaService.buscarPorIds(participantesIds)).thenReturn(List.of());

        String result = torneoWebController.crearTorneo("Torneo Fallido", "2025-10-20", participantesIds);

        assertEquals("redirect:/torneos/crear?error=ParticipantesNoValidos", result);
        verify(judokaService).buscarPorIds(participantesIds);
        verify(torneoService, never()).guardarTorneo(any(Torneo.class));
    }

    @Test
    void testEliminarParticipantesExitoso() {
        Long torneoId = 1L;
        List<Long> participantesIds = List.of(1L, 2L);

        String result = torneoWebController.eliminarParticipantes(torneoId, participantesIds);

        assertEquals("redirect:/torneos", result);
        verify(torneoService).eliminarParticipantesDeTorneo(torneoId, participantesIds);
    }

    @Test
    void testVerTorneoExistente() {
        Long torneoId = 1L;
        Torneo torneo = new Torneo("Torneo Test", "2025-10-20", List.of());

        when(torneoService.buscarPorId(torneoId)).thenReturn(Optional.of(torneo));

        String result = torneoWebController.verTorneo(torneoId, model);

        assertEquals("Torneo/torneo_home", result);
        verify(model).addAttribute("torneo", torneo);
    }

    @Test
    void testVerTorneoNoExistente() {
        Long torneoId = 1L;
        when(torneoService.buscarPorId(torneoId)).thenReturn(Optional.empty());

        String result = torneoWebController.verTorneo(torneoId, model);

        assertEquals("redirect:/torneos?error=TorneoNoEncontrado", result);
        verify(model, never()).addAttribute(eq("torneo"), any());
    }

    @Test
    void testEditarTorneoExitoso() {
        Long torneoId = 1L;
        Torneo torneo = new Torneo("Torneo Original", "2025-10-20", List.of());
        String nuevoNombre = "Torneo Actualizado";
        String nuevaFecha = "2025-11-15";

        when(torneoService.buscarPorId(torneoId)).thenReturn(Optional.of(torneo));

        String result = torneoWebController.editarTorneo(torneoId, nuevoNombre, nuevaFecha);

        assertEquals("redirect:/torneos/" + torneoId, result);
        verify(torneoService).guardarTorneo(torneo);
        assertEquals(nuevoNombre, torneo.getNombre());
        assertEquals(nuevaFecha, torneo.getFecha());
    }

    @Test
    void testEditarTorneoNoExistente() {
        Long torneoId = 1L;
        when(torneoService.buscarPorId(torneoId)).thenReturn(Optional.empty());

        String result = torneoWebController.editarTorneo(torneoId, "Nuevo Nombre", "2025-12-01");

        assertEquals("redirect:/torneos/" + torneoId, result);
        verify(torneoService, never()).guardarTorneo(any());
    }

    @Test
    void testEliminarTorneoExitoso() {
        Long torneoId = 1L;

        String result = torneoWebController.eliminarTorneo(torneoId);

        assertEquals("redirect:/torneos", result);
        verify(torneoService).eliminarTorneo(torneoId);
    }
}