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

    public void crearToken(String username) {
        Judoka judoka = judokaRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Judoka no encontrado"));

        String token = UUID.randomUUID().toString();
        PasswordResetTokenJudoka resetToken = new PasswordResetTokenJudoka();
        resetToken.setToken(token);
        resetToken.setJudoka(judoka);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        tokenRepo.save(resetToken);

        // Aquí agregar lógica para enviar email
        System.out.println("Token generado para judoka: " + token);
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
