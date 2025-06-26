package org.example.service;

import org.example.model.user.Club;
import org.example.repository.ClubRepository;
import org.springframework.transaction.annotation.Transactional; // MODIFICADO: Se añade esta línea de import que faltaba.
import lombok.AllArgsConstructor;
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



    //Se añade un método para buscar un club por su ID.
    public Optional<Club> buscarPorId(Long id) {
        return clubRepository.findById(id);
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

    // MODIFICADO: Se añade este nuevo método transaccional. Es la forma más segura de cargar un club y su lista de judokas.
    @Transactional(readOnly = true)
    public Optional<Club> findAndInitializeJudokasByUsername(String username) {
        Optional<Club> clubOpt = clubRepository.findByUsername(username);
        // Al acceder a la lista de judokas aquí, forzamos a Hibernate a cargarla mientras la sesión de BD está abierta.
        clubOpt.ifPresent(club -> club.getJudokas().size());
        return clubOpt;
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