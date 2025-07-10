package org.example.repository;

import org.example.model.competencia.Torneo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repositorio para acceder y manipular los datos de torneos.
 * <p>
 * Extiende de {@link JpaRepository} para proporcionar operaciones CRUD sobre la entidad {@link Torneo}.
 * </p>
 *
 * Este repositorio permite buscar torneos por nombre y puede ser extendido con consultas personalizadas.
 *
 * @author Ignacio Essus, Alonso Romero, Benjamin Beroiza
 */
public interface TorneoRepository extends JpaRepository<Torneo, Long> {

    /**
     * Busca una lista de torneos por su nombre exacto.
     *
     * @param nombre el nombre del torneo a buscar
     * @return una lista de torneos que coincidan con el nombre dado
     */
    List<Torneo> findByNombre(String nombre);

}
