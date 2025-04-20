package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class GestionarCompetenciaTest {

    private GestionarCompetencia gestor;

    @BeforeEach
    void setUp() {
        gestor = new GestionarCompetencia();
    }

    @Test
    void testAgregarCompetenciaDesdeConsola() {
        String input = "Copa Nacional\n2025-08-15\nIgnacio Essus,Benjamin Beroiza,Alonso Romero\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.agregarCompetenciaDesdeConsola(scanner);

        assertEquals(1, gestor.getCompetencias().size());
        Competencia competencia = gestor.getCompetencias().getFirst();
        assertEquals("Copa Nacional", competencia.getNombre());
        assertTrue(competencia.getParticipantes().contains("Ignacio Essus"));
    }

    @Test
    void testRegistrarGanadorDesdeConsola_CompetenciaExistente() {
        Competencia competencia = new Competencia("Regional 2025", "2025-09-01",
                java.util.Arrays.asList("Ignacio Essus", "Alonso Romero"));
        gestor.getCompetencias().add(competencia);

        String input = "Regional 2025\nAlonso Romero\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.registrarGanadorDesdeConsola(scanner);
        assertEquals("Alonso Romero", competencia.getGanador());
    }
}
