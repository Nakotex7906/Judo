package org.example.repository;

import org.example.model.user.Club;
import org.example.model.user.PasswordResetTokenClub;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link PasswordResetTokenClub}.
 * <p>
 * Permite realizar operaciones de acceso a datos relacionadas con los tokens de recuperación
 * de contraseña para clubes, como la búsqueda por club o por token.
 * </p>
 *
 * @author Benjamin Beroiza
 */
public interface PasswordResetTokenClubRepository extends JpaRepository<PasswordResetTokenClub, Long> {

    /**
     * Busca un token de restablecimiento de contraseña asociado a un club específico.
     *
     * @param club el club del cual se desea obtener el token
     * @return un {@link Optional} que contiene el token si existe
     */
    Optional<PasswordResetTokenClub> findByClub(Club club);

    /**
     * Busca un token de restablecimiento de contraseña por su valor de cadena (token).
     *
     * @param token el valor del token
     * @return un {@link Optional} que contiene el token si existe
     */
    Optional<PasswordResetTokenClub> findByToken(String token);
}
