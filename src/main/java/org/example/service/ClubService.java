package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.user.Club;
import org.example.repository.ClubRepository;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio encargado de la gestión de entidades {@link Club}.
 * Incluye operaciones de búsqueda, validación, guardado y eliminación.
 */
@AllArgsConstructor
@Service
public class ClubService {

    private final ClubRepository clubRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Busca un club por su username.
     *
     * @param username el correo del club
     * @return el club correspondiente si existe
     */
    public Optional<Club> findByUsername(String username) {
        return clubRepository.findByUsername(username);
    }

    /**
     * Busca un club por su ID, incluyendo los judokas asociados.
     *
     * @param id el ID del club
     * @return el club con sus judokas si existe
     */
    public Optional<Club> buscarPorIdConJudokas(Long id) {
        return clubRepository.findByIdWithJudokas(id);
    }

    /**
     * Obtiene todos los clubes ordenados por un criterio dado.
     *
     * @param sort criterio de ordenamiento
     * @return lista de clubes ordenada
     */
    public List<Club> getAllClubs(Sort sort) {
        return clubRepository.findAll(sort);
    }

    /**
     * Busca un club por su username, incluyendo los judokas asociados.
     *
     * @param username el username del club
     * @return club con sus judokas si existe
     */
    public Optional<Club> findByUsernameWithJudokas(String username) {
        return clubRepository.findByUsernameWithJudokas(username);
    }

    /**
     * Valida una contraseña cruda comparándola con la contraseña encriptada del club.
     *
     * @param username username del club
     * @param password contraseña sin encriptar
     * @return true si es válida, false si no lo es o el club no existe
     */
    public boolean validarContrasena(String username, String password) {
        return clubRepository.findByUsername(username)
                .map(club -> passwordEncoder.matches(password, club.getPassword()))
                .orElse(false);
    }

    /**
     * Obtiene todos los clubes registrados.
     *
     * @return lista de todos los clubes
     */
    public List<Club> getAllClubs() {
        return clubRepository.findAll();
    }

    /**
     * Guarda un club, encriptando su contraseña si aún no lo está.
     *
     * @param club el club a guardar
     */
    public void guardarClub(Club club) {
        if (!club.getPassword().startsWith("$2a") && !club.getPassword().startsWith("$2b")) {
            club.setPassword(passwordEncoder.encode(club.getPassword()));
        }
        clubRepository.save(club);
    }

    /**
     * Busca clubes por nombre, sin importar mayúsculas o minúsculas.
     *
     * @param nombre parte del nombre del club
     * @return lista de clubes cuyo nombre coincide
     */
    public List<Club> buscarPorNombre(String nombre) {
        return clubRepository.findByNombreContainingIgnoreCase(nombre);
    }

    /**
     * Elimina la cuenta del club y desvincula a sus judokas.
     *
     * @param username el username del club a eliminar
     * @throws IllegalArgumentException si el club no se encuentra
     */
    public void eliminarCuentaClub(String username) {
        Optional<Club> clubOpt = findByUsername(username);
        if (clubOpt.isPresent()) {
            Club club = clubOpt.get();
            club.getJudokas().forEach(judoka -> judoka.setClub(null));
            club.getJudokas().clear();
            clubRepository.delete(club);
        } else {
            throw new IllegalArgumentException("Club no encontrado con el username: " + username);
        }
    }
}
