package org.example.service.auth;

import org.example.service.JudokaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para la clase {@link JudokaAuthStrategy}, la cual implementa
 * una estrategia de autenticación para usuarios de tipo "judoka", delegando la validación
 * al servicio {@link JudokaService}.
 *
 * <p>Se valida:</p>
 * <ul>
 *     <li>Que las credenciales sean correctamente verificadas por JudokaService.</li>
 *     <li>Que el tipo de estrategia devuelto sea el esperado.</li>
 * </ul>
 *
 * @author
 */
class JudokaAuthStrategyTest {

    private JudokaService judokaServiceMock;
    private JudokaAuthStrategy estrategia;

    /**
     * Configura el entorno de pruebas inicializando un mock de JudokaService
     * y creando una instancia de la estrategia.
     */
    @BeforeEach
    void setUp() {
        judokaServiceMock = mock(JudokaService.class);
        estrategia = new JudokaAuthStrategy(judokaServiceMock);
    }

    /**
     * Verifica que se retorne verdadero si el servicio valida las credenciales correctamente.
     */
    @Test
    void autenticar_conCredencialesValidas_devuelveVerdadero() {
        when(judokaServiceMock.validarContrasena("judokauser", "abc123")).thenReturn(true);

        boolean resultado = estrategia.authenticate("judokauser", "abc123");

        assertTrue(resultado);
        verify(judokaServiceMock).validarContrasena("judokauser", "abc123");
    }

    /**
     * Verifica que se retorne falso si las credenciales no son válidas.
     */
    @Test
    void autenticar_conCredencialesInvalidas_devuelveFalso() {
        when(judokaServiceMock.validarContrasena("judokauser", "malaClave")).thenReturn(false);

        boolean resultado = estrategia.authenticate("judokauser", "malaClave");

        assertFalse(resultado);
        verify(judokaServiceMock).validarContrasena("judokauser", "malaClave");
    }

    /**
     * Verifica que el tipo devuelto sea "judoka".
     */
    @Test
    void getTipo_devuelveJudoka() {
        assertEquals("judoka", estrategia.getTipo());
    }
}
