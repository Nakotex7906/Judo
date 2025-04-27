package org.example;

import org.example.Atleta.Atleta;
import org.example.Atleta.GestionAtletas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

 class GestionAtletasTest {

    private GestionAtletas gestion;
    private Atleta atleta1;

    @BeforeEach
     void setUp() {
        gestion = new GestionAtletas();
        atleta1 = new Atleta("Carlos", "Pérez", "Senior", "01-01-1990");
        gestion.getListaAtletas().add(atleta1);
    }

    @Test
     void testObtenerAtletaExistente() {
        Atleta atleta = gestion.obtenerAtleta("Carlos");
        assertNotNull(atleta);
        assertEquals("Carlos", atleta.getNombre());
    }

    @Test
     void testObtenerAtletaInexistente() {
        Atleta atleta = gestion.obtenerAtleta("Ana");
        assertNull(atleta);
    }

    @Test
     void testAgregarAtletaNuevo() {
        Atleta nuevo = new Atleta("Ana", "Gomez", "Junior", "15-05-2000");
        assertNull(gestion.obtenerAtleta("Ana"));
        gestion.getListaAtletas().add(nuevo);
        assertNotNull(gestion.obtenerAtleta("Ana"));
    }

    @Test
     void testRegistrarVictoria() {
        int victoriasAntes = atleta1.getVictorias();
        atleta1.aumentarVictoria();
        assertEquals(victoriasAntes + 1, atleta1.getVictorias());
    }

    @Test
     void testRegistrarDerrota() {
        int derrotasAntes = atleta1.getDerrotas();
        atleta1.aumentarDerrota();
        assertEquals(derrotasAntes + 1, atleta1.getDerrotas());
    }

    @Test
     void testRegistrarEmpate() {
        int empatesAntes = atleta1.getEmpates();
        atleta1.aumentarEmpate();
        assertEquals(empatesAntes + 1, atleta1.getEmpates());
    }

    @Test
    void testGuardarCSV() {
        String ruta = "test_atletas.csv";
        gestion.guardarAtletasCSV(ruta);
        File archivo = new File(ruta);
        assertTrue(archivo.exists());
        archivo.delete(); // Limpiar el archivo generado después del test
    }
}
