package org.example.repository;

import org.example.model.user.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repositorio para acceder a los datos de los judokas utilizados en el sistema de ranking.
 * <p>
 * Extiende de {@link JpaRepository} para proporcionar operaciones CRUD y
 * funcionalidades adicionales de JPA sobre la entidad {@link Judoka}.
 * </p>
 *
 * Este repositorio puede ser extendido para definir consultas personalizadas relacionadas
 * con el ranking, como ordenar por victorias, empates, derrotas, etc.
 *
 * @author Benjamin Beroiza
 */
public interface RankingRepository extends JpaRepository<Judoka, Long> {
}
