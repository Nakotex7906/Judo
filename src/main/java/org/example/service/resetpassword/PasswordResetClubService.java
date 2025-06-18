package org.example.service.resetpassword;

import org.example.model.user.Club;
import org.example.model.user.PasswordResetTokenClub;
import org.example.repository.ClubRepository;
import org.example.repository.PasswordResetTokenClubRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetClubService {

    private ClubRepository clubRepo;

    private PasswordResetTokenClubRepository tokenRepo;

    private CorreoService correoService;

    public PasswordResetClubService(ClubRepository clubRepo, PasswordResetTokenClubRepository tokenRepo,
                                    CorreoService correoService) {
        this.clubRepo = clubRepo;
        this.tokenRepo = tokenRepo;
        this.correoService = correoService;
    }

    public void crearToken(String username) {
        Club club = clubRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Club no encontrado"));

        // Eliminar token anterior si ya existe
        tokenRepo.findByClub(club).ifPresent(tokenRepo::delete);

        String token = UUID.randomUUID().toString();
        PasswordResetTokenClub resetToken = new PasswordResetTokenClub();
        resetToken.setToken(token);
        resetToken.setClub(club);
        resetToken.setExpiryDate(LocalDateTime.now().plusMinutes(30));
        resetToken.setUsed(false);

        tokenRepo.save(resetToken);

        String link = "http://localhost:8080/restablecer/club?token=" + token;

        correoService.enviarCorreo(
                club.getUsername(), // asegurarse de que username es un correo
                "Recuperación de contraseña - JScore",
                "Hola " + club.getNombre() + ",\n\n" +
                        "Para restablecer tu contraseña, haz clic en el siguiente enlace:\n" +
                        link + "\n\nEste enlace expirará en 30 minutos."
        );
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
