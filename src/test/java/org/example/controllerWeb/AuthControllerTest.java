package org.example.controllerWeb;

import jakarta.servlet.http.HttpSession;
import org.example.service.ClubService;
import org.example.service.JudokaService;
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
    private JudokaService judokaService;

    @Mock
    private ClubService clubService;

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
        authController = new AuthController(judokaService, clubService);
    }

    /**
     * Verifica que el endpoint raíz ("/") redirija correctamente a la página de inicio de sesión ("/login").
     */
    @Test
    void testRootRedirectsToLogin() {
        String result = authController.root();
        assertEquals("redirect:/login", result);
    }

    /**
     * Prueba el comportamiento de la página de inicio de sesión cuando el usuario ya está autenticado.
     * El método redirige a la página de inicio correspondiente según el tipo de usuario (ej., "judoka").
     */
    @Test
    void testShowLoginWhenUserIsLogged() {
        when(session.getAttribute("username")).thenReturn("john_doe");
        when(session.getAttribute("tipo")).thenReturn("judoka");

        String result = authController.showLogin(session);
        assertEquals("redirect:/judoka/home", result);
    }

    /**
     * Prueba el comportamiento de la página de inicio de sesión para un usuario invitado
     * (sin una sesión activa). Debe devolver la vista "login".
     */
    @Test
    void testShowLoginForGuestUser() {
        when(session.getAttribute("username")).thenReturn(null);

        String result = authController.showLogin(session);
        assertEquals("login", result);
    }

    /**
     * Verifica el comportamiento del proceso de inicio de sesión cuando el nombre de usuario está vacío.
     * Debe agregar un mensaje de error al modelo y devolver la vista "login".
     */
    @Test
    void testDoLoginWithEmptyUsername() {
        String result = authController.doLogin("", "password", "judoka", model, session);

        verify(model).addAttribute("error", "Usuario vacío");
        assertEquals("login", result);
    }

    /**
     * Verifica el comportamiento del proceso de inicio de sesión cuando se proporciona un tipo de usuario inválido.
     * Debe agregar un mensaje de error al modelo y devolver la vista "login".
     */
    @Test
    void testDoLoginWithInvalidType() {
        String result = authController.doLogin("john_doe", "password", "invalidType", model, session);

        verify(model).addAttribute("error", "Tipo inválido");
        assertEquals("login", result);
    }

    /**
     * Prueba un inicio de sesión exitoso para un usuario de tipo "judoka" con credenciales válidas.
     * Debe guardar los datos del usuario en la sesión y redirigir a la página de inicio del judoka.
     */
    @Test
    void testDoLoginWithJudokaValidCredentials() {
        when(judokaService.validarContrasena("john_doe", "password")).thenReturn(true);

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
    void testDoLoginWithIncorrectCredentials() {
        when(judokaService.validarContrasena("john_doe", "password")).thenReturn(false);

        String result = authController.doLogin("john_doe", "password", "judoka", model, session);

        verify(model).addAttribute("error", "Usuario o contraseña incorrectos");
        assertEquals("login", result);
    }

    /**
     * Verifica que el método de cierre de sesión invalide la sesión actual
     * y redirija al usuario a la página de inicio de sesión.
     */
    @Test
    void testLogoutInvalidatesSession() {
        String result = authController.logout(session);

        verify(session).invalidate();
        assertEquals("redirect:/login", result);
    }

    /**
     * Verifica que la página "registro" se resuelva y devuelva correctamente la vista esperada.
     */
    @Test
    void testShowRegistroReturnsCorrectView() {
        String result = authController.showRegistro();
        assertEquals("registro", result);
    }
}
