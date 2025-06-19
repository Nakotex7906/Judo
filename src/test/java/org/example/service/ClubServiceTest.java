package org.example.service;

import org.example.model.user.Club;
import org.example.repository.ClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias para el servicio {@link ClubService}, que gestiona operaciones sobre entidades Club.
 * Se prueba la autenticación, almacenamiento y recuperación de clubes.
 *
 * <p>Se emplean mocks para {@link ClubRepository} y {@link PasswordEncoder} para lograr bajo acoplamiento.</p>
 */
class ClubServiceTest {

    private ClubRepository clubRepositoryMock;
    private PasswordEncoder passwordEncoderMock;
    private ClubService clubService;

    /**
     * Inicializa los mocks y la instancia de ClubService antes de cada prueba.
     */
    @BeforeEach
    void setUp() {
        clubRepositoryMock = mock(ClubRepository.class);
        passwordEncoderMock = mock(PasswordEncoder.class);
        clubService = new ClubService(clubRepositoryMock, passwordEncoderMock);
    }

    /**
     * Verifica que se devuelva el club cuando existe en el repositorio.
     */
    @Test
    void findByUsername_existente_devuelveOptionalConClub() {
        Club club = new Club();
        club.setUsername("club1");
        when(clubRepositoryMock.findByUsername("club1")).thenReturn(Optional.of(club));

        Optional<Club> resultado = clubService.findByUsername("club1");

        assertTrue(resultado.isPresent());
        assertEquals("club1", resultado.get().getUsername());
    }

    /**
     * Verifica que la validación de contraseña retorne true si las credenciales coinciden.
     */
    @Test
    void validarContrasena_credencialesValidas_devuelveTrue() {
        Club club = new Club();
        club.setPassword("$2a$hashedPassword");

        when(clubRepositoryMock.findByUsername("club1")).thenReturn(Optional.of(club));
        when(passwordEncoderMock.matches("password123", "$2a$hashedPassword")).thenReturn(true);

        boolean resultado = clubService.validarContrasena("club1", "password123");

        assertTrue(resultado);
    }

    /**
     * Verifica que la validación de contraseña retorne false si no hay coincidencia.
     */
    @Test
    void validarContrasena_credencialesInvalidas_devuelveFalse() {
        Club club = new Club();
        club.setPassword("$2a$hash");

        when(clubRepositoryMock.findByUsername("club1")).thenReturn(Optional.of(club));
        when(passwordEncoderMock.matches("incorrecta", "$2a$hash")).thenReturn(false);

        boolean resultado = clubService.validarContrasena("club1", "incorrecta");

        assertFalse(resultado);
    }

    /**
     * Verifica que si el usuario no existe, el método de validación devuelve false.
     */
    @Test
    void validarContrasena_usuarioNoExiste_devuelveFalse() {
        when(clubRepositoryMock.findByUsername("desconocido")).thenReturn(Optional.empty());

        boolean resultado = clubService.validarContrasena("desconocido", "pass");

        assertFalse(resultado);
    }

    /**
     * Verifica que la contraseña se encripte antes de guardar si no está encriptada.
     */
    @Test
    void guardarClub_conContrasenaNoEncriptada_encriptaYGuarda() {
        Club club = new Club();
        club.setPassword("plaintext");

        when(passwordEncoderMock.encode("plaintext")).thenReturn("$2a$encoded");

        clubService.guardarClub(club);

        assertEquals("$2a$encoded", club.getPassword());
        verify(clubRepositoryMock).save(club);
    }

    /**
     * Verifica que si la contraseña ya está encriptada, no se vuelva a encriptar.
     */
    @Test
    void guardarClub_conContrasenaEncriptada_noModificaNiDuplica() {
        Club club = new Club();
        club.setPassword("$2a$yaCodificada");

        clubService.guardarClub(club);

        verify(passwordEncoderMock, never()).encode(anyString());
        verify(clubRepositoryMock).save(club);
    }

    /**
     * Verifica que se devuelva la lista completa de clubes.
     */
    @Test
    void getAllClubs_devuelveListaClubes() {
        List<Club> clubes = List.of(new Club(), new Club());
        when(clubRepositoryMock.findAll()).thenReturn(clubes);

        List<Club> resultado = clubService.getAllClubs();

        assertEquals(2, resultado.size());
    }
}
