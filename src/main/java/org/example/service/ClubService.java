package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.repository.ClubRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Club service.
 */
@AllArgsConstructor
@Service
public class ClubService {

    private final ClubRepository clubRepository;

    /**
     * Listar clubs list.
     *
     * @return the list
     */
    public List<Club> listarClubs() {
        return clubRepository.findAll();
    }

    /**
     * Buscar por id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Club> buscarPorId(Long id) {
        return clubRepository.findById(id);
    }

    /**
     * Buscar por nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    public List<Club> buscarPorNombre(String nombre) {
        return clubRepository.findByNombre(nombre);
    }

    /**
     * Guardar club club.
     *
     * @param club the club
     * @return the club
     */
    public Club guardarClub(Club club) {
        return clubRepository.save(club);
    }

    /**
     * Eliminar club.
     *
     * @param id the id
     */
    public void eliminarClub(Long id) {
        clubRepository.deleteById(id);
    }
}
