package org.example.service;

import org.example.model.competencia.Competencia;
import org.example.repository.CompetenciaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * The type Competencia service.
 */
@Service
public class CompetenciaService {

    private final CompetenciaRepository competenciaRepository;

    /**
     * Instantiates a new Competencia service.
     *
     * @param competenciaRepository the competencia repository
     */
    public CompetenciaService(CompetenciaRepository competenciaRepository) {
        this.competenciaRepository = competenciaRepository;
    }

    /**
     * Listar competencias list.
     *
     * @return the list
     */
    public List<Competencia> listarCompetencias() {
        return competenciaRepository.findAll();
    }

    /**
     * Buscar por id optional.
     *
     * @param id the id
     * @return the optional
     */
    public Optional<Competencia> buscarPorId(Long id) {
        return competenciaRepository.findById(id);
    }

    /**
     * Buscar por nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    public List<Competencia> buscarPorNombre(String nombre) {
        return competenciaRepository.findByNombre(nombre);
    }

    /**
     * Guardar competencia competencia.
     *
     * @param competencia the competencia
     * @return the competencia
     */
    public Competencia guardarCompetencia(Competencia competencia) {
        return competenciaRepository.save(competencia);
    }

    /**
     * Eliminar competencia.
     *
     * @param id the id
     */
    public void eliminarCompetencia(Long id) {
        competenciaRepository.deleteById(id);
    }


}
