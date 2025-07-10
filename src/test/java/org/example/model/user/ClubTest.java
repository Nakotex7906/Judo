package org.example.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link Club}.
 * Se validan los métodos de agregar/eliminar judokas y mostrar información.
 */
class ClubTest {

    private Club club;
    private Judoka judoka;

    /**
     * Inicializa un club y un judoka antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        club = new Club();
        club.setNombre("Club Kyoudai");
        club.setSensei("Sensei Diego");
        club.setAnoFundacion("2001");
        club.setDireccion("Av Alemania 1523");
        club.setHorarios("Lunes a Viernes, 18:00 - 20:00");

        judoka = new Judoka(1L, "Benjamín", "Beroiza", "73kg", "2003-09-21");
    }

    /**
     * Verifica que al agregar un judoka al club:
     * - la lista de judokas aumente en uno.
     * - el club del judoka se actualice correctamente.
     */
    @Test
    void testAgregarJudoka() {
        club.agregarJudoka(judoka);
        assertEquals(1, club.getJudokas().size());
        assertEquals(club, judoka.getClub());
    }

    /**
     * Verifica que al eliminar un judoka del club:
     * - la lista de judokas se vacíe.
     * - el club del judoka quede en null.
     */
    @Test
    void testEliminarJudoka() {
        club.agregarJudoka(judoka);
        club.eliminarJudoka(judoka);
        assertEquals(0, club.getJudokas().size());
        assertNull(judoka.getClub());
    }

    /**
     * Verifica que el método {@code mostrarInformacion} incluya
     * correctamente los datos del club y la cantidad de judokas.
     */
    @Test
    void testMostrarInformacion() {
        club.agregarJudoka(judoka);
        String info = club.mostrarInformacion();
        assertTrue(info.contains("Club: Club Kyoudai"));
        assertTrue(info.contains("Sensei: Sensei Diego"));
        assertTrue(info.contains("Año Fundación: 2001"));
        assertTrue(info.contains("Judokas inscritos: 1"));
    }
}
