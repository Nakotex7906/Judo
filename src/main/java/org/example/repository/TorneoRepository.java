package org.example.repository;

import org.example.model.competencia.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Competencia repository.
 */
public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    /**
     * Find by nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    List<Torneo> findByNombre(String nombre);

}
