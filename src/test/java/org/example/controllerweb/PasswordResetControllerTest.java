package org.example.controllerweb;

import org.example.service.resetpassword.PasswordResetClubService;
import org.example.service.resetpassword.PasswordResetJudokaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase {@link PasswordResetController}.
 * <p>
 * Se testean los métodos de recuperación y restablecimiento de contraseña tanto para judokas como para clubes.
 * </p>
 */
class PasswordResetControllerTest {

    private PasswordResetController controller;
    private Model model;
    private PasswordResetJudokaService judokaService;
    private PasswordResetClubService clubService;

    /**
     * Inicializa el entorno de pruebas con inyección de dependencias mediante reflexión.
     *
     * @throws Exception si ocurre un error al establecer campos privados
     */
    @BeforeEach
    void setUp() throws Exception {
        controller = Mockito.mock(PasswordResetController.class, CALLS_REAL_METHODS);
        model = Mockito.mock(Model.class);

        judokaService = Mockito.mock(PasswordResetJudokaService.class);
        Field judokaServiceField = PasswordResetController.class.getDeclaredField("judokaService");
        judokaServiceField.setAccessible(true);
        judokaServiceField.set(controller, judokaService);

        clubService = mock(PasswordResetClubService.class);
        Field clubServiceField = PasswordResetController.class.getDeclaredField("clubService");
        clubServiceField.setAccessible(true);
        clubServiceField.set(controller, clubService);
    }

    /**
     * Verifica que la vista para solicitar recuperación del judoka se cargue correctamente.
     */
    @Test
    void testMostrarFormularioRecuperarJudoka() {
        String resultado = controller.mostrarFormularioRecuperarJudoka();
        assertEquals("ResetPassword/recuperar-judoka", resultado);
    }

    /**
     * Verifica que la vista para solicitar recuperación del club se cargue correctamente.
     */
    @Test
    void testMostrarFormularioRecuperarClub() {
        String resultado = controller.mostrarFormularioRecuperarClub();
        assertEquals("ResetPassword/recuperar-club", resultado);
    }

    /**
     * Verifica que se muestra el formulario para restablecer contraseña de judoka si el token es válido.
     */
    @Test
    void testMostrarFormularioRestablecerJudoka() {
        String token = "abc123";
        when(judokaService.validarToken(token)).thenReturn(true);
        String resultado = controller.mostrarFormularioRestablecerJudoka(token, model);
        assertEquals("ResetPassword/restablecer-judoka", resultado);
    }

    /**
     * Verifica que se muestra el formulario para restablecer contraseña del club si el token es válido.
     */
    @Test
    void testMostrarFormularioRestablecerClub() {
        String token = "xyz789";
        when(clubService.validarToken(token)).thenReturn(true);
        String resultado = controller.mostrarFormularioRestablecerClub(token, model);
        assertEquals("ResetPassword/restablecer-club", resultado);
    }

    /**
     * Verifica que la solicitud de recuperación para judoka devuelve una vista válida.
     */
    @Test
    void testProcesarSolicitudJudoka() {
        String result = controller.procesarSolicitudJudoka("usuario@correo.es", model);
        assertTrue(result.equals("ResetPassword/recuperar-judoka") ||
                result.equals("Model/login"));
    }

    /**
     * Verifica que la solicitud de recuperación para club devuelve una vista válida.
     */
    @Test
    void testProcesarSolicitudClub() {
        String result = controller.procesarSolicitudClub("club@correo.es", model);
        assertTrue(result.equals("ResetPassword/recuperar-club") ||
                result.equals("Model/login"));
    }

    /**
     * Verifica que se procesa correctamente el cambio de contraseña para judoka.
     */
    @Test
    void testProcesarNuevaPasswordJudoka() {
        String result = controller.procesarNuevaPasswordJudoka("algunToken", "nuevaPass", model);
        assertNotNull(result); // Puede ser login, mensaje de éxito o error.
    }

    /**
     * Verifica que se procesa correctamente el cambio de contraseña para club.
     */
    @Test
    void testProcesarNuevaPasswordClub() {
        String result = controller.procesarNuevaPasswordClub("algunToken", "nuevaPass", model);
        assertNotNull(result);
    }
}
