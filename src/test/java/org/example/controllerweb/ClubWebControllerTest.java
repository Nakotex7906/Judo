package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import org.example.dto.ClubRegistroDTO;
import org.example.model.user.Club;
import org.example.service.ClubService;
import org.example.service.JudokaService; // Importar el servicio que falta
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Sort;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClubWebControllerTest {

    private ClubWebController clubWebController;

    @Mock
    private ClubService clubService;

    // Añadido: Mock para el servicio de Judoka
    @Mock
    private JudokaService judokaService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Corregido: Pasar ambos mocks al constructor
        clubWebController = new ClubWebController(clubService, judokaService);
    }

    /**
     * Prueba que el método clubHome redirija al inicio de sesión si no es un club válido.
     */
    @Test
    void testClubHomeDireccionaAlLogin_siNoEsValidoElClub() {
        when(session.getAttribute("username")).thenReturn(null);
        when(session.getAttribute("tipo")).thenReturn(null);

        String result = clubWebController.clubHome(session, model);

        assertEquals("redirect:/login", result);
        verify(model, never()).addAttribute(anyString(), any());
    }

    /**
     * Prueba que el método clubHome muestra los datos del club correctamente cuando
     * un club válido está logueado.
     */
    @Test
    void testClubHomeMuestraElNombreDelClubParaUnLoginValido() {
        when(session.getAttribute("username")).thenReturn("club123");
        when(session.getAttribute("tipo")).thenReturn("club");
        Club club = new Club();
        club.setNombre("Club ABC");

        when(clubService.findByUsername("club123")).thenReturn(Optional.of(club));

        String result = clubWebController.clubHome(session, model);

        assertEquals("Club/club_home", result);

        // Corregido: Verificar que se añade el objeto "club" completo al modelo.
        verify(model).addAttribute("club", club);
    }

    /**
     * Prueba que el método clubHome redirige al login si el usuario de la sesión
     * no corresponde a un club existente en la base de datos.
     */
    @Test
    void testClubHomeRedirigeAlLoginSiClubNoExiste() {
        when(session.getAttribute("username")).thenReturn("club123");
        when(clubService.findByUsername("club123")).thenReturn(Optional.empty());

        String result = clubWebController.clubHome(session, model);

        // Corregido: Verificar que se redirige al login
        assertEquals("redirect:/login", result);
        // Corregido: Verificar que NO se añade ningún atributo al modelo
        verify(model, never()).addAttribute(anyString(), any());
    }

    /**
     * Prueba que el método listarClubes carga una lista de clubes y muestra la vista correcta.
     */
    @Test
    void testListarClubes() {
        // Given
        List<Club> clubes = Collections.singletonList(new Club());
        when(clubService.getAllClubs(Sort.by("nombre"))).thenReturn(clubes);

        // When
        String result = clubWebController.listarClubes(null, null, model);

        // Then
        assertEquals("Club/club_lista", result);
        verify(model, times(1)).addAttribute("clubes", clubes);

    }

    /**
     * Prueba que el método showRegistroClub devuelva la vista de registro de clubes.
     */
    @Test
    void testMostrarRegistroClub() {
        when(model.containsAttribute("clubForm")).thenReturn(false);

        String result = clubWebController.showRegistroClub(model);

        assertEquals("Club/registro_club", result);
        verify(model).addAttribute(eq("clubForm"), any(ClubRegistroDTO.class));

    }

    /**
     * Prueba el registro de un club con datos válidos.
     */
    @Test
    void testDoRegistroClubExitoso() {
        ClubRegistroDTO dto = new ClubRegistroDTO();
        dto.setUsername("nuevoClub@email.com");
        dto.setNombre("Nuevo Club");
        dto.setPassword("1234");
        dto.setSensei("Sensei Name");
        dto.setAnoFundacion("2021");
        dto.setDireccion("Mi dirección 321");
        dto.setHorarios("Martes");

        when(clubService.findByUsername("nuevoClub@email.com")).thenReturn(Optional.empty());
        when(bindingResult.hasErrors()).thenReturn(false);

        String result = clubWebController.doRegistroClub(dto, bindingResult, model);

        assertEquals("redirect:/login", result);
        verify(clubService).guardarClub(any(Club.class));
        verify(model).addAttribute(eq("success"), contains("Club registrado correctamente"));
    }

    /**
     * Prueba el registro de un club con campos obligatorios vacíos.
     */
    @Test
    void testDoRegistroClubSinUsername() {
        ClubRegistroDTO dto = new ClubRegistroDTO();
        dto.setNombre("Club ABC");
        dto.setPassword("password123");
        dto.setUsername(""); // Usuario vacio

        when(bindingResult.hasErrors()).thenReturn(true);

        String result = clubWebController.doRegistroClub(dto, bindingResult, model);

        assertEquals("Club/registro_club", result);
        verify(bindingResult, never()).rejectValue(eq("username"), anyString(), anyString());
        verify(clubService, never()).guardarClub(any(Club.class));

    }

    /**
     * Prueba el registro de un club cuando el nombre de usuario ya está registrado.
     */
    @Test
    void testDoRegistroClubConUsernameYaRegistrado() {
        ClubRegistroDTO dto = new ClubRegistroDTO();
        dto.setNombre("Club ABC");
        dto.setPassword("password123");
        dto.setUsername("club123@test.com");

        when(clubService.findByUsername("club123@test.com")).thenReturn(Optional.of(new Club()));
        when(bindingResult.hasErrors()).thenReturn(true);

        String result = clubWebController.doRegistroClub(dto, bindingResult, model);

        assertEquals("Club/registro_club", result);
        verify(bindingResult).rejectValue("username", "error.clubForm", "El correo ya está registrado para un club.");
        verify(clubService, never()).guardarClub(any(Club.class));

    }
}