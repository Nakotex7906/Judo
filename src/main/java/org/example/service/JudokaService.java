package org.example.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.example.model.user.Judoka;
import org.example.repository.JudokaRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Servicio que gestiona las operaciones relacionadas con la entidad {@link Judoka}.
 * Incluye métodos para registrar, buscar, eliminar y validar judokas.
 */
@AllArgsConstructor
@Service
public class JudokaService {

    private final JudokaRepository judokaRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Lista todos los judokas registrados en el sistema.
     *
     * @return lista de judokas
     */
    public List<Judoka> listarJudokas() {
        return judokaRepository.findAll();
    }

    /**
     * Guarda un judoka, encriptando su contraseña si aún no lo está.
     *
     * @param judoka el judoka a guardar
     */
    public void guardarJudoka(Judoka judoka) {
        if (!judoka.getPassword().startsWith("$2a") && !judoka.getPassword().startsWith("$2b")) {
            judoka.setPassword(passwordEncoder.encode(judoka.getPassword()));
        }
        judokaRepository.save(judoka);
    }

    /**
     * Busca un judoka por su nombre de usuario.
     *
     * @param username el username del judoka
     * @return un {@link Optional} con el judoka si existe
     */
    public Optional<Judoka> findByUsername(String username) {
        return judokaRepository.findByUsername(username);
    }

    /**
     * Valida la contraseña de un judoka verificando que coincida con la almacenada en la base de datos.
     *
     * @param username nombre de usuario del judoka
     * @param password contraseña sin encriptar
     * @return true si la contraseña es válida, false si no o si el usuario no existe
     */
    public boolean validarContrasena(String username, String password) {
        Optional<Judoka> opt = findByUsername(username);
        return opt.map(j -> passwordEncoder.matches(password, j.getPassword())).orElse(false);
    }

    /**
     * Busca una lista de judokas a partir de una lista de IDs.
     *
     * @param participantes lista de IDs de judokas
     * @return lista de judokas encontrados
     */
    public List<Judoka> buscarPorIds(List<Long> participantes) {
        return judokaRepository.findAllById(participantes);
    }

    /**
     * Busca un judoka por su ID.
     *
     * @param id el ID del judoka
     * @return un {@link Optional} con el judoka si existe
     */
    public Optional<Judoka> buscarPorId(Long id) {
        return judokaRepository.findById(id);
    }

    /**
     * Lista todos los judokas que no están asociados a ningún club.
     *
     * @return lista de judokas sin club
     */
    public List<Judoka> listarJudokasSinClub() {
        return judokaRepository.findByClubIsNull();
    }

    /**
     * Elimina la cuenta de un judoka identificado por su username.
     *
     * @param username el nombre de usuario del judoka
     * @throws EntityNotFoundException si no se encuentra el judoka
     */
    public void eliminarCuentaJudoka(String username) {
        Optional<Judoka> judoka = findByUsername(username);
        if (judoka.isPresent()) {
            judokaRepository.delete(judoka.get());
        } else {
            throw new EntityNotFoundException("No se encontró el judoka con username: " + username);
        }
    }
}
