package org.example.service.resetpassword;

import lombok.AllArgsConstructor;
import org.example.model.user.Judoka;
import org.example.model.user.PasswordResetTokenJudoka;
import org.example.repository.JudokaRepository;
import org.example.repository.PasswordResetTokenJudokaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Servicio para la recuperación de contraseña de usuarios tipo Judoka.
 * <p>
 * Permite generar tokens, validar su vigencia y actualizar la contraseña
 * mediante un proceso de recuperación controlado por token.
 * </p>
 */
@AllArgsConstructor
@Service
public class PasswordResetJudokaService {

    private JudokaRepository judokaRepo;
    private PasswordResetTokenJudokaRepository tokenRepo;
    private CorreoService correoService;

    /**
     * Crea un nuevo token de recuperación para el judoka identificado por su username (email).
     * Elimina cualquier token anterior y envía el nuevo enlace por correo.
     *
     * @param username el correo electrónico del judoka
     */
    public void crearToken(String username) {
        Judoka judoka = judokaRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Judoka no encontrado"));

        // Eliminar token anterior si existe
        tokenRepo.findByJudoka(judoka).ifPresent(tokenRepo::delete);

        // Crear nuevo token
        String token = UUID.randomUUID().toString();
        PasswordResetTokenJudoka resetToken = new PasswordResetTokenJudoka();
        resetToken.setToken(token);
        resetToken.setJudoka(judoka);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        tokenRepo.save(resetToken);

        // Enviar correo con enlace de recuperación
        String link = "http://localhost:8080/restablecer/judoka?token=" + token;

        correoService.enviarCorreo(
                judoka.getUsername(),
                "Recuperación de contraseña - JScore",
                "Hola " + judoka.getNombre() + ",\n\n" +
                        "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" +
                        link + "\n\nEste enlace expirará en 30 minutos."
        );
    }

    /**
     * Valida si un token es válido, no expirado y no ha sido usado.
     *
     * @param token el token a validar
     * @return true si el token es válido, false si es inválido, expirado o ya usado
     */
    public boolean validarToken(String token) {
        var resetToken = tokenRepo.findByToken(token);
        return resetToken.isPresent() && !resetToken.get().isExpired() && !resetToken.get().isUsed();
    }

    /**
     * Actualiza la contraseña del judoka asociado al token, usando encriptación.
     * Marca el token como usado una vez aplicado el cambio.
     *
     * @param token         el token válido de recuperación
     * @param nuevaPassword la nueva contraseña sin encriptar
     */
    public void actualizarPassword(String token, String nuevaPassword) {
        PasswordResetTokenJudoka resetToken = tokenRepo.findByToken(token)
                .orElseThrow(() -> new RuntimeException("Token inválido"));

        Judoka judoka = resetToken.getJudoka();
        judoka.setPassword(new BCryptPasswordEncoder().encode(nuevaPassword));
        judokaRepo.save(judoka);

        resetToken.setUsed(true);
        tokenRepo.save(resetToken);
    }
}
