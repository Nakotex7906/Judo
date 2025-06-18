package org.example.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para PasswordResetTokenJudoka,
 * responsable de gestionar tokens de recuperación de contraseña para judokas.
 */
class PasswordResetTokenJudokaTest {

    private PasswordResetTokenJudoka token;
    private Judoka judoka;

    /**
     * Configuración inicial para cada caso de prueba.
     */
    @BeforeEach
    void setUp() {
        judoka = new Judoka();
        judoka.setNombre("Nakamura");

        token = new PasswordResetTokenJudoka();
        token.setToken("testtoken");
        token.setJudoka(judoka);
        token.setUsed(false);
        token.setExpiryDate(LocalDateTime.now().plusHours(2));
    }

    /**
     * Verifica los getters y setters principales.
     */
    @Test
    void testGettersAndSetters() {
        assertEquals("testtoken", token.getToken());
        assertEquals(judoka, token.getJudoka());
        assertNotNull(token.getExpiryDate());
        assertFalse(token.isUsed());
    }

    /**
     * Verifica que el token NO esté expirado si la fecha de expiración es futura.
     */
    @Test
    void testIsNotExpired() {
        token.setExpiryDate(LocalDateTime.now().plusMinutes(20));
        assertFalse(token.isExpired());
    }

    /**
     * Verifica que el token SÍ esté expirado si la fecha de expiración ya pasó.
     */
    @Test
    void testIsExpired() {
        token.setExpiryDate(LocalDateTime.now().minusMinutes(5));
        assertTrue(token.isExpired());
    }

    /**
     * Valida la actualización correcta de la bandera "used".
     */
    @Test
    void testSetUsed() {
        token.setUsed(true);
        assertTrue(token.isUsed());
        token.setUsed(false);
        assertFalse(token.isUsed());
    }

    /**
     * Prueba la asignación y recuperación del judoka asociado.
     */
    @Test
    void testJudokaAssignment() {
        Judoka nuevoJudoka = new Judoka();
        token.setJudoka(nuevoJudoka);
        assertEquals(nuevoJudoka, token.getJudoka());
    }

    /**
     * Prueba la asignación y lectura de la fecha de expiración.
     */
    @Test
    void testExpiryDateAssignment() {
        LocalDateTime nuevaFecha = LocalDateTime.now().plusDays(2);
        token.setExpiryDate(nuevaFecha);
        assertEquals(nuevaFecha, token.getExpiryDate());
    }
}