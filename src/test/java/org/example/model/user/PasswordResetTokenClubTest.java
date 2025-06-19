package org.example.model.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase PasswordResetTokenClub,
 * que representa el token de recuperación de contraseña para un club.
 */
class PasswordResetTokenClubTest {

    private PasswordResetTokenClub token;
    private Club club;

    /**
     * Inicializa los objetos necesarios para cada test.
     */
    @BeforeEach
    void setUp() {
        club = new Club();
        club.setNombre("ClubKyoudai");

        token = new PasswordResetTokenClub();
        token.setToken("token123");
        token.setClub(club);
        token.setUsed(false);
        token.setExpiryDate(LocalDateTime.now().plusHours(1));
    }

    /**
     * Verifica el correcto funcionamiento de los getters y setters.
     */
    @Test
    void testGettersAndSetters() {
        assertEquals("token123", token.getToken());
        assertEquals(club, token.getClub());
        assertNotNull(token.getExpiryDate());
        assertFalse(token.isUsed());
    }

    /**
     * Verifica que el token no esté expirado si la fecha de expiración es futura.
     */
    @Test
    void testIsNotExpired() {
        token.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        assertFalse(token.isExpired());
    }

    /**
     * Verifica que el token esté expirado si la fecha de expiración ya pasó.
     */
    @Test
    void testIsExpired() {
        token.setExpiryDate(LocalDateTime.now().minusMinutes(1));
        assertTrue(token.isExpired());
    }

    /**
     * Valida que el método setUsed modifique el estado correctamente.
     */
    @Test
    void testSetUsed() {
        token.setUsed(true);
        assertTrue(token.isUsed());
        token.setUsed(false);
        assertFalse(token.isUsed());
    }

    /**
     * Prueba la asignación y lectura del objeto Club relacionado.
     */
    @Test
    void testClubAssignment() {
        Club newClub = new Club();
        token.setClub(newClub);
        assertEquals(newClub, token.getClub());
    }

    /**
     * Prueba la asignación y lectura de la fecha de expiración.
     */
    @Test
    void testExpiryDateAssignment() {
        LocalDateTime date = LocalDateTime.now().plusDays(5);
        token.setExpiryDate(date);
        assertEquals(date, token.getExpiryDate());
    }
}