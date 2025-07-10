package org.example.service.resetpassword;

import org.example.model.user.Club;
import org.example.model.user.PasswordResetTokenClub;
import org.example.repository.ClubRepository;
import org.example.repository.PasswordResetTokenClubRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Servicio para gestionar el proceso de recuperación de contraseña para usuarios tipo Club.
 * <p>
 * Esta clase permite generar tokens únicos para restablecimiento,
 * validar su vigencia y actualizar la contraseña del club correspondiente.
 * </p>
 */
@Service
public class PasswordResetClubService {

    private final ClubRepository clubRepo;
    private final PasswordResetTokenClubRepository tokenRepo;
    private final CorreoService correoService;

    /**
     * Constructor del servicio de recuperación para Club.
     *
     * @param clubRepo     repositorio de clubes
     * @param tokenRepo    repositorio de tokens de recuperación
     * @param correoService servicio para el envío de correos electrónicos
     */
    public PasswordResetClubService(ClubRepository clubRepo, PasswordResetTokenClubRepository tokenRepo,
                                    CorreoService correoService) {
        this.clubRepo = clubRepo;
        this.tokenRepo = tokenRepo;
        this.correoService = correoService;
    }

    /**
     * Genera y guarda un nuevo token de recuperación para el club identificado por su username (email).
     * Si ya existía un token anterior, lo elimina. Luego, envía un correo con el enlace de recuperación.
     *
     * @param username el correo electrónico del club
     */
    public void crearToken(String username) {
        Club club = clubRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Club no encontrado"));

        // Elimina token previo si existe
        tokenRepo.findByClub(club).ifPresent(tokenRepo::delete);

        // Crear nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetTokenClub resetToken = new PasswordResetTokenClub();
        resetToken.setToken(token);
        resetToken.setClub(club);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        tokenRepo.save(resetToken);

        // Enviar enlace por correo
        String link = "http://localhost:8080/restablecer/club?token=" + token;

        correoService.enviarCorreo(
                club.getUsername(), // se asume que el username es un correo válido
                "Recuperación de contraseña - JScore",
                "Hola " + club.getNombre() + ",\n\n" +
                        "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" +
                        link + "\n\nEste enlace expirará en 30 minutos."
        );
    }

    /**
     * Verifica si un token es válido, no ha expirado y no ha sido usado.
     *
     * @param token el token a validar
     * @return true si el token es válido, false en caso contrario
     */
    public boolean validarToken(String token) {
        var resetToken = tokenRepo.findByToken(token);
        return resetToken.isPresent() && !resetToken.get().isExpired() && !resetToken.get().isUsed();
    }

    /**
     * Actualiza la contraseña del club asociado al token dado, encriptándola.
     * Marca el token como usado después del cambio.
     *
     * @param token         el token de recuperación válido
     * @param nuevaPassword la nueva contraseña sin encriptar
     */
    public void actualizarPassword(String token, String nuevaPassword) {
        PasswordResetTokenClub resetToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        Club club = resetToken.getClub();
        club.setPassword(new BCryptPasswordEncoder().encode(nuevaPassword));
        clubRepo.save(club);

        resetToken.setUsed(true);
        tokenRepo.save(resetToken);
    }
}
