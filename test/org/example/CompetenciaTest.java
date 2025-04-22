package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

class CompetenciaTest {
    private Competencia competencia;
    private Atleta atleta1;
    private Atleta atleta2;

    @BeforeEach
    void setUp() {
        atleta1 = new Atleta("Ignacio", "Essus", "66kg", "2004-05-12");
        atleta2 = new Atleta("Benjamin", "Beroiza", "73kg", "2003-09-21");
        List<Atleta> participantes = Arrays.asList(atleta1, atleta2);
        competencia = new Competencia("Torneo Regional", "2025-06-01", participantes);
    }

    @Test
    void testRegistrarGanadorValido() {
        competencia.registrarGanador("Ignacio");
        assertEquals(atleta1, competencia.getGanador());
    }

    @Test
    void testRegistrarGanadorInvalido() {
        competencia.registrarGanador("Alonso Romero");
        assertNull(competencia.getGanador());
    }
}