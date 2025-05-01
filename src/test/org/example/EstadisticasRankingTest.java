package org.example;

import org.example.model.judoka.Judoka;
import org.example.model.judoka.GestionJudokas;
import org.example.model.ranking.EstadisticasRanking;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class EstadisticasRankingTest {

    private GestionJudokas gestorAtletas;
    private EstadisticasRanking estadisticasRanking;

    @BeforeEach
    void setUp() {
        gestorAtletas = new GestionJudokas();
        estadisticasRanking = new EstadisticasRanking(gestorAtletas);
    }

    @Test
    void testMostrarEstadisticasDesdeConsolaAtletaRegistrado() {
        Judoka atleta = new Judoka("Ignacio", "Essus", "66kg", "2004-05-12");
        gestorAtletas.getListaAtletas().add(atleta);

        String input = "Ignacio\n";
        Scanner scanner = new Scanner(input);

        estadisticasRanking.mostrarEstadisticasDesdeConsola(scanner);
        Judoka encontrado = gestorAtletas.obtenerAtleta("Ignacio");
        assertNotNull(encontrado);
    }

    @Test
    void testMostrarEstadisticasDesdeConsolaAtletaNoRegistrado() {
        String input = "Fantasma\n";
        Scanner scanner = new Scanner(input);
        estadisticasRanking.mostrarEstadisticasDesdeConsola(scanner);
        Judoka noEncontrado = gestorAtletas.obtenerAtleta("Fantasma");
        assertNull(noEncontrado);
    }

    @Test
    void testCalcularRanking() {
        Judoka atleta1 = new Judoka("Ignacio", "Essus", "66kg", "2004-05-12");
        atleta1.aumentarVictoria();
        atleta1.aumentarVictoria();

        Judoka atleta2 = new Judoka("Alonso", "Romero", "66kg", "2004-03-28");
        atleta2.aumentarVictoria();

        Judoka atleta3 = new Judoka("Benjamin", "Beroiza", "73kg", "2003-09-21");

        gestorAtletas.getListaAtletas().add(atleta1);
        gestorAtletas.getListaAtletas().add(atleta2);
        gestorAtletas.getListaAtletas().add(atleta3);

        estadisticasRanking.calcularRanking();

        gestorAtletas.getListaAtletas().sort((a, b) -> Integer.compare(b.getVictorias(), a.getVictorias()));
        assertEquals("Ignacio", gestorAtletas.getListaAtletas().get(0).getNombre());
    }
}
