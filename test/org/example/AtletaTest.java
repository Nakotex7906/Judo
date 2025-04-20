package org.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AtletaTest {

    Atleta atleta;

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
        assertTrue(info.contains("Nombre: Juan Pérez"));
        assertTrue(info.contains("Categoria: Senior"));
        assertTrue(info.contains("Victorias: 1"));
        assertTrue(info.contains("% Victorias: 100.00"));
    }
}