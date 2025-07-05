package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import org.example.service.ClubService;
import org.example.service.JudokaService;
import org.example.service.RankingService;
import org.example.service.TorneoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase AuthController. Esta clase verifica el comportamiento de los métodos
 * de AuthController, asegurando la resolución correcta de vistas y el manejo de sesiones.
 * Las pruebas de login ya no son necesarias aquí, ya que Spring Security gestiona la autenticación.
 */
class AuthControllerTest {

    private AuthController authController;

    // Corregido: Mocks para las nuevas dependencias del controlador
    @Mock
    private JudokaService judokaService;

    @Mock
    private ClubService clubService;

    @Mock
    private TorneoService torneoService;

    @Mock
    private RankingService rankingService;


    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    /**
     * Configura el entorno de pruebas inicializando los mocks de las dependencias
     * e instanciando la clase AuthController.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Corregido: Se instancia el controlador con sus dependencias actuales.
        authController = new AuthController(judokaService, clubService, torneoService, rankingService);

    }

    /**
     * Verifica que el endpoint raíz ("/") redirija correctamente a la página de inicio de index ("/index").
     */
    @Test
    void testRootDireccionaAlIndex() {
        String result = authController.root();
        assertEquals("redirect:/index", result);
    }

    /*
     * Eliminado: El test para `showLogin` con sesión ya no es aplicable.
     * La nueva lógica de `showLogin()` es simplemente mostrar la vista.
     * La redirección si el usuario ya está autenticado es gestionada por otros componentes como el `CustomAuthenticationSuccessHandler`.
     */

    /*
     * Eliminado: Todos los tests para `doLogin` han sido removidos.
     * El método `doLogin` ya no existe en `AuthController`, ya que Spring Security
     * intercepta la petición POST a /login y maneja la autenticación.
     */

    /**
     * Verifica que el método de cierre de sesión invalide la sesión actual
     * y redirija al usuario a la página de inicio de sesión.
     * NOTA: Este test asume que tienes un método logout en tu controlador.
     * Si el logout es manejado 100% por Spring Security, este test podría ser eliminado.
     */
    @Test
    void testLogoutInvalidaSesion() {
        // Suponiendo que el método logout existe y tiene esta firma: public String logout(HttpSession session)
        String result = authController.logout(session);

        verify(session).invalidate();
        assertEquals("redirect:/login?logout", result);
    }

    /**
     * Verifica que la página "registro" se resuelva y devuelva correctamente la vista esperada.
     * NOTA: Este test asume que tienes un método showRegistro en tu controlador.
     */
    @Test
    void testShowRegistroDevuelveRegistro() {
        // Suponiendo que el método existe y tiene esta firma: public String showRegistro()
        String result = authController.showRegistro();
        assertEquals("Model/registro", result);
    }
}