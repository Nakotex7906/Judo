package org.example.service;

import org.example.model.user.Club;
import org.example.model.user.Judoka;
import org.example.repository.ClubRepository;
import org.example.repository.JudokaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Pruebas unitarias para {@link UserService}, encargado de proveer autenticación
 * para Spring Security a partir de Clubes o Judokas registrados.
 *
 * <p>Se valida:</p>
 * <ul>
 *     <li>Carga de usuario como Club.</li>
 *     <li>Carga de usuario como Judoka si no es Club.</li>
 *     <li>Lanzamiento de excepción si no se encuentra en ninguno.</li>
 * </ul>
 */
class UserServiceTest {

    private ClubRepository clubRepositoryMock;
    private JudokaRepository judokaRepositoryMock;
    private UserService userService;

    @BeforeEach
    void setUp() {
        clubRepositoryMock = mock(ClubRepository.class);
        judokaRepositoryMock = mock(JudokaRepository.class);
        userService = new UserService(clubRepositoryMock, judokaRepositoryMock);
    }

    /**
     * Verifica que se retorne un UserDetails con rol CLUB si el usuario está en ClubRepository.
     */
    @Test
    void loadUserByUsername_usuarioEsClub_devuelveUserDetailsConRolClub() {
        Club club = new Club();
        club.setUsername("club1");
        club.setPassword("clave123");

        when(clubRepositoryMock.findByUsername("club1")).thenReturn(Optional.of(club));

        UserDetails userDetails = userService.loadUserByUsername("club1");

        assertEquals("club1", userDetails.getUsername());
        assertEquals("clave123", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_CLUB")));
    }

    /**
     * Verifica que se retorne un UserDetails con rol JUDOKA si el usuario no está en Club pero sí en Judoka.
     */
    @Test
    void loadUserByUsername_usuarioEsJudoka_devuelveUserDetailsConRolJudoka() {
        when(clubRepositoryMock.findByUsername("judoka1")).thenReturn(Optional.empty());

        Judoka judoka = new Judoka();
        judoka.setUsername("judoka1");
        judoka.setPassword("judopass");

        when(judokaRepositoryMock.findByUsername("judoka1")).thenReturn(Optional.of(judoka));

        UserDetails userDetails = userService.loadUserByUsername("judoka1");

        assertEquals("judoka1", userDetails.getUsername());
        assertEquals("judopass", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_JUDOKA")));
    }

    /**
     * Verifica que se lance una excepción si el usuario no se encuentra ni como Club ni como Judoka.
     */
    @Test
    void loadUserByUsername_usuarioNoExiste_lanzaExcepcion() {
        when(clubRepositoryMock.findByUsername("desconocido")).thenReturn(Optional.empty());
        when(judokaRepositoryMock.findByUsername("desconocido")).thenReturn(Optional.empty());

        assertThrows(UsernameNotFoundException.class,
                () -> userService.loadUserByUsername("desconocido"));
    }
}
