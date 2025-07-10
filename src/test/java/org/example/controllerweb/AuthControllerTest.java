package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import org.example.model.competencia.Torneo;
import org.example.model.user.Club;
import org.example.model.user.Judoka;
import org.example.service.ClubService;
import org.example.service.JudokaService;
import org.example.service.RankingService;
import org.example.service.TorneoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para la clase AuthController. Esta clase verifica el comportamiento de los métodos
 * de AuthController, asegurando la resolución correcta de vistas y el manejo de sesiones.
 * Las pruebas de login ya no son necesarias aquí, ya que Spring Security gestiona la autenticación.
 */
class AuthControllerTest {

    private AuthController authController;


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

    @Test
    void testShowLoginDevuelvePaginaLogin() {
        String result = authController.showLogin();
        assertEquals("Model/login", result);
    }

    @Test
    void testIndexCargaTopRankingYTorneos() {
        // Preparar datos de prueba
        List<Judoka> topJudokas = Arrays.asList(
            new Judoka(), new Judoka(), new Judoka()
        );
        List<Torneo> torneos = Arrays.asList(
            new Torneo(), new Torneo()
        );

        // Configurar comportamiento esperado
        when(rankingService.obtenerRankingJudokas()).thenReturn(topJudokas);
        when(torneoService.listarTorneos()).thenReturn(torneos);

        // Ejecutar el método
        String result = authController.index(model);

        // Verificar resultados
        assertEquals("Model/index", result);
        verify(model).addAttribute("top3Ranking", topJudokas);
        verify(model).addAttribute("proximosTorneos", torneos);
    }

    @Test
    void testVerPerfilSinSesionRedirige() {
        when(session.getAttribute("username")).thenReturn(null);

        String result = authController.verPerfil(session, model);

        assertEquals("redirect:/login", result);
    }

    @Test
    void testVerPerfilJudokaRedirige() {
        when(session.getAttribute("username")).thenReturn("testUser");
        when(session.getAttribute("tipo")).thenReturn("judoka");
        when(judokaService.findByUsername("testUser"))
            .thenReturn(Optional.of(new Judoka()));

        String result = authController.verPerfil(session, model);

        verify(session).getAttribute("username");
        verify(session).getAttribute("tipo");
        assertNotNull(result);
    }

    @Test
    void testVerPerfilClubRedirige() {
        when(session.getAttribute("username")).thenReturn("testClub");
        when(session.getAttribute("tipo")).thenReturn("club");
        when(clubService.findByUsername("testClub"))
            .thenReturn(Optional.of(new Club()));

        String result = authController.verPerfil(session, model);

        verify(session).getAttribute("username");
        verify(session).getAttribute("tipo");
        assertNotNull(result);
    }

    @Test
    void testVerPerfilTipoInvalidoRedirige() {
        when(session.getAttribute("username")).thenReturn("testUser");
        when(session.getAttribute("tipo")).thenReturn("invalid");

        String result = authController.verPerfil(session, model);

        verify(session).invalidate();
        assertEquals("redirect:/login?error=user_not_found", result);
    }
}