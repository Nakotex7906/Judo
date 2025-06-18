package org.example.controllerweb;

import org.example.service.resetPassword.PasswordResetClubService;
import org.example.service.resetPassword.PasswordResetJudokaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.ui.Model;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias base para el controlador PasswordResetController.
 */
class PasswordResetControllerTest {

    private PasswordResetController controller;
    private Model model;
    private PasswordResetJudokaService judokaService;
    private PasswordResetClubService clubService;


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

    @Test
    void testMostrarFormularioRecuperarJudoka() {
        String resultado = controller.mostrarFormularioRecuperarJudoka();
        assertEquals("ResetPassword/recuperar-judoka", resultado);
    }

    @Test
    void testMostrarFormularioRecuperarClub() {
        String resultado = controller.mostrarFormularioRecuperarClub();
        assertEquals("ResetPassword/recuperar-club", resultado);
    }

    @Test
    void testMostrarFormularioRestablecerJudoka() {
        String token = "abc123";

        // simula un token válido
        when(judokaService.validarToken(token)).thenReturn(true);

        String resultado = controller.mostrarFormularioRestablecerJudoka(token, model);
        assertEquals("ResetPassword/restablecer-judoka", resultado);

    }

    @Test
    void testMostrarFormularioRestablecerClub() {
        String token = "xyz789";
        when(clubService.validarToken(token)).thenReturn(true);
        String resultado = controller.mostrarFormularioRestablecerClub(token, model);
        assertEquals("ResetPassword/restablecer-club", resultado);

    }

    @Test
    void testProcesarSolicitudJudoka() {
        String result = controller.procesarSolicitudJudoka("usuario@correo.es", model);
        assertTrue(result.equals("ResetPassword/recuperar-judoka") ||
                result.equals("Model/login"));
    }

    @Test
    void testProcesarSolicitudClub() {
        String result = controller.procesarSolicitudClub("club@correo.es", model);
        assertTrue(result.equals("ResetPassword/recuperar-club") ||
                result.equals("Model/login"));
    }

    @Test
    void testProcesarNuevaPasswordJudoka() {
        String result = controller.procesarNuevaPasswordJudoka("algunToken", "nuevaPass", model);
        assertNotNull(result); // Según flujo, puede ser login, mensaje de error o exito en la vista.
    }

    @Test
    void testProcesarNuevaPasswordClub() {
        String result = controller.procesarNuevaPasswordClub("algunToken", "nuevaPass", model);
        assertNotNull(result);
    }
}