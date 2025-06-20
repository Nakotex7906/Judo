package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import org.example.service.auth.AuthenticationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase AuthController. Esta clase verifica el comportamiento de los métodos
 * de AuthController, asegurando la resolución correcta de vistas, el manejo de sesiones, la validación de errores
 * y las interacciones con los servicios.
 */
class AuthControllerTest {

    private AuthController authController;

    @Mock
    private AuthenticationService authenticationService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    /**
     * Configura el entorno de pruebas inicializando los mocks de las dependencias
     * (JudokaService, ClubService, etc.) e instanciando la clase AuthController.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authController = new AuthController(authenticationService);
    }

    /**
     * Verifica que el endpoint raíz ("/") redirija correctamente a la página de inicio de index ("/index").
     */
    @Test
    void testRootDireccionaAlIndex() {
        String result = authController.root();
        assertEquals("redirect:/index", result);
    }

    /**
     * Prueba el comportamiento de la página de inicio de sesión cuando el usuario ya está autenticado.
     * El método redirige a la página de inicio correspondiente según el tipo de usuario (ej., "judoka").
     */
    @Test
    void testMuestraLoginCuandoElUsuarioEstaAutenticado() {
        when(session.getAttribute("username")).thenReturn("john_doe");
        when(session.getAttribute("tipo")).thenReturn("judoka");

        String result = authController.showLogin(session);
        assertEquals("redirect:/judoka/home", result);
    }

    /**
     * Verifica el comportamiento del proceso de inicio de sesión cuando el nombre de usuario está vacío.
     * Debe agregar un mensaje de error al modelo y devolver la vista "login".
     */
    @Test
    void testDoLoginCuandoElCampoUsernameEstaVacio() {
        String result = authController.doLogin("", "password", "judoka", model, session);

        verify(model).addAttribute("error", "Usuario vacío");
        assertEquals("Model/login", result);
    }

    /**
     * Verifica el comportamiento del proceso de inicio de sesión cuando se proporciona un tipo de usuario inválido.
     * Debe agregar un mensaje de error al modelo y devolver la vista "login".
     */
    @Test
    void testDoLoginConTipoDeUsuarioInvalido() {
        String result = authController.doLogin("john_doe", "password", "invalidType", model, session);

        verify(model).addAttribute("error", "Tipo inválido");
        assertEquals("Model/login", result);
    }

    /**
     * Prueba un inicio de sesión exitoso para un usuario de tipo "judoka" con credenciales válidas.
     * Debe guardar los datos del usuario en la sesión y redirigir a la página de inicio del judoka.
     */
    @Test
    void testDoLoginConCredencialesValidasJudoka() {
        when(authenticationService.tipoValido("judoka")).thenReturn(true);
        when(authenticationService.authenticate("judoka", "john_doe", "password")).thenReturn(true);

        String result = authController.doLogin("john_doe", "password", "judoka", model, session);

        verify(session).setAttribute("username", "john_doe");
        verify(session).setAttribute("tipo", "judoka");
        assertEquals("redirect:/judoka/home", result);

    }

    /**
     * Prueba el proceso de inicio de sesión con credenciales incorrectas para un usuario de tipo "judoka".
     * Debe agregar un mensaje de error al modelo y devolver la vista "login".
     */
    @Test
    void testDoLoginConCredencialesInvalidasJudoka() {
        when(authenticationService.tipoValido("judoka")).thenReturn(true);
        when(authenticationService.authenticate("judoka", "john_doe", "password")).thenReturn(false);

        String result = authController.doLogin("john_doe", "password", "judoka", model, session);

        verify(model).addAttribute("error", "Usuario o contraseña incorrectos");
        assertEquals("Model/login", result);
    }

    /**
     * Verifica que el método de cierre de sesión invalido la sesión actual
     * y redirija al usuario a la página de inicio de sesión.
     */
    @Test
    void testLogoutinvalido() {
        String result = authController.logout(session);

        verify(session).invalidate();
        assertEquals("redirect:/login", result);
    }

    /**
     * Verifica que la página "registro" se resuelva y devuelva correctamente la vista esperada.
     */
    @Test
    void testShowRegistroDevuelveRegistro() {
        String result = authController.showRegistro();
        assertEquals("Model/registro", result);
    }
}