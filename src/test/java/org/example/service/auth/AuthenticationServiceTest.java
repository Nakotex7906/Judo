package org.example.service.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Clase de pruebas unitarias para {@link AuthenticationService}.
 * Valida el comportamiento del servicio de autenticación utilizando estrategias simuladas.
 *
 * <p>Se verifica:</p>
 * <ul>
 *     <li>Autenticación exitosa con credenciales válidas.</li>
 *     <li>Fallo de autenticación por tipo desconocido o credenciales incorrectas.</li>
 *     <li>Validación del tipo de usuario soportado.</li>
 * </ul>
 *
 * @author
 */
class AuthenticationServiceTest {

    private AuthenticationService servicioAutenticacion;
    private AuthenticationStrategy estrategiaClub;
    private AuthenticationStrategy estrategiaJudoka;

    /**
     * Configura el entorno de pruebas creando mocks para las estrategias y
     * preparando el servicio de autenticación con dos tipos: Club y Judoka.
     */
    @BeforeEach
    void configurar() {
        estrategiaClub = mock(AuthenticationStrategy.class);
        estrategiaJudoka = mock(AuthenticationStrategy.class);

        when(estrategiaClub.getTipo()).thenReturn("Club");
        when(estrategiaJudoka.getTipo()).thenReturn("Judoka");

        servicioAutenticacion = new AuthenticationService(List.of(estrategiaClub, estrategiaJudoka));
    }

    /**
     * Verifica que se autentique correctamente un usuario de tipo "Club" con credenciales válidas.
     */
    @Test
    void autenticarClub_conCredencialesValidas_devuelveVerdadero() {
        when(estrategiaClub.authenticate("usuario", "clave")).thenReturn(true);

        boolean resultado = servicioAutenticacion.authenticate("Club", "usuario", "clave");

        assertTrue(resultado);
        verify(estrategiaClub).authenticate("usuario", "clave");
    }

    /**
     * Verifica que al intentar autenticar con un tipo no soportado, se devuelva {@code false}.
     */
    @Test
    void autenticarTipoDesconocido_devuelveFalso() {
        boolean resultado = servicioAutenticacion.authenticate("Desconocido", "usuario", "clave");

        assertFalse(resultado);
    }

    /**
     * Verifica que un usuario de tipo "Judoka" no sea autenticado si entrega credenciales inválidas.
     */
    @Test
    void autenticarJudoka_conClaveIncorrecta_devuelveFalso() {
        when(estrategiaJudoka.authenticate("judoka", "malaClave")).thenReturn(false);

        boolean resultado = servicioAutenticacion.authenticate("Judoka", "judoka", "malaClave");

        assertFalse(resultado);
    }

    /**
     * Verifica que se reconozcan correctamente los tipos de usuario registrados como válidos.
     */
    @Test
    void tipoValido_conTipoRegistrado_devuelveVerdadero() {
        assertTrue(servicioAutenticacion.tipoValido("Club"));
        assertTrue(servicioAutenticacion.tipoValido("Judoka"));
    }

    /**
     * Verifica que un tipo de usuario no registrado sea marcado como no válido.
     */
    @Test
    void tipoValido_conTipoNoRegistrado_devuelveFalso() {
        assertFalse(servicioAutenticacion.tipoValido("Administrador"));
    }
}
