package org.example.controllerweb;

import jakarta.servlet.http.HttpSession;
import org.example.model.user.Club;
import org.example.service.ClubService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class ClubWebControllerTest {

    private ClubWebController clubWebController;

    @Mock
    private ClubService clubService;

    @Mock
    private HttpSession session;

    @Mock
    private Model model;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        clubWebController = new ClubWebController(clubService);
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
     * Prueba que el método clubHome muestra el nombre del club correctamente cuando
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

        assertEquals("club_home", result);
        verify(model).addAttribute("nombre", "Club ABC");
    }

    /**
     * Prueba que el método clubHome utiliza el nombre de usuario como fallback
     * si no encuentra el club.
     */
    @Test
    void TestClubHomeNombreUsuarioComoFallback() {
        when(session.getAttribute("username")).thenReturn("club123");
        when(session.getAttribute("tipo")).thenReturn("club");

        when(clubService.findByUsername("club123")).thenReturn(Optional.empty());

        String result = clubWebController.clubHome(session, model);

        assertEquals("club_home", result);
        verify(model).addAttribute("nombre", "club123");
    }

    /**
     * Prueba que el método listarClubes carga una lista de clubes y muestra la vista correcta.
     */
    @Test
    void testListarClubes() {
        List<Club> clubes = List.of(new Club(), new Club());
        when(clubService.getAllClubs()).thenReturn(clubes);

        String result = clubWebController.listarClubes(model);

        assertEquals("club_lista", result);
        verify(model).addAttribute("clubes", clubes);
    }

    /**
     * Prueba que el método showRegistroClub devuelva la vista de registro de clubes.
     */
    @Test
    void testMostrarRegistroClub() {
        String result = clubWebController.showRegistroClub();

        assertEquals("registro_club", result);
    }

    /**
     * Prueba el registro de un club con datos válidos.
     */
    @Test
    void testDoRegistroClubExitoso() {
        when(clubService.findByUsername("club123@test.com")).thenReturn(Optional.empty());

        String result = clubWebController.doRegistroClub("club123@test.com", "password123", "Club ABC", model);

        assertEquals("redirect:/login?registrado=1", result); 
        verify(clubService).guardarClub(any(Club.class));
    }

    /**
     * Prueba el registro de un club con campos obligatorios vacíos.
     */
    @Test
    void testDoRegistroClubSinUsername() {
        String result = clubWebController.doRegistroClub("", "password123", "Club ABC", model);

        assertEquals("registro_club", result);
        verify(model).addAttribute("error", "Todos los campos son obligatorios.");
        verify(clubService, never()).guardarClub(any(Club.class));
    }

    /**
     * Prueba el registro de un club cuando el nombre de usuario ya está registrado.
     */
    @Test
    void testDoRegistroClubConUsernameYaRegistrado() {
        when(clubService.findByUsername("club123@test.com")).thenReturn(Optional.of(new Club()));

        String result = clubWebController.doRegistroClub("club123@test.com", "password123", "Club ABC", model);

        assertEquals("registro_club", result);
        verify(model).addAttribute("error", "El correo ya está registrado para un club.");
        verify(clubService, never()).guardarClub(any(Club.class));
    }
}