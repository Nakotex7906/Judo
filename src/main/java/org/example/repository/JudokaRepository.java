package org.example.repository;

import org.example.model.user.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;



import java.util.List;
import java.util.Optional;
/**
 * The interface Judoka repository.
 */
public interface JudokaRepository extends JpaRepository<Judoka, Long> {

    Optional<Judoka> findByUsername(String username);

    //Se añade un método para buscar todos los judokas cuyo campo 'club' es nulo.
    List<Judoka> findByClubIsNull();

}
