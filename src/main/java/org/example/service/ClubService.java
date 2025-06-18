package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.repository.ClubRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@AllArgsConstructor
@Service
public class ClubService{

    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;

    public Optional<Club> findByUsername(String username) {
        return clubRepository.findByUsername(username);
    }

    public boolean validarContrasena(String username, String password) {
        return clubRepository.findByUsername(username)
                .map(club -> passwordEncoder.matches(password, club.getPassword()))
                .orElse(false);
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    // Encripta la contraseña antes de guardar
    public void guardarClub(Club club) {
        // Solo la encripta si no está ya encriptada
        if (!club.getPassword().startsWith("$2a") && !club.getPassword().startsWith("$2b")) {
            club.setPassword(passwordEncoder.encode(club.getPassword()));
        }
         clubRepository.save(club);
    }


}