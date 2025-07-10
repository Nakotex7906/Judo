package org.example.controllerweb;

import org.example.dto.JudokaRegistroDTO;
import org.example.model.user.Judoka;
import org.example.service.JudokaService;
import org.example.service.RankingService;
import org.junit.jupiter.api.BeforeEach;
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
    private RankingService rankingService;

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
        controller = new JudokaWebController(judokaService,rankingService);
    }

    /**
     * Verifica que, si la lista de judokas está vacía,
     * se añade correctamente al modelo y se retorna la vista apropiada.
     */
    @Test
    void listarJudokas_modelConListaVacia() {
        List<Judoka> listaVacia = new ArrayList<>();
        when(judokaService.listarJudokas()).thenReturn(listaVacia);

        String vista = controller.listarJudokas(model);
        verify(model).addAttribute("judokas", listaVacia);
        assertEquals("judoka/judokas", vista);
    }

    /**
     * Verifica que, si la lista de judokas contiene elementos,
     * se añade correctamente al modelo y se retorna la vista apropiada.
     */
    @Test
    void listarJudokas_modelConListaNoVacia() {
        List<Judoka> judokas = List.of(new Judoka());
        when(judokaService.listarJudokas()).thenReturn(judokas);

        String vista = controller.listarJudokas(model);

        verify(model).addAttribute("judokas", judokas);
        assertEquals("judoka/judokas", vista);

    }

    /**
     * Prueba que el método mostrarJudokas añade la lista al modelo
     * y retorna la vista 'judokas'.
     */
    @Test
    void mostrarJudokas_devuelveVistaJudokas() {
        List<Judoka> judokas = List.of(new Judoka());
        when(judokaService.listarJudokas()).thenReturn(judokas);

        String vista = controller.mostrarJudokas(model);

        verify(model).addAttribute("judokas", judokas);
        assertEquals("judoka/judokas", vista);

    }

    /**
     * Verifica que si el usuario no está autenticado como judoka,
     * el método judokaHome redirecciona al login.
     */
    @Test
    void judokaHome_siNoEsJudoka_redirigeALogin() {
        when(session.getAttribute("username")).thenReturn(null); // No logueado

        String resultado = controller.judokaHome(session, model);

        assertEquals("redirect:/login", resultado);
        verifyNoInteractions(judokaService);

    }

    /**
     * Comprueba que si el usuario es un judoka autenticado,
     * se muestra su nombre en el modelo y se retorna la vista 'judoka_home'.
     */
    @Test
    void judokaHome_siEsJudoka_muestraNombre() {
        when(session.getAttribute("username")).thenReturn("usuario");
        when(session.getAttribute("tipo")).thenReturn("judoka");
        Judoka judoka = new Judoka();
        judoka.setNombre("Pedro");
        when(judokaService.findByUsername("usuario")).thenReturn(Optional.of(judoka));

        String resultado = controller.judokaHome(session, model);

        verify(model).addAttribute("judoka", judoka);
        assertEquals("Judoka/judoka_home", resultado);

    }

    /**
     * Prueba que showRegistroJudoka retorna la vista correspondiente
     * al formulario de registro de judoka.
     */
    @Test
    void showRegistroJudoka_muestraVista() {
        assertEquals("Judoka/registro_judoka", controller.showRegistroJudoka(model));
    }

    /**
     * Verifica que si faltan campos obligatorios en el registro,
     * se retorna la vista de registro y se añade un mensaje de error al modelo.
     */
    @Test
    void doRegistroJudoka_camposObligatoriosFaltantes() {
        JudokaRegistroDTO dto = new JudokaRegistroDTO();
        dto.setUsername(""); // campo vacío
        dto.setPassword("123");
        dto.setNombre("nombre");
        dto.setApellido("apellido");
        dto.setCategoria("cat");
        dto.setFechaNacimiento("2000-01-01");

        String vista = controller.doRegistroJudoka(dto, model);

        verify(model).addAttribute(eq("error"), anyString());
        assertEquals("Judoka/registro_judoka", vista);
    }

    /**
     * Comprueba que si el username ya está registrado,
     * el método añade un mensaje de error al modelo y retorna la vista de registro.
     */
    @Test
    void doRegistroJudoka_usernameYaRegistrado() {
        JudokaRegistroDTO dto = new JudokaRegistroDTO();
        dto.setUsername("usuario");
        dto.setPassword("pass");
        dto.setNombre("nombre");
        dto.setApellido("apellido");
        dto.setCategoria("cat");
        dto.setFechaNacimiento("2000-01-01");

        when(judokaService.findByUsername("usuario")).thenReturn(Optional.of(new Judoka()));

        String vista = controller.doRegistroJudoka(dto, model);

        verify(model).addAttribute("error", "El correo ya está registrado.");
        assertEquals("Judoka/registro_judoka", vista);

    }

    /**
     * Verifica que cuando el registro es exitoso,
     * se llama al guardado del Judoka y se añade un mensaje de éxito al modelo.
     */


    // No pasa

    @Test
    void doRegistroJudoka_registroExitoso() {
        JudokaRegistroDTO dto = new JudokaRegistroDTO();
        dto.setUsername("usuario");
        dto.setPassword("pass");
        dto.setNombre("nombre");
        dto.setApellido("apellido");
        dto.setCategoria("cat");
        dto.setFechaNacimiento("2000-01-01");

        when(judokaService.findByUsername("usuario")).thenReturn(Optional.empty());

        String vista = controller.doRegistroJudoka(dto, model);

        verify(judokaService).guardarJudoka(any(Judoka.class));
        verify(model).addAttribute(eq("success"), contains("Judoka registrado correctamente"));
        assertEquals("redirect:/login", vista);

    }
}
