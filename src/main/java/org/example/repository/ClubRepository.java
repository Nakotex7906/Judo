package org.example.repository;

import org.example.model.user.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club, Long> {
    Optional<Club> findByUsername(String username);

    @Query("SELECT c FROM Club c LEFT JOIN FETCH c.judokas WHERE c.id = :id")
    Optional<Club> findByIdWithJudokas(@Param("id") Long id);


    //Se añade esta consulta para cargar el perfil personal del club con sus integrantes.
    @Query("SELECT c FROM Club c LEFT JOIN FETCH c.judokas WHERE c.username = :username")
    Optional<Club> findByUsernameWithJudokas(@Param("username") String username);


}
