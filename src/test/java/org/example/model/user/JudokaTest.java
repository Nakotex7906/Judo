package org.example.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link Judoka}.
 * Se testean los métodos relacionados con el conteo de combates y visualización de estadísticas.
 */
class JudokaTest {

    private Judoka atleta;

    /**
     * Inicializa un judoka con valores base antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        atleta = new Judoka(2L, "Ignacio", "Essus", "66kg", "2004-05-12");
    }

    /**
     * Verifica que el método {@code aumentarVictoria()} incremente en 1
     * la cantidad de victorias del judoka.
     */
    @Test
    void testAumentarVictoria() {
        atleta.aumentarVictoria();
        assertEquals(1, atleta.getVictorias());
    }

    /**
     * Verifica que el método {@code aumentarDerrota()} incremente en 1
     * la cantidad de derrotas del judoka.
     */
    @Test
    void testAumentarDerrota() {
        atleta.aumentarDerrota();
        assertEquals(1, atleta.getDerrotas());
    }

    /**
     * Verifica que el método {@code mostrarInformacion()} incluya los campos clave
     * como nombre completo, categoría, número de victorias y porcentaje correcto.
     */
    @Test
    void testMostrarInformacion() {
        atleta.aumentarVictoria(); // 1 victoria de 1 combate
        String info = atleta.mostrarInformacion();
        assertTrue(info.contains("Nombre: Ignacio Essus"));
        assertTrue(info.contains("Categoria: 66kg"));
        assertTrue(info.contains("Victorias: 1"));
        assertTrue(info.contains("% Victorias: 100.00"));
    }
}
