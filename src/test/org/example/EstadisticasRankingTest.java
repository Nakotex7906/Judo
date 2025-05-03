package org.example;

import org.example.model.judoka.Judoka;
import org.example.model.judoka.GestionJudokas;
import org.example.model.ranking.EstadisticasRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticasRankingTest {

    private GestionJudokas gestorJudokas;
    private EstadisticasRanking estadisticasRanking;

    @BeforeEach
    void setUp() {
        gestorJudokas = new GestionJudokas();
        estadisticasRanking = new EstadisticasRanking(gestorJudokas);
    }

    @Test
    void testMostrarEstadisticasDesdeConsolaJudokaRegistrado() {
        Judoka judoka = new Judoka("Ignacio", "Essus", "66kg", "2004-05-12");
        gestorJudokas.getListaJudokas().add(judoka);

        String input = "Ignacio\n";
        Scanner scanner = new Scanner(input);

        estadisticasRanking.mostrarEstadisticasDesdeConsola(scanner);
        Judoka encontrado = gestorJudokas.obtenerJudoka("Ignacio");
        assertNotNull(encontrado);
    }

    @Test
    void testMostrarEstadisticasDesdeConsolaJudokaNoRegistrado() {
        String input = "Fantasma\n";
        Scanner scanner = new Scanner(input);
        estadisticasRanking.mostrarEstadisticasDesdeConsola(scanner);
        Judoka noEncontrado = gestorJudokas.obtenerJudoka("Fantasma");
        assertNull(noEncontrado);
    }

    @Test
    void testCalcularRanking() {
        Judoka judoka1 = new Judoka("Ignacio", "Essus", "66kg", "2004-05-12");
        judoka1.aumentarVictoria();
        judoka1.aumentarVictoria();

        Judoka judoka2 = new Judoka("Alonso", "Romero", "66kg", "2004-03-28");
        judoka2.aumentarVictoria();

        Judoka judoka3 = new Judoka("Benjamin", "Beroiza", "73kg", "2003-09-21");

        gestorJudokas.getListaJudokas().add(judoka1);
        gestorJudokas.getListaJudokas().add(judoka2);
        gestorJudokas.getListaJudokas().add(judoka3);

        estadisticasRanking.calcularRanking();

        gestorJudokas.getListaJudokas().sort((a, b) -> Integer.compare(b.getVictorias(), a.getVictorias()));
        assertEquals("Ignacio", gestorJudokas.getListaJudokas().get(0).getNombre());
    }
}
