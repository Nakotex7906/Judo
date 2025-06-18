package org.example.service.resetpassword;

import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.model.user.PasswordResetTokenClub;
import org.example.repository.ClubRepository;
import org.example.repository.PasswordResetTokenClubRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

/**
 * Clase de prueba para PasswordResetClubService.
 * Esta clase contiene pruebas unitarias para el método crearToken,
 * que es responsable de generar y gestionar los tokens de restablecimiento de contraseña para los clubes.
 */
@SpringBootTest
@AllArgsConstructor
class PasswordResetClubServiceTest {

    @Autowired
    private PasswordResetClubService passwordResetClubService;

    private ClubRepository clubRepository;

    private PasswordResetTokenClubRepository tokenRepository;

    private CorreoService correoService;

    /**
     * Prueba que un token de restablecimiento de contraseña se crea con éxito
     * y que se envía un correo electrónico al club correspondiente.
     */
    @Test
    void crearToken_shouldCreateAndSendPasswordResetTokenSuccessfully() {
        // Arrange
        String username = "testClub";
        Club club = new Club();
        club.setUsername(username);
        club.setNombre("Test Club");

        Mockito.when(clubRepository.findByUsername(username)).thenReturn(Optional.of(club));
        Mockito.when(tokenRepository.findByClub(club)).thenReturn(Optional.empty());

        // Act
        passwordResetClubService.crearToken(username);

        // Assert
        Mockito.verify(tokenRepository).save(any(PasswordResetTokenClub.class));
        Mockito.verify(correoService).enviarCorreo(
                Mockito.eq(username),
                Mockito.eq("Recuperación de contraseña - JScore"),
                Mockito.contains("Hola Test Club,")
        );
    }

    /**
     * Prueba que se elimina un token de restablecimiento de contraseña existente
     * antes de crear uno nuevo.
     */
    @Test
    void crearToken_shouldDeleteExistingTokenBeforeCreatingNewOne() {
        // Arrange
        String username = "testClub";
        Club club = new Club();
        club.setUsername(username);
        club.setNombre("Test Club");

        PasswordResetTokenClub existingToken = new PasswordResetTokenClub();
        existingToken.setToken(UUID.randomUUID().toString());
        existingToken.setClub(club);
        existingToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));

        Mockito.when(clubRepository.findByUsername(username)).thenReturn(Optional.of(club));
        Mockito.when(tokenRepository.findByClub(club)).thenReturn(Optional.of(existingToken));

        // Act
        passwordResetClubService.crearToken(username);

        // Assert
        Mockito.verify(tokenRepository).delete(existingToken);
        Mockito.verify(tokenRepository).save(any(PasswordResetTokenClub.class));
        Mockito.verify(correoService).enviarCorreo(
                Mockito.eq(username),
                Mockito.eq("Recuperación de contraseña - JScore"),
                Mockito.contains("Hola Test Club,")
        );
    }

    /**
     * Prueba que se lanza una {@link RuntimeException} cuando no se encuentra
     * un club con el nombre de usuario proporcionado.
     */
    @Test
    void crearToken_shouldThrowExceptionWhenClubNotFound() {
        // Arrange
        String username = "nonExistentClub";
        Mockito.when(clubRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> passwordResetClubService.crearToken(username));
        Mockito.verify(tokenRepository, Mockito.never()).save(any(PasswordResetTokenClub.class));
        Mockito.verify(correoService, Mockito.never()).enviarCorreo(any(String.class), any(String.class), any(String.class));
    }
}