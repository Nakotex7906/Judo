package org.example.model.competencia;

import org.example.model.user.Judoka;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CombateTest {

    private Judoka judoka1;
    private Judoka judoka2;
    private Torneo torneo;
    private Combate combate;

    @BeforeEach
    void setUp() {
        judoka1 = new Judoka("Benjamin", "Beroiza", "73kg", "2003-09-21");
        judoka2 = new Judoka("Ignacio", "Essus", "66kg", "2004-05-12");
        torneo = new Torneo();
        torneo.setNombre("Torneo Nacional");

        combate = new Combate(judoka1, judoka2, torneo);
    }

    @Test
    void testCrearCombateSinGanador() {
        assertEquals(judoka1, combate.getJudokaBlanco());
        assertEquals(judoka2, combate.getJudokaAzul());
        assertEquals(torneo, combate.getTorneo());
        assertNull(combate.getGanador());
        assertEquals(0, combate.getDuracionSegundos());
    }

    @Test
    void testRegistrarGanadorValido() {
        combate.registrarGanador(judoka1);
        assertEquals(judoka1, combate.getGanador());
        assertEquals(1, judoka1.getVictorias());
        assertEquals(1, judoka2.getDerrotas());
    }

    @Test
    void testRegistrarGanadorInvalidoNoCambiaEstado() {
        Judoka externo = new Judoka("Alonso", "Romero", "66kg", "2004-03-28");
        combate.registrarGanador(externo);
        assertNull(combate.getGanador());
        assertEquals(0, judoka1.getVictorias());
        assertEquals(0, judoka2.getDerrotas());
    }

    @Test
    void testGenerarCombatesImpar_RoundRobin() {
        //Se simula un combate categoria absoluta(se pueden mesclar las categorias)
        Judoka a = new Judoka("Ignacio", "Essus", "66kg", "2004-05-12");
        Judoka b = new Judoka("Benjamin", "Beroiza", "73kg", "2003-09-21");
        Judoka c = new Judoka("Alonso", "Romero", "66kg", "2004-03-28");

        List<Judoka> participantes = Arrays.asList(a, b, c);
        List<Combate> combates = Combate.generarCombates(participantes, torneo);

        assertEquals(3, combates.size(), "Debe haber 3 combates para 3 participantes (round-robin)");

        boolean aParticipa = combates.stream().anyMatch(cbt ->
                cbt.getJudokaBlanco().equals(a) || cbt.getJudokaAzul().equals(a));
        boolean bParticipa = combates.stream().anyMatch(cbt ->
                cbt.getJudokaBlanco().equals(b) || cbt.getJudokaAzul().equals(b));
        boolean cParticipa = combates.stream().anyMatch(cbt ->
                cbt.getJudokaBlanco().equals(c) || cbt.getJudokaAzul().equals(c));

        assertTrue(aParticipa, "Judoka A debe participar");
        assertTrue(bParticipa, "Judoka B debe participar");
        assertTrue(cParticipa, "Judoka C debe participar");
    }

    @Test
    void testMostrarInformacionSinGanador() {
        String info = combate.mostrarInformacion();
        assertTrue(info.contains("Sin ganador"));
        assertTrue(info.contains("Benjamin"));
        assertTrue(info.contains("Ignacio"));
    }

    @Test
    void testMostrarInformacionConGanador() {
        combate.registrarGanador(judoka2);
        String info = combate.mostrarInformacion();
        assertTrue(info.contains("Ganador: Ignacio"));
    }
}
