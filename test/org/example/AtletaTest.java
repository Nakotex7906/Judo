package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AtletaTest {

    private Atleta atleta;

    @BeforeEach
    void setUp() {
        atleta = new Atleta("Ignacio", "Essus", "66kg", "2004-05-12");
    }

    @Test
    void testAumentarVictoria() {
        atleta.aumentarVictoria();
        assertEquals(1, atleta.getVictorias());
    }

    @Test
    void testAumentarDerrota() {
        atleta.aumentarDerrota();
        assertEquals(1, atleta.getDerrotas());
    }

    @Test
    void testMostrarInformacion() {
        atleta.aumentarVictoria();
        String info = atleta.mostrarInformacion();
        assertTrue(info.contains("Nombre: Ignacio Essus"));
        assertTrue(info.contains("Categoria: 66kg"));
        assertTrue(info.contains("Victorias: 1"));
        assertTrue(info.contains("% Victorias: 100.00"));
    }
}
