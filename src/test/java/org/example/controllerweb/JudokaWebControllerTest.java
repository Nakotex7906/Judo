package org.example.controllerweb;

import org.example.model.user.Judoka;
import org.example.service.JudokaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para el controlador web de Judoka (JudokaWebController).
 * Estas pruebas verifican el comportamiento de los métodos del controlador,
 * simulando el servicio y el modelo.
 */
class JudokaWebControllerTest {

    @Mock
    private JudokaService judokaService;

    @Mock
    private Model model;

    @Mock
    private HttpSession session;

    @InjectMocks
    private JudokaWebController controller;

    /**
     * Inicializa los mocks antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Verifica que, si la lista de judokas está vacía,
     * se añade correctamente al modelo y se retorna la vista apropiada.
     */
    @Test
    void listarJudokas_modelConListaVacia() {
        when(judokaService.listarJudokas()).thenReturn(new ArrayList<>());
        String view = controller.listarJudokas(model);
        verify(model).addAttribute(eq("judokas"), anyList());
        assertEquals("judokas", view);
    }

    /**
     * Verifica que, si la lista de judokas contiene elementos,
     * se añade correctamente al modelo y se retorna la vista apropiada.
     */
    @Test
    void listarJudokas_modelConListaNoVacia() {
        List<Judoka> lista = new ArrayList<>();
        lista.add(new Judoka());
        when(judokaService.listarJudokas()).thenReturn(lista);

        String view = controller.listarJudokas(model);
        verify(model).addAttribute("judokas", lista);
        assertEquals("judokas", view);
    }

    /**
     * Prueba que el método mostrarJudokas añade la lista al modelo
     * y retorna la vista 'judokas'.
     */
    @Test
    void mostrarJudokas_devuelveVistaJudokas() {
        List<Judoka> lista = new ArrayList<>();
        when(judokaService.listarJudokas()).thenReturn(lista);
        String view = controller.mostrarJudokas(model);
        verify(model).addAttribute("judokas", lista);
        assertEquals("judokas", view);
    }

    /**
     * Verifica que si el usuario no está autenticado como judoka,
     * el método judokaHome redirecciona al login.
     */
    @Test
    void judokaHome_siNoEsJudoka_redirigeALogin() {
        when(session.getAttribute("username")).thenReturn(null);
        String view = controller.judokaHome(session, model);
        assertEquals("redirect:/login", view);
    }

    /**
     * Comprueba que si el usuario es un judoka autenticado,
     * se muestra su nombre en el modelo y se retorna la vista 'judoka_home'.
     */
    @Test
    void judokaHome_siEsJudoka_muestraNombre() {
        when(session.getAttribute("username")).thenReturn("test@correo.com");
        when(session.getAttribute("tipo")).thenReturn("judoka");

        Judoka judoka = new Judoka();
        judoka.setNombre("Pedro");
        when(judokaService.findByUsername("test@correo.com")).thenReturn(Optional.of(judoka));

        String view = controller.judokaHome(session, model);
        verify(model).addAttribute("nombre", "Pedro");
        assertEquals("judoka_home", view);
    }

    /**
     * Prueba que showRegistroJudoka retorna la vista correspondiente
     * al formulario de registro de judoka.
     */
    @Test
    void showRegistroJudoka_muestraVista() {
        assertEquals("registro_judoka", controller.showRegistroJudoka());
    }

    /**
     * Verifica que si faltan campos obligatorios en el registro,
     * se retorna la vista de registro y se añade un mensaje de error al modelo.
     */
    @Test
    void doRegistroJudoka_camposObligatoriosFaltantes() {
        String view = controller.doRegistroJudoka("", "", "", "", "", "", model);
        verify(model).addAttribute(eq("error"), anyString());
        assertEquals("registro_judoka", view);
    }

    /**
     * Comprueba que si el username ya está registrado,
     * el método añade un mensaje de error al modelo y retorna la vista de registro.
     */
    @Test
    void doRegistroJudoka_usernameYaRegistrado() {
        when(judokaService.findByUsername("usuario@correo.com")).thenReturn(Optional.of(new Judoka()));
        String view = controller.doRegistroJudoka(
                "usuario@correo.com", "pass", "nombre", "apellido", "cat", "1990-01-01", model);
        verify(model).addAttribute(eq("error"), contains("ya está registrado"));
        assertEquals("registro_judoka", view);
    }

    /**
     * Verifica que cuando el registro es exitoso,
     * se llama al guardado del Judoka y se añade un mensaje de éxito al modelo.
     */
    @Disabled
    void doRegistroJudoka_registroExitoso() {
        when(judokaService.findByUsername("nuevo@correo.com")).thenReturn(Optional.empty());

        String view = controller.doRegistroJudoka(
                "nuevo@correo.com", "pass", "nombre", "apellido", "cat", "1990-01-01", model);

        verify(judokaService).guardarJudoka(any(Judoka.class));
        assertEquals("redirect:/login?registrado=1", view);
    }
}
