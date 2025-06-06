package org.example.model.competencia;

import org.example.model.user.Judoka;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class TorneoTest {
    private Torneo torneo;
    private Judoka judoka1;
    private Judoka judoka2;

    @BeforeEach
    void setUp() {
        judoka1 = new Judoka(1L, "Ignacio", "Essus", "66kg", "2004-05-12");
        judoka2 = new Judoka(2L, "Benjamin", "Beroiza", "73kg", "2003-09-21");
        List<Judoka> participantes = Arrays.asList(judoka1, judoka2);
        torneo = new Torneo("Torneo Regional", "2025-06-01", participantes);
    }

    @Test
    void testRegistrarGanadorValido() {
        torneo.registrarGanador("Ignacio");
        assertEquals(judoka1, torneo.getGanador());
    }

    @Test
    void testRegistrarGanadorInvalido() {
        torneo.registrarGanador("Alonso Romero");
        assertNull(torneo.getGanador());
    }
}