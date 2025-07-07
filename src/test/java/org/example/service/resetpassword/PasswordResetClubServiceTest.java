package org.example.service.resetpassword;

import org.example.model.user.Club;
import org.example.model.user.PasswordResetTokenClub;
import org.example.repository.ClubRepository;
import org.example.repository.PasswordResetTokenClubRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PasswordResetClubServiceTest {

    @Mock
    private ClubRepository clubRepo;

    @Mock
    private PasswordResetTokenClubRepository tokenRepo;

    @Mock
    private CorreoService correoService;

    private PasswordResetClubService passwordResetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        passwordResetService = new PasswordResetClubService(clubRepo, tokenRepo, correoService);
    }

    @Test
    void crearTokenExitoso() {
        // Preparar
        Club club = new Club();
        club.setUsername("club@test.com");
        club.setNombre("Club Test");

        when(clubRepo.findByUsername("club@test.com")).thenReturn(Optional.of(club));
        when(tokenRepo.findByClub(club)).thenReturn(Optional.empty());

        // Ejecutar
        passwordResetService.crearToken("club@test.com");

        // Verificar
        verify(tokenRepo).save(any(PasswordResetTokenClub.class));
        verify(correoService).enviarCorreo(
            eq("club@test.com"),
            eq("Recuperación de contraseña - JScore"),
            argThat(mensaje -> mensaje.contains("Hola Club Test") &&
                             mensaje.contains("http://localhost:8080/restablecer/club?token="))
        );
    }

    @Test
    void crearTokenConTokenExistente() {
        // Preparar
        Club club = new Club();
        club.setUsername("club@test.com");

        PasswordResetTokenClub tokenExistente = new PasswordResetTokenClub();

        when(clubRepo.findByUsername("club@test.com")).thenReturn(Optional.of(club));
        when(tokenRepo.findByClub(club)).thenReturn(Optional.of(tokenExistente));

        // Ejecutar
        passwordResetService.crearToken("club@test.com");

        // Verificar
        verify(tokenRepo).delete(tokenExistente);
        verify(tokenRepo).save(any(PasswordResetTokenClub.class));
    }

    @Test
    void crearTokenClubNoEncontrado() {
        // Preparar
        when(clubRepo.findByUsername("noexiste@test.com")).thenReturn(Optional.empty());

        // Ejecutar y Verificar
        assertThrows(RuntimeException.class, () ->
            passwordResetService.crearToken("noexiste@test.com")
        );
        verify(tokenRepo, never()).save(any());
        verify(correoService, never()).enviarCorreo(any(), any(), any());
    }

    @Test
    void validarTokenValido() {
        // Preparar
        String token = "token-valido";
        PasswordResetTokenClub resetToken = new PasswordResetTokenClub();
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        resetToken.setUsed(false);

        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(resetToken));

        // Ejecutar
        boolean resultado = passwordResetService.validarToken(token);

        // Verificar
        assertTrue(resultado);
    }

    @Test
    void validarTokenExpirado() {
        // Preparar
        String token = "token-expirado";
        PasswordResetTokenClub resetToken = new PasswordResetTokenClub();
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().minusMinutes(10));
        resetToken.setUsed(false);

        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(resetToken));

        // Ejecutar
        boolean resultado = passwordResetService.validarToken(token);

        // Verificar
        assertFalse(resultado);
    }

    @Test
    void validarTokenUsado() {
        // Preparar
        String token = "token-usado";
        PasswordResetTokenClub resetToken = new PasswordResetTokenClub();
        resetToken.setToken(token);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(10));
        resetToken.setUsed(true);

        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(resetToken));

        // Ejecutar
        boolean resultado = passwordResetService.validarToken(token);

        // Verificar
        assertFalse(resultado);
    }

    @Test
    void actualizarPasswordExitoso() {
        // Preparar
        String token = "token-valido";
        String nuevaPassword = "NuevaPassword123";

        Club club = new Club();
        club.setUsername("club@test.com");

        PasswordResetTokenClub resetToken = new PasswordResetTokenClub();
        resetToken.setToken(token);
        resetToken.setClub(club);

        when(tokenRepo.findByToken(token)).thenReturn(Optional.of(resetToken));

        // Ejecutar
        passwordResetService.actualizarPassword(token, nuevaPassword);

        // Verificar
        ArgumentCaptor<Club> clubCaptor = ArgumentCaptor.forClass(Club.class);
        verify(clubRepo).save(clubCaptor.capture());

        Club clubGuardado = clubCaptor.getValue();
        assertTrue(new BCryptPasswordEncoder().matches(nuevaPassword, clubGuardado.getPassword()));

        verify(tokenRepo).save(argThat(t -> t.isUsed()));
    }

    @Test
    void actualizarPasswordTokenInvalido() {
        // Preparar
        String token = "token-invalido";
        when(tokenRepo.findByToken(token)).thenReturn(Optional.empty());

        // Ejecutar y Verificar
        assertThrows(RuntimeException.class, () ->
            passwordResetService.actualizarPassword(token, "cualquier-password")
        );

        verify(clubRepo, never()).save(any());
        verify(tokenRepo, never()).save(any());
    }
}
