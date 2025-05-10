package org.example.repository;

import org.example.model.user.Club;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * The interface Club repository.
 */
public interface ClubRepository extends JpaRepository<Club, Long> {

    /**
     * Find by nombre list.
     *
     * @param nombre the nombre
     * @return the list
     */
    List<Club> findByNombre(String nombre);

}
