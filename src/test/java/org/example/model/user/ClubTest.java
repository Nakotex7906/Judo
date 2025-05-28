package org.example.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ClubTest {

    private Club club;
    private Judoka judoka;

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

    @Test
    void testAgregarJudoka() {
        club.agregarJudoka(judoka);
        assertEquals(1, club.getJudokas().size());
        assertEquals(club, judoka.getClub());
    }

    @Test
    void testEliminarJudoka() {
        club.agregarJudoka(judoka);
        club.eliminarJudoka(judoka);
        assertEquals(0, club.getJudokas().size());
        assertNull(judoka.getClub());
    }

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
