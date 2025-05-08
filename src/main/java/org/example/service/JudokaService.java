package org.example.service;

import lombok.AllArgsConstructor;
import org.example.model.judoka.Judoka;
import org.example.repository.JudokaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Judoka service.
 */
@AllArgsConstructor
@Service
public class JudokaService {

    private final JudokaRepository judokaRepository;

    /**
     * Listar judokas list.
     *
     * @return the list
     */
    public List<Judoka> listarJudokas() {
        return judokaRepository.findAll();
    }

    /**
     * Buscar por id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Judoka> buscarPorId(Long id) {
        return judokaRepository.findById(id);
    }

    /**
     * Buscar por nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    public List<Judoka> buscarPorNombre(String nombre) {
        return judokaRepository.findByNombre(nombre);
    }

    /**
     * Guardarjudoka judoka.
     *
     * @param judoka the judoka
     * @return the judoka
     */
    public Judoka guardarJudoka(Judoka judoka) {
        return judokaRepository.save(judoka);
    }

    /**
     * Eliminar judoka.
     *
     * @param id the id
     */
    public void eliminarJudoka(Long id) {
        judokaRepository.deleteById(id);
    }

    /**
     * Buscar por ids list.
     *
     * @param participantes the participantes
     * @return the list
     */
    public List<Judoka> buscarPorIds(List<Long> participantes) {
        return judokaRepository.findAllById(participantes);
    }
}
