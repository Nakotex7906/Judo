package org.example;

import org.example.model.atleta.Atleta;
import org.example.model.atleta.GestionAtletas;
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
    private Atleta atleta1;
    private Atleta atleta2;
    private Atleta atleta3;

    @BeforeEach
    void setUp() {
        GestionAtletas gestionAtletas = new GestionAtletas();
        atleta1 = new Atleta("Ignacio", "Essus", "66kg", "2004-05-12");
        atleta2 = new Atleta("Benjamin", "Beroiza", "73kg", "2003-09-21");
        atleta3 = new Atleta("Alonso", "Romero", "81kg", "2002-08-30");
        gestionAtletas.getListaAtletas().addAll(Arrays.asList(atleta1, atleta2, atleta3));
        gestor = new GestionarCompetencia(gestionAtletas);
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

        // Comprobar que los nombres de los participantes están presentes (convertidos en Atleta)
        assertTrue(competencia.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Ignacio")));
        assertTrue(competencia.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Benjamin")));
        assertTrue(competencia.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Alonso")));
    }

    @Test
    void testRegistrarGanadorDesdeConsola_CompetenciaExistente() {
        List<Atleta> participantes = Arrays.asList(atleta1, atleta3);
        Competencia competencia = new Competencia("Regional 2025", "2025-09-01", participantes);
        gestor.getCompetencias().add(competencia);

        String input = "Regional 2025\nAlonso\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.registrarGanadorDesdeConsola(scanner);
        assertEquals(atleta3, competencia.getGanador());
    }

    @Test
    void testRegistrarGanadorDesdeConsola_AtletaInvalido() {
        List<Atleta> participantes = Arrays.asList(atleta1, atleta2);
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