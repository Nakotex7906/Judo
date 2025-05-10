package org.example.service;

import org.example.model.club.Club;
import org.example.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClubService {

    private final ClubRepository clubRepository;

    public ClubService(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }

    public Optional<Club> findByUsername(String username) {
        return clubRepository.findByUsername(username);
    }

    public boolean validarContrasena(String username, String password) {
        return clubRepository.findByUsername(username)
                .map(club -> club.getPassword().equals(password))
                .orElse(false);
    }

    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    public Club guardarClub(Club club) {
        return clubRepository.save(club);
    }

}