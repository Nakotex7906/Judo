package org.example.service;

import org.example.model.competencia.Torneo;
import org.example.repository.TorneoRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link TorneoService}, que gestiona operaciones CRUD sobre torneos.
 * Se valida la interacción correcta con el {@link TorneoRepository}.
 */
class TorneoServiceTest {

    private TorneoRepository torneoRepositoryMock;
    private TorneoService torneoService;

    /**
     * Configura el mock del repositorio y el servicio antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        torneoRepositoryMock = mock(TorneoRepository.class);
        torneoService = new TorneoService(torneoRepositoryMock);
    }

    /**
     * Verifica que listarTorneos retorne todos los torneos del repositorio.
     */
    @Test
    void listarTorneos_devuelveListaCompleta() {
        List<Torneo> torneos = List.of(new Torneo(), new Torneo());
        when(torneoRepositoryMock.findAll()).thenReturn(torneos);

        List<Torneo> resultado = torneoService.listarTorneos();

        assertEquals(2, resultado.size());
        verify(torneoRepositoryMock).findAll();
    }

    /**
     * Verifica que buscarPorId devuelva el torneo correcto cuando existe.
     */
    @Test
    void buscarPorId_existente_devuelveOptionalConTorneo() {
        Torneo torneo = new Torneo();
        torneo.setId(1L);
        when(torneoRepositoryMock.findById(1L)).thenReturn(Optional.of(torneo));

        Optional<Torneo> resultado = torneoService.buscarPorId(1L);

        assertTrue(resultado.isPresent());
        assertEquals(1L, resultado.get().getId());
    }

    /**
     * Verifica que buscarPorId retorne un Optional vacío si no existe el torneo.
     */
    @Test
    void buscarPorId_noExiste_devuelveOptionalVacio() {
        when(torneoRepositoryMock.findById(99L)).thenReturn(Optional.empty());

        Optional<Torneo> resultado = torneoService.buscarPorId(99L);

        assertFalse(resultado.isPresent());
    }

    /**
     * Verifica que buscarPorNombre retorne una lista según el nombre especificado.
     */
    @Test
    void buscarPorNombre_devuelveListaTorneosConEseNombre() {
        List<Torneo> torneos = List.of(new Torneo(), new Torneo());
        when(torneoRepositoryMock.findByNombre("Nacional")).thenReturn(torneos);

        List<Torneo> resultado = torneoService.buscarPorNombre("Nacional");

        assertEquals(2, resultado.size());
        verify(torneoRepositoryMock).findByNombre("Nacional");
    }

    /**
     * Verifica que guardarTorneo invoque el repositorio y devuelva el objeto persistido.
     */
    @Test
    void guardarTorneo_guardaYDevuelveTorneo() {
        Torneo torneo = new Torneo();
        torneo.setNombre("Internacional");

        when(torneoRepositoryMock.save(torneo)).thenReturn(torneo);

        Torneo resultado = torneoService.guardarTorneo(torneo);

        assertEquals("Internacional", resultado.getNombre());
        verify(torneoRepositoryMock).save(torneo);
    }

    /**
     * Verifica que eliminarTorneo invoque el método deleteById del repositorio.
     */
    @Test
    void eliminarTorneo_eliminaPorId() {
        torneoService.eliminarTorneo(10L);
        verify(torneoRepositoryMock).deleteById(10L);
    }
}
