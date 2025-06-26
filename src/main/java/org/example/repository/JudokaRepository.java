package org.example.repository;

import org.example.model.user.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.util.List;
import java.util.Optional;
/**
 * The interface Judoka repository.
 */
public interface JudokaRepository extends JpaRepository<Judoka, Long> {

    Optional<Judoka> findByUsername(String username);

    //Se añade un método para buscar todos los judokas cuyo campo 'club' es nulo.
    List<Judoka> findByClubIsNull();

    // MODIFICADO: Se añade esta consulta para cargar al judoka y su club asociado.
    @Query("SELECT j FROM Judoka j LEFT JOIN FETCH j.club WHERE j.username = :username")
    Optional<Judoka> findByUsernameWithClub(@Param("username") String username);
}
