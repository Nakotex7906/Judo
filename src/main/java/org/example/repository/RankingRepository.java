package org.example.repository;

import org.example.model.user.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para acceder a los datos de los judokas.
 */
public interface RankingRepository extends JpaRepository<Judoka, Long> {
}

