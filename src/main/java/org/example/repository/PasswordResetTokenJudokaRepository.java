package org.example.repository;

import org.example.model.user.Judoka;
import org.example.model.user.PasswordResetTokenJudoka;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Repositorio JPA para la entidad {@link PasswordResetTokenJudoka}.
 * <p>
 * Permite realizar operaciones de persistencia relacionadas con los tokens de restablecimiento
 * de contraseña para judokas.
 * </p>
 *
 * @author Benjamin Beroiza
 */
public interface PasswordResetTokenJudokaRepository extends JpaRepository<PasswordResetTokenJudoka, Long> {

    /**
     * Busca un token de restablecimiento de contraseña por su valor de cadena (token).
     *
     * @param token el valor del token
     * @return un {@link Optional} con el token si existe
     */
    Optional<PasswordResetTokenJudoka> findByToken(String token);

    /**
     * Busca el token de restablecimiento asociado a un judoka específico.
     *
     * @param judoka el judoka del cual se desea obtener el token
     * @return un {@link Optional} con el token si existe
     */
    Optional<PasswordResetTokenJudoka> findByJudoka(Judoka judoka);
}
