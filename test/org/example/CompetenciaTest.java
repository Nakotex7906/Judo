package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CompetenciaTest {
    private Competencia competencia;

    @BeforeEach
    void setUp() {
        competencia = new Competencia("Torneo Regional", "2025-06-01",
                java.util.Arrays.asList("Ignacio Essus", "Benjamin Beroiza"));
    }

    @Test
    void testRegistrarGanadorValido() {
        competencia.registrarGanador("Ignacio Essus");
        assertEquals("Ignacio Essus", competencia.getGanador());
    }

    @Test
    void testRegistrarGanadorInvalido() {
        competencia.registrarGanador("Alonso Romero");
        assertNull(competencia.getGanador());
    }
}
