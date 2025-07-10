package org.example.integration;

import org.example.model.competencia.Torneo;
import org.example.model.user.Club;
import org.example.model.user.Judoka;
import org.example.service.ClubService;
import org.example.service.JudokaService;
import org.example.service.TorneoService;
import org.example.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Pruebas de integración que validan el flujo de un usuario club creando un torneo
 * y asociando judokas al mismo.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ClubTournamentFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ClubService clubService;

    @MockBean
    private UserService userService;

    @MockBean
    private JudokaService judokaService;

    @MockBean
    private TorneoService torneoService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Verifica que un club pueda registrarse, iniciar sesión y crear un torneo con judokas.
     */
    @Test
    void testClubRegistersLogsInAndCreatesTournament() throws Exception {
        // Registro de club
        when(clubService.findByUsername("club@correo.es")).thenReturn(Optional.empty());
        doNothing().when(clubService).guardarClub(any(Club.class));

        mockMvc.perform(post("/registro-club").with(csrf())
                        .param("username", "club@correo.es")
                        .param("password", "clave")
                        .param("nombre", "Club Test")
                        .param("sensei", "Sensei")
                        .param("anoFundacion", "2020")
                        .param("direccion", "Calle 1")
                        .param("horarios", "9-18"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));

        // Login de club
        UserDetails userDetails = User.withUsername("club@correo.es")
                .password(passwordEncoder.encode("clave"))
                .roles("CLUB")
                .build();
        when(userService.loadUserByUsername("club@correo.es")).thenReturn(userDetails);

        MvcResult loginResult = mockMvc.perform(post("/login").with(csrf())
                        .param("username", "club@correo.es")
                        .param("password", "clave"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/index"))
                .andReturn();

        MockHttpSession session = (MockHttpSession) loginResult.getRequest().getSession(false);

        // Creación de torneo con judokas
        Judoka j1 = new Judoka();
        j1.setId(1L);
        Judoka j2 = new Judoka();
        j2.setId(2L);
        when(judokaService.buscarPorIds(List.of(1L, 2L))).thenReturn(List.of(j1, j2));
        when(torneoService.guardarTorneo(any(Torneo.class))).thenReturn(new Torneo());

        mockMvc.perform(post("/torneos/crear").with(csrf())
                        .session(session)
                        .param("nombre", "Torneo Nuevo")
                        .param("fecha", "2025-01-01")
                        .param("participantes", "1", "2"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/torneos"));
    }
}
