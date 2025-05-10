package org.example.repository;

import org.example.model.user.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Judoka repository.
 */
public interface JudokaRepository extends JpaRepository<Judoka, Long> {

    /**
     * Find by nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    List<Judoka> findByNombre(String nombre);

}
