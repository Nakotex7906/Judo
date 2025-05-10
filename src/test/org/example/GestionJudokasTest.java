package org.example;

import org.example.model.gestor.Judoka;
import org.example.model.gestor.GestionJudokas;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

 class GestionJudokasTest {

    private GestionJudokas gestion;
    private Judoka judoka;

    @BeforeEach
     void setUp() {
        gestion = new GestionJudokas();
        judoka = new Judoka("Carlos", "Pérez", "Senior", "01-01-1990");
        gestion.getListaJudokas().add(judoka);
    }

    @Test
     void testObtenerAtletaExistente() {
        Judoka atleta = gestion.obtenerJudoka("Carlos");
        assertNotNull(atleta);
        assertEquals("Carlos", atleta.getNombre());
    }

    @Test
     void testObtenerAtletaInexistente() {
        Judoka atleta = gestion.obtenerJudoka("Ana");
        assertNull(atleta);
    }

    @Test
     void testAgregarAtletaNuevo() {
        Judoka nuevo = new Judoka("Ana", "Gomez", "Junior", "15-05-2000");
        assertNull(gestion.obtenerJudoka("Ana"));
        gestion.getListaJudokas().add(nuevo);
        assertNotNull(gestion.obtenerJudoka("Ana"));
    }

    @Test
     void testRegistrarVictoria() {
        int victoriasAntes = judoka.getVictorias();
        judoka.aumentarVictoria();
        assertEquals(victoriasAntes + 1, judoka.getVictorias());
    }

    @Test
     void testRegistrarDerrota() {
        int derrotasAntes = judoka.getDerrotas();
        judoka.aumentarDerrota();
        assertEquals(derrotasAntes + 1, judoka.getDerrotas());
    }

    @Test
     void testRegistrarEmpate() {
        int empatesAntes = judoka.getEmpates();
        judoka.aumentarEmpate();
        assertEquals(empatesAntes + 1, judoka.getEmpates());
    }

    @Test
     void testGuardarCSV() throws IOException {
        String ruta = "test_atletas.csv";
        gestion.guardarJudokasCSV(ruta);
        File archivo = new File(ruta);
        assertTrue(archivo.exists());
        archivo.delete(); // Limpiar el archivo generado después del test
    }
}
