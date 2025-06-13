package org.example.service.resetPassword;

import org.example.model.user.Club;
import org.example.model.user.PasswordResetTokenClub;
import org.example.repository.ClubRepository;
import org.example.repository.PasswordResetTokenClubRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetClubService {

    @Autowired
    private ClubRepository clubRepo;

    @Autowired
    private PasswordResetTokenClubRepository tokenRepo;

    public void crearToken(String username) {
        Club club = clubRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Club no encontrado"));

        String token = UUID.randomUUID().toString();
        PasswordResetTokenClub resetToken = new PasswordResetTokenClub();
        resetToken.setToken(token);
        resetToken.setClub(club);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        tokenRepo.save(resetToken);

        // Lógica de envío de correo
        System.out.println("Token generado para club: " + token);
    }

    public boolean validarToken(String token) {
        var resetToken = tokenRepo.findByToken(token);
        return resetToken.isPresent() && !resetToken.get().isExpired() && !resetToken.get().isUsed();
    }

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

