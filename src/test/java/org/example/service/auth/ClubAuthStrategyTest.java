package org.example.service.auth;

import org.example.service.ClubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link ClubAuthStrategy}, encargada de autenticar usuarios de tipo "club"
 * utilizando el servicio {@link ClubService}.
 *
 * <p>Se valida:</p>
 * <ul>
 *     <li>Que delegue correctamente en el servicio ClubService.</li>
 *     <li>Que retorne el tipo correspondiente.</li>
 * </ul>
 *
 * @author
 */
class ClubAuthStrategyTest {

    private ClubService clubServiceMock;
    private ClubAuthStrategy estrategia;

    /**
     * Inicializa el mock de ClubService y crea una instancia de la estrategia antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        clubServiceMock = mock(ClubService.class);
        estrategia = new ClubAuthStrategy(clubServiceMock);
    }

    /**
     * Verifica que la autenticación retorne verdadero cuando ClubService valida las credenciales como correctas.
     */
    @Test
    void autenticar_conCredencialesValidas_devuelveVerdadero() {
        when(clubServiceMock.validarContrasena("clubuser", "1234")).thenReturn(true);

        boolean resultado = estrategia.authenticate("clubuser", "1234");

        assertTrue(resultado);
        verify(clubServiceMock).validarContrasena("clubuser", "1234");
    }

    /**
     * Verifica que la autenticación retorne falso cuando las credenciales no son válidas.
     */
    @Test
    void autenticar_conCredencialesInvalidas_devuelveFalso() {
        when(clubServiceMock.validarContrasena("clubuser", "incorrecta")).thenReturn(false);

        boolean resultado = estrategia.authenticate("clubuser", "incorrecta");

        assertFalse(resultado);
        verify(clubServiceMock).validarContrasena("clubuser", "incorrecta");
    }

    /**
     * Verifica que el método getTipo devuelva correctamente el tipo "club".
     */
    @Test
    void getTipo_devuelveClub() {
        assertEquals("club", estrategia.getTipo());
    }
}
