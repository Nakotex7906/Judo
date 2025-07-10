package org.example.repository;

import org.example.model.user.Judoka;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link Judoka}.
 * <p>
 * Proporciona métodos CRUD y consultas personalizadas relacionadas con judokas.
 * Extiende {@link JpaRepository}, lo que incluye operaciones básicas como
 * guardar, eliminar, encontrar por ID, etc.
 * </p>
 *
 * @author Alonso Romero
 */
public interface JudokaRepository extends JpaRepository<Judoka, Long> {

    /**
     * Busca un judoka por su nombre de usuario (correo electrónico).
     *
     * @param username el nombre de usuario del judoka
     * @return un {@code Optional<Judoka>} si se encuentra
     */
    Optional<Judoka> findByUsername(String username);

    /**
     * Devuelve una lista de judokas que no pertenecen a ningún club.
     * <p>
     * Esto se usa comúnmente al registrar nuevos clubes o asignar judokas.
     * </p>
     *
     * @return lista de judokas sin club asignado
     */
    List<Judoka> findByClubIsNull();
}
