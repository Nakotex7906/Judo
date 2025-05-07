package org.example.repository;

import org.example.model.competencia.Competencia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Competencia repository.
 */
public interface CompetenciaRepository extends JpaRepository<Competencia, Long> {

    /**
     * Find by nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    List<Competencia> findByNombre(String nombre);

}
