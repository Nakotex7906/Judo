package org.example;

import org.example.model.judoka.Judoka;
import org.example.model.judoka.GestionJudokas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

 class GestionJudokasTest {

    private GestionJudokas gestion;
    private Judoka atleta1;

    @BeforeEach
     void setUp() {
        gestion = new GestionJudokas();
        atleta1 = new Judoka("Carlos", "Pérez", "Senior", "01-01-1990");
        gestion.getListaAtletas().add(atleta1);
    }

    @Test
     void testObtenerAtletaExistente() {
        Judoka atleta = gestion.obtenerAtleta("Carlos");
        assertNotNull(atleta);
        assertEquals("Carlos", atleta.getNombre());
    }

    @Test
     void testObtenerAtletaInexistente() {
        Judoka atleta = gestion.obtenerAtleta("Ana");
        assertNull(atleta);
    }

    @Test
     void testAgregarAtletaNuevo() {
        Judoka nuevo = new Judoka("Ana", "Gomez", "Junior", "15-05-2000");
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
     void testGuardarCSV() throws IOException {
        String ruta = "test_atletas.csv";
        gestion.guardarAtletasCSV(ruta);
        File archivo = new File(ruta);
        assertTrue(archivo.exists());
        archivo.delete(); // Limpiar el archivo generado después del test
    }
}
