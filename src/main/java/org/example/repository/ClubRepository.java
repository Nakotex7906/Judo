package org.example.repository;

import org.example.model.user.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Club}.
 * <p>
 * Proporciona métodos CRUD y consultas personalizadas para recuperar información
 * de clubes y sus judokas asociados.
 * </p>
 *
 * Se extiende de {@link JpaRepository}, lo que proporciona automáticamente métodos
 * como {@code save}, {@code findAll}, {@code findById}, {@code delete}, etc.
 *
 * @author Alonso Romero, Ignacio Essus
 */
public interface ClubRepository extends JpaRepository<Club, Long> {

    /**
     * Busca un club por su nombre de usuario (correo electrónico).
     *
     * @param username nombre de usuario del club
     * @return un {@code Optional<Club>} si se encuentra el club
     */
    Optional<Club> findByUsername(String username);

    /**
     * Busca un club por su ID e incluye la lista de judokas asociados usando una consulta con {@code fetch join}.
     *
     * @param id identificador del club
     * @return club con sus judokas precargados
     */
    @Query("SELECT c FROM Club c LEFT JOIN FETCH c.judokas WHERE c.id = :id")
    Optional<Club> findByIdWithJudokas(@Param("id") Long id);

    /**
     * Busca un club por su username e incluye sus judokas asociados.
     * <p>Usado principalmente para cargar el perfil completo del club autenticado.</p>
     *
     * @param username nombre de usuario (correo) del club
     * @return club con sus judokas cargados
     */
    @Query("SELECT c FROM Club c LEFT JOIN FETCH c.judokas WHERE c.username = :username")
    Optional<Club> findByUsernameWithJudokas(@Param("username") String username);

    /**
     * Busca todos los clubes cuyo nombre contenga la cadena proporcionada, ignorando mayúsculas o minúsculas.
     * <p>Usado en filtros por letra o búsqueda parcial.</p>
     *
     * @param nombre fragmento del nombre del club
     * @return lista de clubes coincidentes
     */
    List<Club> findByNombreContainingIgnoreCase(String nombre);
}
