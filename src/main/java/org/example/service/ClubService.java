package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.repository.ClubRepository;
import org.springframework.data.domain.Sort;
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

    // MODIFICADO: Se añade un nuevo método de servicio para llamar a nuestra consulta personalizada.
    public Optional<Club> buscarPorIdConJudokas(Long id) {
        return clubRepository.findByIdWithJudokas(id);
    }

    //Se añade un método para obtener todos los clubes ordenados.
    public List<Club> getAllClubs(Sort sort) {
        return clubRepository.findAll(sort);
    }

    //Se añade el nuevo método que llama a la consulta del repositorio.
    public Optional<Club> findByUsernameWithJudokas(String username) {
        return clubRepository.findByUsernameWithJudokas(username);
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

    // Metodo para busacr club por nombre
    public List<Club> buscarPorNombre(String nombre) {
        return clubRepository.findByNombreContainingIgnoreCase(nombre);
    }

    public void eliminarCuentaClub(String username) {
        Optional<Club> clubOpt = findByUsername(username);
        if (clubOpt.isPresent()) {
            Club club = clubOpt.get();
            // Elimina todos los judokas asociados al club
            club.getJudokas().forEach(judoka -> judoka.setClub(null));
            club.getJudokas().clear();
            // Elimina el club
            clubRepository.delete(club);
        }else {
            throw new IllegalArgumentException("Club no encontrado con el username: " + username);
        }
    }

}