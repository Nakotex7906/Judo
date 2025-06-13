package org.example.service.resetPassword;

import org.example.model.user.Judoka;
import org.example.model.user.PasswordResetTokenJudoka;
import org.example.repository.JudokaRepository;
import org.example.repository.PasswordResetTokenJudokaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetJudokaService {

    @Autowired
    private JudokaRepository judokaRepo;

    @Autowired
    private PasswordResetTokenJudokaRepository tokenRepo;

    @Autowired
    private CorreoService correoService;

    public void crearToken(String username) {
        Judoka judoka = judokaRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Judoka no encontrado"));

        // Eliminar token existente si lo hay
        tokenRepo.findByJudoka(judoka).ifPresent(tokenRepo::delete);

        String token = UUID.randomUUID().toString();
        PasswordResetTokenJudoka resetToken = new PasswordResetTokenJudoka();
        resetToken.setToken(token);
        resetToken.setJudoka(judoka);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        tokenRepo.save(resetToken);

        String link = "http://localhost:8080/restablecer/judoka?token=" + token;

        correoService.enviarCorreo(
                judoka.getUsername(),
                "Recuperación de contraseña - JScore",
                "Hola " + judoka.getNombre() + ",\n\n" +
                        "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" +
                        link + "\n\nEste enlace expirará en 30 minutos."
        );
    }

    public boolean validarToken(String token) {
        var resetToken = tokenRepo.findByToken(token);
        return resetToken.isPresent() && !resetToken.get().isExpired() && !resetToken.get().isUsed();
    }

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
