package org.example;

import org.example.model.user.Judoka;
import org.example.model.user.GestionJudokas;
import org.example.model.competencia.Torneo;
import org.example.model.competencia.GestionarTorneo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.util.Arrays;
import java.util.Scanner;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GestionarTorneoTest {

    private GestionarTorneo gestor;
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
        gestor = new GestionarTorneo(gestionJudokas);
    }

    @Test
    void testAgregarTorneoDesdeConsola() {
        String input = "Copa Nacional\n2025-08-15\nIgnacio,Benjamin,Alonso\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.agregarTorneoDesdeConsola(scanner);

        assertEquals(1, gestor.getTorneos().size());
        Torneo torneo = gestor.getTorneos().getFirst();
        assertEquals("Copa Nacional", torneo.getNombre());

        assertTrue(torneo.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Ignacio")));
        assertTrue(torneo.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Benjamin")));
        assertTrue(torneo.getParticipantes().stream().anyMatch(a -> a.getNombre().equals("Alonso")));
    }

    @Test
    void testRegistrarGanadorDesdeConsola_TorneoExistente() {
        List<Judoka> participantes = Arrays.asList(judoka1, judoka3);
        Torneo torneo = new Torneo("Regional 2025", "2025-09-01", participantes);
        gestor.getTorneos().add(torneo);

        String input = "Regional 2025\nAlonso\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.registrarGanadorDesdeConsola(scanner);
        assertEquals(judoka3, torneo.getGanador());
    }

    @Test
    void testRegistrarGanadorDesdeConsola_JudokaInvalido() {
        List<Judoka> participantes = Arrays.asList(judoka1, judoka2);
        Torneo torneo = new Torneo("Torneo Invierno", "2025-12-12", participantes);
        gestor.getTorneos().add(torneo);

        String input = "Torneo Invierno\nAlonso\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        gestor.registrarGanadorDesdeConsola(scanner);
        assertNull(torneo.getGanador());
    }

    @Test
    void testRegistrarGanadorDesdeConsola_TorneoInexistente() {
        String input = "Torneo Fantasma\nIgnacio\n";
        ByteArrayInputStream in = new ByteArrayInputStream(input.getBytes());
        Scanner scanner = new Scanner(in);

        assertDoesNotThrow(() -> gestor.registrarGanadorDesdeConsola(scanner));
    }
}
