package org.example;

import org.example.model.judoka.Judoka;
import org.example.model.judoka.GestionJudokas;
import org.example.model.competencia.Competencia;
import org.example.model.competencia.GestionarCompetencia;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionarCompetenciaTest {

    private GestionarCompetencia gestor;
    private Judoka judoka1;
    private Judoka judoka2;
    private Judoka judoka3;

    @BeforeEach
    void setUp() {
        GestionJudokas gestionJudokas = new GestionJudokas();
        judoka1 = new Judoka("Ignacio", "Essus", "66kg", "2004-05-12");
        judoka2 = new Judoka("Benjamin", "Beroiza", "73kg", "2003-09-21");
        judoka3 = new Judoka("Alonso", "Romero", "81kg", "2002-08-30");
        gestionJudokas.getListaJudokas().addAll(Arrays.asList(judoka1, judoka2, judoka3));
        gestor = new GestionarCompetencia(gestionJudokas);
    }

    @Test
    void testAgregarCompetenciaDesdeConsola() {
        String input = "Copa Nacional\n2025-08-15\nIgnacio,Benjamin,Alonso\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.agregarCompetenciaDesdeConsola(scanner);

        assertEquals(1, gestor.getCompetencias().size());
        Competencia competencia = gestor.getCompetencias().getFirst();
        assertEquals("Copa Nacional", competencia.getNombre());

        // Comprobar que los nombres de los participantes están presentes (convertidos en Judoka)
        assertTrue(competencia.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Ignacio")));
        assertTrue(competencia.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Benjamin")));
        assertTrue(competencia.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Alonso")));
    }

    @Test
    void testRegistrarGanadorDesdeConsola_CompetenciaExistente() {
        List<Judoka> participantes = Arrays.asList(judoka1, judoka3);
        Competencia competencia = new Competencia("Regional 2025", "2025-09-01", participantes);
        gestor.getCompetencias().add(competencia);

        String input = "Regional 2025\nAlonso\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.registrarGanadorDesdeConsola(scanner);
        assertEquals(judoka3, competencia.getGanador());
    }

    @Test
    void testRegistrarGanadorDesdeConsola_JudokaInvalido() {
        List<Judoka> participantes = Arrays.asList(judoka1, judoka2);
        Competencia competencia = new Competencia("Torneo Invierno", "2025-12-12", participantes);
        gestor.getCompetencias().add(competencia);

        String input = "Torneo Invierno\nAlonso\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.registrarGanadorDesdeConsola(scanner);
        assertNull(competencia.getGanador()); // Alonso no está inscrito
    }

    @Test
    void testRegistrarGanadorDesdeConsola_CompetenciaInexistente() {
        String input = "Competencia Fantasma\nIgnacio\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        // Ejecuta, no debe lanzar excepción y evidentemente no se modificará ningún objeto
        assertDoesNotThrow(() -> gestor.registrarGanadorDesdeConsola(scanner));
    }
}