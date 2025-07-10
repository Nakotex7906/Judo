package org.example.model.competencia;

import org.example.model.user.Judoka;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

/**
 * Pruebas unitarias para la clase {@link Torneo}.
 * Verifica el comportamiento al registrar ganadores válidos o inválidos.
 */
class TorneoTest {

    private Torneo torneo;
    private Judoka judoka1;
    private Judoka judoka2;

    /**
     * Configura el entorno antes de cada prueba.
     * Se crea un torneo con dos participantes: Ignacio y Benjamin.
     */
    @BeforeEach
    void setUp() {
        judoka1 = new Judoka(1L, "Ignacio", "Essus", "66kg", "2004-05-12");
        judoka2 = new Judoka(2L, "Benjamin", "Beroiza", "73kg", "2003-09-21");
        List<Judoka> participantes = Arrays.asList(judoka1, judoka2);
        torneo = new Torneo("Torneo Regional", "2025-06-01", participantes);
    }

    /**
     * Verifica que se registre correctamente un ganador
     * cuando el nombre coincide con un participante.
     */
    @Test
    void testRegistrarGanadorValido() {
        torneo.registrarGanador("Ignacio");
        assertEquals(judoka1, torneo.getGanador());
    }

    /**
     * Verifica que no se registre ningún ganador
     * cuando el nombre no coincide con los participantes.
     */
    @Test
    void testRegistrarGanadorInvalido() {
        torneo.registrarGanador("Alonso Romero");
        assertNull(torneo.getGanador());
    }
}
